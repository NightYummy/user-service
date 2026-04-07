import com.nightyummy.Main;
import com.nightyummy.dto.UserDTO;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.client.RestClient;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = Main.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class EmailServiceIntegrationTest {

    @Container
    private static final PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:latest")
            .withDatabaseName("testDB")
            .withUsername("test")
            .withPassword("test");

    @Container
    private static final KafkaContainer kafka = new KafkaContainer("apache/kafka:latest");

    @MockitoBean
    private JavaMailSender mailSender;

    @LocalServerPort
    private int port;

    private RestClient restClient;

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

    @BeforeEach
    void setup() {
        restClient = RestClient.builder()
                .baseUrl("http://localhost:" + port)
                .build();
        reset(mailSender);
    }

    @Test
    void testSendEmailOnCreateUser() {
        UserDTO newUser = new UserDTO("Ivan", "ivan@example.com", 25);

        ResponseEntity<UserDTO> response = restClient.post()
                .uri("/api/user")
                .body(newUser)
                .retrieve()
                .toEntity(UserDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getEmail()).isEqualTo("ivan@example.com");

        Awaitility.await()
                .atMost(Duration.ofSeconds(5))
                .untilAsserted(() -> verify(mailSender, times(1)).send(any(SimpleMailMessage.class)));

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(messageCaptor.capture());

        SimpleMailMessage sentMessage = messageCaptor.getValue();
        assertThat(sentMessage.getTo()).containsExactly("ivan@example.com");
        assertThat(sentMessage.getSubject()).isEqualTo("Создание аккаунта");
        assertThat(sentMessage.getText()).contains("успешно создан");
    }

    @Test
    void testSendEmailOnDeleteUser() {
        UserDTO newUser = new UserDTO("Oleg", "oleg@example.com", 30);
        ResponseEntity<UserDTO> createResponse = restClient.post()
                .uri("/api/user")
                .body(newUser)
                .retrieve()
                .toEntity(UserDTO.class);

        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        reset(mailSender);

        restClient.delete()
                .uri("/api/user/oleg@example.com")
                .retrieve()
                .toBodilessEntity();

        Awaitility.await()
                .atMost(Duration.ofSeconds(5))
                .untilAsserted(() -> {
                    ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
                    verify(mailSender, atLeast(1)).send(captor.capture());

                    List<SimpleMailMessage> messages = captor.getAllValues();
                    boolean deleteEmailSent = messages.stream()
                            .anyMatch(msg -> {
                                assert msg.getTo() != null;
                                if (!msg.getTo()[0].equals("oleg@example.com")) return false;
                                assert msg.getSubject() != null;
                                if (!msg.getSubject().equals("Удаление акаунта")) return false;
                                assert msg.getText() != null;
                                return msg.getText().contains("аккаунт был удален");
                            });
                    assertThat(deleteEmailSent).isTrue();
                });
    }
}

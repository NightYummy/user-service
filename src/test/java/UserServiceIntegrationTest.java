import com.nightyummy.Main;
import com.nightyummy.dao.UserRepository;
import com.nightyummy.dto.UserDTO;
import com.nightyummy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest(classes = Main.class)
public class UserServiceIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:latest")
        .withDatabaseName("test")
        .withUsername("test")
        .withPassword("test");

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @DynamicPropertySource
    static void setup(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @BeforeEach
    void clearTable() {
        userRepository.deleteAll();
    }

    @Test
    void testSaveAndGetUser() {
        UserDTO user = new UserDTO("Igor", "igor@mail.ru", 26);

        UserDTO saved = userService.createUser(user);
        assertNotNull(saved);
        assertEquals("Igor", saved.getName());
        assertEquals("igor@mail.ru", saved.getEmail());
        assertEquals(26, saved.getAge());

        UserDTO found = userService.getUserByEmail("igor@mail.ru");
        assertNotNull(found);
        assertEquals("Igor", found.getName());
        assertEquals("igor@mail.ru", found.getEmail());
        assertEquals(26, found.getAge());

        assertEquals(user, saved);
        assertEquals(user, found);
        assertEquals(saved, found);
    }

    @Test
    void testUpdateUser() {
        UserDTO user = new UserDTO("Igor", "igor@mail.ru", 26);
        userService.createUser(user);

        UserDTO found = userService.getUserByEmail("igor@mail.ru");
        found.setName("Egor");
        found.setEmail("egor@gmail.com");
        found.setAge(27);
        userService.updateUser("igor@mail.ru", found);

        UserDTO updated = userService.getUserByEmail("egor@gmail.com");
        assertNotNull(updated);
        assertEquals("Egor", updated.getName());
        assertEquals("egor@gmail.com", updated.getEmail());
        assertEquals(27, updated.getAge());
    }

    @Test
    void testDeleteUser() {
        UserDTO user = new UserDTO("Igor", "igor@mail.ru", 26);
        userService.createUser(user);

        UserDTO found = userService.getUserByEmail("igor@mail.ru");
        userService.deleteUser(found);

        assertThrows(RuntimeException.class, () -> userService.getUserByEmail("igor@mail.ru"));
    }
}

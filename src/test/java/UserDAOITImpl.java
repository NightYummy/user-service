import app.ConnectionConfiguration;
import dao.UserDAOImpl;
import dto.UserDTO;
import entities.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.util.Properties;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class UserDAOITImpl {

    @Container
    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:latest")
        .withDatabaseName("test")
        .withUsername("test")
        .withPassword("test");
    private static SessionFactory sessionFactory;
    private UserDAOImpl userDAO;

    @BeforeAll
    static void setup() {
        Properties properties = new Properties();
        properties.put(Environment.DRIVER, "org.postgresql.Driver");
        properties.put(Environment.URL, container.getJdbcUrl());
        properties.put(Environment.USER, container.getUsername());
        properties.put(Environment.PASS, container.getPassword());
        properties.put(Environment.HBM2DDL_AUTO, "create-drop");

        Configuration configuration = new Configuration();
        configuration.setProperties(properties);
        configuration.addAnnotatedClass(User.class);
        sessionFactory = configuration.buildSessionFactory();
    }

    @AfterAll
    static void shutdown() {
        sessionFactory.close();
    }

    @BeforeEach
    void clearTable() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createMutationQuery("DELETE FROM entities.User").executeUpdate();
            session.getTransaction().commit();
        }
        ConnectionConfiguration connection = new ConnectionConfiguration(sessionFactory);
        userDAO = new UserDAOImpl(connection);
    }

    @Test
    void testSaveAndGetUser() {
        UserDTO user = new UserDTO("Igor", "igor@mail.ru", 26);
        userDAO.saveUser(user);

        UserDTO found = userDAO.getUserByEmail("igor@mail.ru");
        assertNotNull(found);
        assertEquals("Igor", found.getName());
        assertEquals(26, found.getAge());
    }

    @Test
    void testUpdateUser() {
        UserDTO user = new UserDTO("Igor", "igor@mail.ru", 26);
        userDAO.saveUser(user);

        UserDTO found = userDAO.getUserByEmail("igor@mail.ru");
        found.setAge(27);
        found.setName("IGOR");
        userDAO.updateUser(found);

        UserDTO updated = userDAO.getUserByEmail("igor@mail.ru");
        assertEquals(27, updated.getAge());
        assertEquals("IGOR", updated.getName());
    }

    @Test
    void testUpdateUserEmail() {
        UserDTO user = new UserDTO("Igor", "igor@mail.ru", 26);
        userDAO.saveUser(user);

        UserDTO found = userDAO.getUserByEmail("igor@mail.ru");
        userDAO.updateUserEmail(found, "igor@gmail.com");

        UserDTO updated = userDAO.getUserByEmail("igor@gmail.com");
        assertNotNull(updated);
        assertEquals("igor@gmail.com", updated.getEmail());
    }

    @Test
    void testDeleteUser() {
        UserDTO user = new UserDTO("Igor", "igor@mail.ru", 26);
        userDAO.saveUser(user);

        UserDTO found = userDAO.getUserByEmail("igor@mail.ru");
        assertNotNull(found);

        userDAO.deleteUser(found);

        UserDTO deleted = userDAO.getUserByEmail("igor@mail.ru");
        assertNull(deleted);
    }
}

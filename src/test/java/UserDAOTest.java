import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserDAOTest {

    @Mock
    private ConnectionConfiguration connection;

    @Mock
    private Session session;

    @Mock
    private Transaction transaction;

    @Mock
    private Query<User> query;

    @InjectMocks
    private UserDAO userDAO;

    @BeforeEach
    void setup() {
        when(connection.getSession()).thenReturn(session);
    }

    @Test
    void testSaveUser() {
        UserDTO user = new UserDTO("Oleg", "oleg@mail.ru", 25);

        when(session.beginTransaction()).thenReturn(transaction);

        userDAO.saveUser(user);

        verify(session).persist(any(User.class));
        verify(transaction).commit();
        verifyNoMoreInteractions(transaction);
    }

    @Test
    void testGetUser() {
        User user = new User("Oleg", "oleg@mail.ru", 25);

        when(session.createQuery(anyString(), eq(User.class))).thenReturn(query);
        when(query.setParameter("email", user.getEmail())).thenReturn(query);
        when(query.uniqueResult()).thenReturn(user);

        UserDTO found = userDAO.getUserByEmail(user.getEmail());

        assertNotNull(found);
        assertEquals(user.getEmail(), found.getEmail());

        verify(session).createQuery(anyString(), eq(User.class));
        verify(query).setParameter("email", user.getEmail());
        verify(query).uniqueResult();
    }

    @Test
    void testUpdateUser() {
        UserDTO user = new UserDTO("Oleg", "oleg@mail.ru", 25);
        User existingUser = new User("Igor", "igor@test.com", 30);

        when(session.beginTransaction()).thenReturn(transaction);

        when(session.createQuery(anyString(), eq(User.class))).thenReturn(query);
        when(query.setParameter("email", user.getEmail())).thenReturn(query);
        when(query.uniqueResult()).thenReturn(existingUser);

        userDAO.updateUser(user);

        verify(session).merge(existingUser);
        verify(transaction).commit();

        assertEquals("Oleg", existingUser.getName());
        assertEquals(25, existingUser.getAge());
    }

    @Test
    void testDeleteUser() {
        UserDTO user = new UserDTO("Oleg", "oleg@mail.ru", 25);
        User userEntity = new User();
        userEntity.setEmail(user.getEmail());

        when(session.beginTransaction()).thenReturn(transaction);

        when(session.createQuery(anyString(), eq(User.class))).thenReturn(query);
        when(query.setParameter("email", user.getEmail())).thenReturn(query);
        when(query.uniqueResult()).thenReturn(userEntity);

        userDAO.deleteUser(user);

        verify(session).remove(userEntity);
        verify(transaction).commit();
    }
}

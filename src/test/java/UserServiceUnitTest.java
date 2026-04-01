import com.nightyummy.dao.UserRepository;
import com.nightyummy.dto.UserDTO;
import com.nightyummy.entity.User;
import com.nightyummy.service.UserEventProducer;
import com.nightyummy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserEventProducer userEventProducer;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setup() {

    }

    @Test
    void testSaveUser() {
        User user = new User("Oleg", "oleg@mail.ru", 25);

        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO dto = userService.createUser(new UserDTO(user));
        assertNotNull(dto);
        assertEquals("Oleg", dto.getName());
        assertEquals("oleg@mail.ru", dto.getEmail());
        assertEquals(25, dto.getAge());

        verify(userRepository).save(any(User.class));
    }

    @Test
    void testGetUser() {
        User user = new User("Oleg", "oleg@mail.ru", 25);
        Optional<User> optionalUser = Optional.of(user);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(optionalUser);

        UserDTO found = userService.getUserByEmail(user.getEmail());
        assertNotNull(found);
        assertEquals(new UserDTO(user), found);

        verify(userRepository).findByEmail(any(String.class));
    }

    @Test
    void testUpdateUser() {
        User user = new User("Igor", "igor@mail.com", 30);

        when(userRepository.findByEmail("igor@mail.com")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO newData = new UserDTO("Egor","egor@mail.com" ,35 );
        userService.updateUser("igor@mail.com", newData);

        assertEquals(new UserDTO(user), newData);

        verify(userRepository).save(any(User.class));
    }

    @Test
    void testDeleteUser() {
        User user = new User("Igor", "igor@mail.com", 30);

        when(userRepository.findByEmail("igor@mail.com")).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(any(User.class));

        userService.deleteUser(user.getEmail());

        verify(userRepository).delete(any(User.class));
    }
}

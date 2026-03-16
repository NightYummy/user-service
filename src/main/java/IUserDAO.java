public interface IUserDAO {

    void saveUser(UserDTO user);
    UserDTO getUserByEmail(String email);
    void updateUser(UserDTO user);
    void deleteUser(UserDTO user);
}

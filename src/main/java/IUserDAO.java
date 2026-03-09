public interface IUserDAO {

    void saveUser(User user);
    User getUserByEmail(String email);
    void updateUser(User user);
    void deleteUser(User user);
}

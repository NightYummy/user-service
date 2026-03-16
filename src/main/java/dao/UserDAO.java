package dao;

import dto.UserDTO;

public interface UserDAO {

    void saveUser(UserDTO user);
    UserDTO getUserByEmail(String email);
    void updateUser(UserDTO user);
    void deleteUser(UserDTO user);
}

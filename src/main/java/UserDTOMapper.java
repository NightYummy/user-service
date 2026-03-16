public interface UserDTOMapper {

    UserDTO mapToDTO(User user);
    User mapToUser(UserDTO dto);
}

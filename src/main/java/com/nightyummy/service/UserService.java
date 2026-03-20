package com.nightyummy.service;

import com.nightyummy.dao.UserRepository;
import com.nightyummy.dto.UserDTO;
import com.nightyummy.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserDTO createUser(UserDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail()))
            throw new IllegalArgumentException("Пользователь с таким E-mail уже существует: " + dto.getEmail());

        User user = new User(dto.getName(), dto.getEmail(), dto.getAge());
        return new UserDTO(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("Пользователь с таким E-mail не найден: " + email));
        return new UserDTO(user);
    }

    @Transactional
    public UserDTO updateUser(String email, UserDTO dto) {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("Пользователь с таким E-mail не найден: " + email));

        if (!user.getName().equals(dto.getName()) && !dto.getName().isEmpty())
            user.setName(dto.getName());

        if (!user.getEmail().equals(dto.getEmail()) && !dto.getEmail().isEmpty())
            user.setEmail(dto.getEmail());

        if (user.getAge() != dto.getAge() && dto.getAge() != 0)
            user.setAge(dto.getAge());

        return new UserDTO(userRepository.save(user));
    }

    @Transactional
    public void deleteUser(UserDTO dto) {
        User user = userRepository.findByEmail(dto.getEmail()).orElseThrow(() ->
                new IllegalArgumentException("Пользователь с таким E-mail не найден: " + dto.getEmail()));
        userRepository.delete(user);
    }
}

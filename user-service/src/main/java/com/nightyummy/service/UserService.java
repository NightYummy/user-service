package com.nightyummy.service;

import com.nightyummy.dao.UserRepository;
import com.nightyummy.dto.UserDTO;
import com.nightyummy.dto.UserEvent;
import com.nightyummy.dto.UserEvent.Operation;
import com.nightyummy.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserEventProducer userEventProducer;

    public UserService(UserRepository userRepository, UserEventProducer userEventProducer) {
        this.userRepository = userRepository;
        this.userEventProducer = userEventProducer;
    }

    @Transactional
    public UserDTO createUser(UserDTO dto) throws IllegalArgumentException {
        if (userRepository.existsByEmail(dto.getEmail()))
            throw new IllegalArgumentException("Пользователь с таким E-mail уже существует: " + dto.getEmail());

        User user = new User(dto.getName(), dto.getEmail(), dto.getAge());
        User saved = userRepository.save(user);
        userEventProducer.send(new UserEvent(Operation.CREATE, saved.getEmail()));
        return new UserDTO(saved);
    }

    @Transactional(readOnly = true)
    public UserDTO getUserByEmail(String email) throws IllegalArgumentException {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("Пользователь с таким E-mail не найден: " + email));
        return new UserDTO(user);
    }

    @Transactional
    public UserDTO updateUser(String email, UserDTO dto) throws IllegalArgumentException {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("Пользователь с таким E-mail не найден: " + email));

        if (!user.getName().equals(dto.getName()) && !dto.getName().isEmpty())
            user.setName(dto.getName());

        if (!user.getEmail().equals(dto.getEmail()) && !dto.getEmail().isEmpty()) {
            if (userRepository.existsByEmail(dto.getEmail()))
                throw new IllegalArgumentException("Пользователь с таким E-mail уже существует: " + dto.getEmail());

            user.setEmail(dto.getEmail());
        }

        if (user.getAge() != dto.getAge() && dto.getAge() != 0)
            user.setAge(dto.getAge());

        return new UserDTO(userRepository.save(user));
    }

    @Transactional
    public void deleteUser(String email) throws IllegalArgumentException {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("Пользователь с таким E-mail не найден: " + email));
        userRepository.delete(user);
        userEventProducer.send(new UserEvent(Operation.DELETE, email));
    }
}

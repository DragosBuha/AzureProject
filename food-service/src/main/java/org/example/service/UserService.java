package org.example.service;

import lombok.AllArgsConstructor;
import org.example.entity.User;
import org.example.entity.dto.UserDTO;
import org.example.entity.dto.UserLoginDTO;
import org.example.repository.UserRepository;
import org.example.utils.PasswordUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User addUser(UserDTO userDTO) {
        User existingUser = userRepository.findByUsername(userDTO.getUsername()).orElse(null);

        if (existingUser != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exists!");
        }

        return userRepository.save(User.builder()
                .username(userDTO.getUsername())
                .password(PasswordUtil.encryptPassword(userDTO.getPassword()))
                .fullName(userDTO.getFullName())
                .build());
    }

    public UserLoginDTO checkCredentials(UserDTO userDTO) {
        User user = userRepository
                .findByUsername(userDTO.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));

        if (PasswordUtil.checkPassword(userDTO.getPassword(), user.getPassword())) {
            return UserLoginDTO.builder()
                    .userId(user.getId())
                    .build();
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid credentials!");
        }
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}

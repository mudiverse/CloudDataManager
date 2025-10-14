package com.cloudapi.service;

import com.cloudapi.model.User;
import com.cloudapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final SecureRandom secureRandom = new SecureRandom();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(String name, String email) {
        Optional<User> existing = userRepository.findByEmail(email);
        if (existing.isPresent()) {
            return existing.get();
        }
        String apiKey = generateApiKey();
        User user = User.builder()
                .name(name)
                .email(email)
                .apiKey(apiKey)
                .build();
        return userRepository.save(user);
    }

    public Optional<User> getByApiKey(String apiKey) {
        return userRepository.findByApiKey(apiKey);
    }

    public User loginOrRegister(String email, String fallbackName) {
        return userRepository.findByEmail(email)
                .orElseGet(() -> {
                    String name = (fallbackName != null && !fallbackName.isBlank()) ? fallbackName : "User";
                    String apiKey = generateApiKey();
                    User user = User.builder()
                            .name(name)
                            .email(email)
                            .apiKey(apiKey)
                            .build();
                    return userRepository.save(user);
                });
    }

    private String generateApiKey() {
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}



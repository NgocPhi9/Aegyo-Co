package group1.commerce.service;

import group1.commerce.entity.User;
import group1.commerce.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findUserByIdProvided(String id) {
        return userRepository.findByIdProvided(id);
    }

    public void save(User user) {
        userRepository.save(user);
    }
}

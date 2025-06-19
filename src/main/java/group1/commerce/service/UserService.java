package group1.commerce.service;

import group1.commerce.dto.UserRegisteredDTO;
import group1.commerce.dto.UserWithOrderCount;
import group1.commerce.entity.Role;
import group1.commerce.entity.User;
import group1.commerce.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User findUserByIdProvided(String id) {
        return userRepository.findByIdProvided(id);
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public User getUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public void registerUser(UserRegisteredDTO userRegisteredDTO) {
        if(existsByEmail(userRegisteredDTO.getEmail())) {
            throw new RuntimeException("User with this email already exists");
        };

        User user = new User();
        user.setUserName(userRegisteredDTO.getUserName());
        user.setEmail(userRegisteredDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userRegisteredDTO.getPassword()));
        user.setRole(Role.CUSTOMER);
        save(user);
    }

    public Page<UserWithOrderCount> filterUsers(String keyword, Boolean restricted, String sort, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.searchAndSort(keyword, restricted, sort, pageable);
    }

    public void setRestrict(String id) {
        User user = getUserById(id);
        user.setPurchaseRestricted(!user.isPurchaseRestricted());
        save(user);
    }

}

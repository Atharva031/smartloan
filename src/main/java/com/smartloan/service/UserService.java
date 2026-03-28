package com.smartloan.service;

import com.smartloan.model.User;
import com.smartloan.repository.UserRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;


@Service
@RequiredArgsConstructor

public class UserService 
{
    private final UserRepository userRepository;

    public User registerUser(User user)
    {
        if(userRepository.existsByEmail(user.getEmail()))
        {
            throw new RuntimeException("Email already registered: " + user.getEmail());
        }
        user.setRole(User.Role.APPLICANT);
        return userRepository.save(user);
    }

    public List<User> getAllUsers()
    {
        return userRepository.findAll();
    }

    public User getUserById(Long id)
    {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }
}

package com.example.BookStore.provider;

import com.example.BookStore.dao.RoleRepository;
import com.example.BookStore.dao.UserRepository;
import com.example.BookStore.model.Role;
import com.example.BookStore.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserProvider {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void saveUser(User user) {
        Set<Role> existingRoles = new HashSet<>();

        //Чтобы не создавались уже существующие роли
        for (Role role : user.getRoles()) {
            Optional<Role> existingRole = roleRepository.findByName(role.getName());

            if (existingRole.isPresent()) {
                existingRoles.add(existingRole.get());
            } else {
                existingRoles.add(role);
            }
        }
        user.setRoles(existingRoles);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
}

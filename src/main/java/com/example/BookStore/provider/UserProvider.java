package com.example.BookStore.provider;

import com.example.BookStore.dao.UserRepository;
import com.example.BookStore.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserProvider {
    @Autowired
    private UserRepository repo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        System.out.println(user.getPassword() + " : " + passwordEncoder.encode(user.getPassword()));
        repo.save(user);
    }
}

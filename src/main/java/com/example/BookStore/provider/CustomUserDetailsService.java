package com.example.BookStore.provider;

import com.example.BookStore.config.CustomUserDetails;
import com.example.BookStore.dao.UserRepository;
import com.example.BookStore.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("~~~~~~~~~~~~~~");
        Optional<User> user = repo.findByUsername(username);
        System.out.println(user.get().getUsername() + " " + user.get().getRoles());
        return user.map(CustomUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException(username + " not found"));
    }
}


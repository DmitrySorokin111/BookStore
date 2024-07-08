package com.example.BookStore.provider;

import com.example.BookStore.config.CustomUserDetails;
import com.example.BookStore.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserProvider userProvider;

    @Autowired
    private RoleProvider roleProvider;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userProvider.getByUsername(username); //почему-то user.getRoles() == []
        if (user.isPresent()) {
            user.get().setRoles(roleProvider.getByUserId(user.get().getId())); //поэтому отдельно присвоим роли по id пользователя (костыль)
            return user.map(CustomUserDetails::new).get();
        } else {
            throw new UsernameNotFoundException(username + " not found");
        }
    }
}


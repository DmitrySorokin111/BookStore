package com.example.BookStore.provider;

import com.example.BookStore.dao.RoleRepository;
import com.example.BookStore.model.Book;
import com.example.BookStore.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class RoleProvider {

    @Autowired
    private RoleRepository roleRepository;

    public void saveRole(Role role) {
        roleRepository.save(role);
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Optional<Role> getByName(String name) {
        return roleRepository.findByName(name);
    }

    public Set<Role> getByUserId(Long userId) {
        return roleRepository.findRolesByUserId(userId);
    }
}

package com.example.BookStore.dao;

import com.example.BookStore.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;

public interface CartRepository extends JpaRepository<Cart, Long> {
    @PreAuthorize("hasAuthority('ROLE_USER')")
    Cart findByUserId(Long userId);
}

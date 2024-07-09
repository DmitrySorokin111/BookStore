package com.example.BookStore.dao;

import com.example.BookStore.model.Order;
import com.example.BookStore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Set<Order> findAllByUser(User user);
}

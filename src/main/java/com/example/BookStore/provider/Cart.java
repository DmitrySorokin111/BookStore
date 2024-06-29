package com.example.BookStore.provider;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Cart {
    private Map<Book, Integer> cart = new HashMap<>();

    public void addBook(Book book) {
        cart.put(book, cart.getOrDefault(book, 0) + 1);
    }

    public int getQuantity() {
        return cart.size();
    }

    public int getQuantity(Book book) {
        return cart.getOrDefault(book, 0);
    }

    public void removeBook(Book book) {
        cart.remove(book);
    }

    public void updateBookQuantity(Book book, int quantity) {
        if (quantity <= 0) {
            cart.remove(book);
        } else {
            cart.put(book, quantity);
        }
    }

    public double getTotalPrice() {
        return cart.entrySet().stream()
                .mapToDouble(entry -> entry.getKey().getPrice() * entry.getValue())
                .sum();
    }
}

package com.example.BookStore.provider;

import lombok.Data;

@Data
public class Book {

    private static long seq = 0;

    private long id;
    private String title;
    private String author;
    private String publisher;
    private int year;
    private String genre;
    private String isbn;
    private double price;
    private int pages;
    private String description;
    private int rating;
    private boolean newFlag;
    private int stock;

    public Book() {
        this.id = seq++;
    }
}

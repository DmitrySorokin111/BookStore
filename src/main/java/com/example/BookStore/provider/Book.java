package com.example.BookStore.provider;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Data
public class Book {

    private static long seq = 0;

    private long id;
    @NotEmpty(message = "Обязательно для заполнения")
    private String title;
    @NotEmpty(message = "Обязательно для заполнения")
    private String author;
    @NotEmpty(message = "Обязательно для заполнения")
    private String publisher;
    @NotNull(message = "Обязательно для заполнения")
    private int year;
    private String genre;
    @NotEmpty(message = "Обязательно для заполнения")
    @Pattern(regexp = "\\d{3}-\\d{1,5}-\\d{1,7}-\\d{1,7}-\\d{1,3}", message = "Неверный формат ISBN")
    private String isbn;
    @NotNull(message = "Обязательно для заполнения")
    @Min(value = 0, message = "Цена должна быть положительным числом")
    private double price;
    @NotNull(message = "Обязательно для заполнения")
    private int pages;
    private String description;
    private int rating;
    private boolean newFlag;
    @NotNull(message = "Обязательно для заполнения")
    @Min(value = 0, message = "Кол-во на складе должно быть положительным числом")
    private int stock;
    public String imagePath;

    public Book() {
        this.id = seq++;
    }

    public Book(
            long id, String title, String author, String publisher,
            int year, String isbn, double price, int pages,
            int stock, String imagePath)
    {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.year = year;
        this.isbn = isbn;
        this.price = price;
        this.pages = pages;
        this.stock = stock;
        this.imagePath = imagePath;
    }
}

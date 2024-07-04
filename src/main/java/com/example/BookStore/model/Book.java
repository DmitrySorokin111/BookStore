package com.example.BookStore.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "books")
@Data
public class Book {

    private static long seq = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotEmpty(message = "Обязательно для заполнения")
    private String title;
    @NotEmpty(message = "Обязательно для заполнения")
    private String publisher;
    @NotNull(message = "Обязательно для заполнения")
    private int publicationYear;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Author author;

    public Book() {}
}

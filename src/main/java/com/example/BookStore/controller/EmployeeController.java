package com.example.BookStore.controller;

import com.example.BookStore.model.Author;
import com.example.BookStore.model.Book;
import com.example.BookStore.provider.AuthorProvider;
import com.example.BookStore.provider.BookProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class EmployeeController {

    @Autowired
    private AuthorProvider authorProvider;

    @Autowired
    private BookProvider bookProvider;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/storeassistance/add-book")
    public String addBook(@RequestBody Book book) {
        Author author = authorProvider.getAuthorById(book.getAuthor().getId());
        book.setAuthor(author);
        bookProvider.saveBook(book);
        return "redirect:/booklist";
    }
}

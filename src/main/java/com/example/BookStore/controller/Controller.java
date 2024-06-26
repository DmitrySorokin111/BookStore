package com.example.BookStore.controller;

import com.example.BookStore.provider.Book;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Controller
public class Controller {

    private static List<Book> books = new ArrayList<>();

    @GetMapping("/booklist")
    public String getBookList(Model model) {
        model.addAttribute("books", books);
        return "booklist";
    }

    @GetMapping("/storeassistance/add-book")
    public String getAddBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "add-book";
    }

    @PostMapping("/storeassistance/add-book")
    public String addBook(Book book) {
        books.add(book);
        return "redirect:/booklist";
    }
}

package com.example.BookStore.controller;

import com.example.BookStore.provider.Book;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@org.springframework.stereotype.Controller
public class Controller {

    private static Map<Long, Book> books = new HashMap<>();

    @GetMapping("/booklist")
    public String getBookList(Model model) {
        model.addAttribute("books", books);
        return "booklist";
    }

    @GetMapping("/book")
    public String getBook(@RequestParam("id") long bookId, Model model) {
        Book book = books.get(bookId);
        model.addAttribute("book", book);
//        System.out.println(books.get(bookId).getImagePath().substring(1));
        return "book-details";
    }

    @GetMapping("/storeassistance/add-book")
    public String getAddBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "add-book";
    }

    @PostMapping("/storeassistance/add-book")
    public String addBook(@ModelAttribute Book book) {
        System.out.println(book.getImagePath());
        books.put(book.getId(), book);
        return "redirect:/booklist";
    }
}

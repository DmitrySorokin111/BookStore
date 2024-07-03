package com.example.BookStore.provider;

import com.example.BookStore.dao.BookDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookProvider {

    @Autowired
    private BookDAO bookDAO;

    public void addBook(Book book) {
        bookDAO.addBook(book);
    }

    public List<Book> getAllBooks() {
        return bookDAO.getAllBooks();
    }

    public Optional<Book> getBook(long id) {
        return bookDAO.getBook(id);
    }
}

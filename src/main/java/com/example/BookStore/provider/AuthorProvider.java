package com.example.BookStore.provider;

import com.example.BookStore.dao.AuthorRepository;
import com.example.BookStore.model.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorProvider {

    @Autowired
    private AuthorRepository authorRepository;

    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    public Author getAuthorById(Long id) {
        return authorRepository.findById(id).orElse(null);
    }

    public void saveAuthor(Author author) {
        authorRepository.save(author);
    }
}

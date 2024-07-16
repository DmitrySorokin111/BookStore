package com.example.BookStore;

import com.example.BookStore.dao.AuthorRepository;
import com.example.BookStore.dao.BookRepository;
import com.example.BookStore.model.Author;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @BeforeEach
    public void setup() {
        bookRepository.deleteAll();
        authorRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testAddBookSuccess() throws Exception {
        Author author = new Author();
        author.setName("Test author");
        authorRepository.save(author);

        String bookJson = "{ \"title\": \"Test Book\", \"author\": { \"id\": " + author.getId() + " }, \"publisher\": \"Test Publisher\", \"publicationYear\": 2022, \"genre\": \"Fiction\", \"isbn\": \"978-1-4447-0786-1\", \"price\": 10.99, \"pages\": 100, \"rating\": 7, \"newFlag\": true, \"stock\": 10, \"imagePath\": \"images/3.jpg\" }";

        mockMvc.perform(MockMvcRequestBuilders
                    .post("/storeassistance/add-book")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(bookJson))
                .andExpect(status().isOk());
    }
}

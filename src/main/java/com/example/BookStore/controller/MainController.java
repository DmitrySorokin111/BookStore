package com.example.BookStore.controller;

import com.example.BookStore.model.Book;
import com.example.BookStore.model.Cart;
import com.example.BookStore.model.CartItem;
import com.example.BookStore.model.User;
import com.example.BookStore.provider.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class MainController {
    @Autowired
    private BookProvider bookProvider;

    @Autowired
    private AuthorProvider authorProvider;

    @Autowired
    private CartProvider cartProvider;

    @Autowired
    private UserProvider userProvider ;

    @GetMapping("/welcome")
    public String welcome() {
        return "welcome";
    }

//    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/booklist")
    public String getBookList(Model model) {
//        System.out.println(SecurityContextHolder.getContext().getAuthentication().getR());
        model.addAttribute("books", bookProvider.getAllBooks());
        return "booklist";
    }

//    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/book")
    public String getBook(@RequestParam("id") long bookId, Model model) {
        Optional<Book> book = bookProvider.getBook(bookId);

        if (book.isPresent()) {
            model.addAttribute("book", book.get());

            Optional<User> usr = userProvider.getCurrentUser();
            if (usr.isPresent()) {
                Cart cart = cartProvider.getCartByUserId(usr.get().getId());
                CartItem cartItem = cart.getCartItems().stream()
                        .filter(item -> item.getBook().getId() == bookId)
                        .findFirst()
                        .orElse(null);
                if (cartItem != null) {
                    model.addAttribute("quantity", cartItem.getQuantity());
                } else {
                    model.addAttribute("quantity", 0);
                }
            } else {
                model.addAttribute("quantity", 0);
            }
            return "book-details";
        }
        return "redirect:/booklist";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/storeassistance/add-book")
    public String getAddBookForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("authors", authorProvider.getAllAuthors());
        return "add-book";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/storeassistance/add-book")
    public String addBook(@Valid @ModelAttribute Book book, BindingResult br, Model model) {
        if (br.hasErrors()) {
            model.addAttribute("authors", authorProvider.getAllAuthors());
            return "add-book";
        }
        bookProvider.saveBook(book);
        return "redirect:/booklist";
    }
}

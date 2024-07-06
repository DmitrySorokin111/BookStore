package com.example.BookStore.controller;

import com.example.BookStore.model.Author;
import com.example.BookStore.model.Book;
import com.example.BookStore.model.User;
import com.example.BookStore.provider.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@org.springframework.stereotype.Controller
@SessionAttributes("cart")
public class Controller {
    @Autowired
    private BookProvider bookProvider;

    @Autowired
    private AuthorProvider authorProvider;

    @Autowired
    UserProvider userProvider;

    @ModelAttribute("cart")
    public Cart cart() {
        return new Cart();
    }

    @GetMapping("/welcome")
    public String welcome() {
        return "welcome";
    }

    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    @GetMapping("/booklist")
    public String getBookList(Model model) {
        model.addAttribute("books", bookProvider.getAllBooks());
        return "booklist";
    }

    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    @GetMapping("/book")
    public String getBook(@RequestParam("id") long bookId, Model model, @ModelAttribute("cart") Cart cart) {
        Optional<Book> book = bookProvider.getBook(bookId);
        if (book.isPresent()) {
            model.addAttribute("book", book.get());
            model.addAttribute("quantity", cart.getQuantity(book.get()));
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

    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    @GetMapping("/cart")
    public String viewCart(Model model, @ModelAttribute("cart") Cart cart) {
        model.addAttribute("cartItems", cart.getCart());
        model.addAttribute("totalPrice", cart.getTotalPrice());
        return "cart";
    }

    @PostMapping("/storeassistance/add-book")
    public String addBook(@Valid @ModelAttribute Book book, BindingResult br, Model model) {
        if (br.hasErrors()) {
            model.addAttribute("authors", authorProvider.getAllAuthors());
            return "add-book";
        }
//        Author author = authorProvider.getAuthorById(book.getAuthor().getId());
//        System.out.print(author.getBooks());
        bookProvider.saveBook(book);
        return "redirect:/booklist";
    }

    @PostMapping("/add-to-cart")
    public String addToCart(@RequestParam("id") Long id, @ModelAttribute("cart") Cart cart) {
        Optional<Book> book = bookProvider.getBook(id);
        cart.addBook(book.get());
        return "redirect:/book?id=" + id;
    }

    @PostMapping("/update-cart")
    public String updateCart(@RequestParam("bookId") Long bookId, @RequestParam("action") String action, @ModelAttribute("cart") Cart cart) {
        Optional<Book> book = bookProvider.getBook(bookId);
        int currentQuantity = cart.getQuantity(book.get());
        if ("increase".equals(action)) {
            cart.addBook(book.get());
        } else if ("decrease".equals(action) && currentQuantity > 0) {
            cart.updateBookQuantity(book.get(), currentQuantity - 1);
        }
        return "redirect:/cart";
    }

    @PostMapping("/remove-from-cart")
    public String removeFromCart(@RequestParam("bookId") Long bookId, @ModelAttribute("cart") Cart cart) {
        Optional<Book> book = bookProvider.getBook(bookId);
        cart.removeBook(book.get());
        return "redirect:/cart";
    }

    @PostMapping("/add-user")
    public String addUser(@RequestBody User user) {
        userProvider.addUser(user);
        return "User created";
    }
}

package com.example.BookStore.controller;

import com.example.BookStore.provider.Book;
import com.example.BookStore.provider.BookProvider;
import com.example.BookStore.provider.Cart;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@org.springframework.stereotype.Controller
@SessionAttributes("cart")
public class Controller {
    @Autowired
    private BookProvider bookProvider;

    @ModelAttribute("cart")
    public Cart cart() {
        return new Cart();
    }

    @GetMapping("/booklist")
    public String getBookList(Model model) {
        model.addAttribute("books", bookProvider.getAllBooks());
        return "booklist";
    }

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

    @GetMapping("/storeassistance/add-book")
    public String getAddBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "add-book";
    }

    @GetMapping("/cart")
    public String viewCart(Model model, @ModelAttribute("cart") Cart cart) {
        model.addAttribute("cartItems", cart.getCart());
        model.addAttribute("totalPrice", cart.getTotalPrice());
        return "cart";
    }

    @PostMapping("/storeassistance/add-book")
    public String addBook(@Valid @ModelAttribute Book book, BindingResult br) {
        if (br.hasErrors()) {
            return "add-book";
        }
        bookProvider.addBook(book);
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
}

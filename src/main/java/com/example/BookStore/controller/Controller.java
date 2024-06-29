package com.example.BookStore.controller;

import com.example.BookStore.provider.Book;
import com.example.BookStore.provider.Cart;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@org.springframework.stereotype.Controller
@SessionAttributes("cart")
public class Controller {
    private static Map<Long, Book> books = new HashMap<>();

    @ModelAttribute("cart")
    public Cart cart() {
        return new Cart();
    }

    @GetMapping("/booklist")
    public String getBookList(Model model) {
        model.addAttribute("books", books);
        return "booklist";
    }

    @GetMapping("/book")
    public String getBook(@RequestParam("id") long bookId, Model model, @ModelAttribute("cart") Cart cart) {
        Book book = books.get(bookId);
        model.addAttribute("book", book);
        model.addAttribute("quantity", cart.getQuantity(book));
        return "book-details";
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
        books.put(book.getId(), book);
        return "redirect:/booklist";
    }

    @PostMapping("/add-to-cart")
    public String addToCart(@RequestParam("id") Long id, @ModelAttribute("cart") Cart cart) {
        Book book = books.get(id);
        cart.addBook(book);
        return "redirect:/book?id=" + id;
    }

    @PostMapping("/update-cart")
    public String updateCart(@RequestParam("bookId") Long bookId, @RequestParam("action") String action, @ModelAttribute("cart") Cart cart) {
        Book book = books.get(bookId);
        int currentQuantity = cart.getQuantity(book);
        if ("increase".equals(action)) {
            cart.addBook(book);
        } else if ("decrease".equals(action) && currentQuantity > 0) {
            cart.updateBookQuantity(book, currentQuantity - 1);
        }
        return "redirect:/cart";
    }

    @PostMapping("/remove-from-cart")
    public String removeFromCart(@RequestParam("bookId") Long bookId, @ModelAttribute("cart") Cart cart) {
        Book book = books.get(bookId);
        cart.removeBook(book);
        return "redirect:/cart";
    }
}

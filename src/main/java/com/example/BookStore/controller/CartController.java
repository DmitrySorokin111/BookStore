package com.example.BookStore.controller;

import com.example.BookStore.model.Address;
import com.example.BookStore.model.Cart;
import com.example.BookStore.model.CartItem;
import com.example.BookStore.model.User;
import com.example.BookStore.provider.BookProvider;
import com.example.BookStore.provider.CartProvider;
import com.example.BookStore.provider.OrderProvider;
import com.example.BookStore.provider.UserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class CartController {

    @Autowired
    private CartProvider cartProvider;

    @Autowired
    private BookProvider bookProvider;

    @Autowired
    private UserProvider userProvider;

    @Autowired
    private OrderProvider orderProvider;

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/cart")
    public String viewCart(Model model) {
        Optional<User> user = userProvider.getCurrentUser();
        if (user.isPresent()) {
            Cart cart = cartProvider.getCartByUserId(user.get().getId());
            double totalPrice = cartProvider.getTotalPrice(cart);
            model.addAttribute("totalPrice", totalPrice);
            model.addAttribute("cart", cart);
            model.addAttribute("addresses", orderProvider.getAllAddresses());
        } else {
            throw new RuntimeException("user not found");
        }
        return "cart";
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("/cart/add")
    public String addToCart(@RequestParam Long bookId, Model model) {
        Optional<User> user = userProvider.getCurrentUser();
        if (user.isPresent()) {
            cartProvider.addToCart(user.get().getId(), bookId);
        } else {
            return "redirect:/login";
        }
        return "redirect:/book?id="+bookId;
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("/cart/remove")
    public String removeFromCart(@RequestParam Long cartItemId) {
        cartProvider.removeFromCart(cartItemId);
        return "redirect:/cart";
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("/cart/update")
    public String updateCart(@RequestParam("cartItemId") long cartItemId, @RequestParam("action") String action) {
        Optional<User> user = userProvider.getCurrentUser();

        if (user.isEmpty()) {
            throw new RuntimeException("user not found");
        }

        Optional<CartItem> cartItem = cartProvider.getCartItemById(cartItemId);

        if (cartItem.isEmpty()) {
            throw new RuntimeException("item not found");
        }

        if ("increase".equals(action)) {
            cartProvider.addToCart(user.get().getId(), cartItem.get().getBook().getId());
        } else if ("decrease".equals(action) && cartItem.get().getQuantity() > 0) {
            cartProvider.decreaseQuantity(cartItem.get());
        }

        return "redirect:/cart";
    }
}

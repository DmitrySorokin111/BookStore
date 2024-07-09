package com.example.BookStore.provider;

import com.example.BookStore.dao.BookRepository;
import com.example.BookStore.dao.CartItemRepository;
import com.example.BookStore.dao.CartRepository;
import com.example.BookStore.dao.UserRepository;
import com.example.BookStore.model.Book;
import com.example.BookStore.model.Cart;
import com.example.BookStore.model.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CartProvider {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    public Cart getCartByUserId(Long id) {
        return cartRepository.findByUserId(id);
    }

    public Optional<CartItem> getCartItemById(Long id) {
        return cartItemRepository.findById(id);
    }

    @Transactional
    public Cart addToCart(Long userId, Long bookId) {
        Cart cart = cartRepository.findByUserId(userId);

        Optional<Book> book = bookRepository.findById(bookId);

        if (book.isPresent()) {
            if (book.get().getStock() == 0) {
                throw new IllegalArgumentException("Not enough books in stock");
            }

            Book b = book.get();

            Optional<CartItem> existingCartItem = cartItemRepository.findByCartAndBook(cart, b);

            if (existingCartItem.isPresent()) {
                if (existingCartItem.get().getQuantity() + 1 > b.getStock()) {
                    return cart;
                }
                existingCartItem.get().setQuantity(existingCartItem.get().getQuantity() + 1);
            } else {
                CartItem cartItem = new CartItem();
                cartItem.setCart(cart);
                cartItem.setBook(b);
                cartItem.setQuantity(1);
                cart.getCartItems().add(cartItem);
                cartItemRepository.save(cartItem);
            }
            cartRepository.save(cart);
        }
        return cart;
    }

    public void removeFromCart(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    public void decreaseQuantity(CartItem cartItem) {
        if (cartItem.getQuantity()-1 == 0) {
            cartItemRepository.deleteById(cartItem.getId());
            return;
        }
        cartItem.setQuantity(cartItem.getQuantity() - 1);
        cartItemRepository.save(cartItem);
    }

    public double getTotalPrice(Cart cart) {
        return cart.getCartItems().stream()
                .mapToDouble(item -> item.getBook().getPrice() * item.getQuantity())
                .sum();
    }

    public void clearCart(Cart cart) {
        cartItemRepository.deleteAllByCart(cart);
    }
}

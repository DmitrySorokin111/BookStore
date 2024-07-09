package com.example.BookStore.provider;

import com.example.BookStore.dao.*;
import com.example.BookStore.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class OrderProvider {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private OrderStatusRepository orderStatusRepository;

    @Autowired
    private CartProvider cartProvider;

    @Transactional
    public Order createOrder(User user, long addressId) {
        Cart cart = cartProvider.getCartByUserId(user.getId());

        Address address = addressRepository.findById(addressId).orElse(null);
        OrderStatus status = orderStatusRepository.findById(1L).orElseThrow(() -> new IllegalArgumentException("Pending status not found"));

        Order order = new Order();
        order.setUser(user);
        order.setAddress(address);
        order.setStatus(status);
        order.setCreatedDate(new Date());
        orderRepository.save(order);

        for (CartItem cartItem : cart.getCartItems()) {
            Book book = cartItem.getBook();
            if (book.getStock() < cartItem.getQuantity()) {
                throw new IllegalArgumentException("Not enough books in stock");
            }

            book.setStock(book.getStock()-cartItem.getQuantity());
            bookRepository.save(book);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setBook(cartItem.getBook());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPriceAtOrder(cartItem.getBook().getPrice());
            orderItemRepository.save(orderItem);
        }

        cartProvider.clearCart(cart);

        return order;
    }

    public Set<Order> getOrdersByUser(User user) {
        return orderRepository.findAllByUser(user);
    }

    public Order getOrderById(long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public List<Address> getAllAddresses() {
        return addressRepository.findAll();
    }

    public void updateOrderStatus(long orderId, long statusId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Invalid order ID"));
        OrderStatus status = orderStatusRepository.findById(statusId).orElseThrow(() -> new IllegalArgumentException("Invalid status ID"));
        order.setStatus(status);
        orderRepository.save(order);
    }
}

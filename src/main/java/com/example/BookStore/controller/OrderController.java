package com.example.BookStore.controller;

import com.example.BookStore.model.Order;
import com.example.BookStore.model.User;
import com.example.BookStore.provider.OrderProvider;
import com.example.BookStore.provider.UserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;
import java.util.Set;

@Controller
public class OrderController {

    @Autowired
    private OrderProvider orderProvider;

    @Autowired
    private UserProvider userProvider;

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/my-orders")
    public String viewOrders(Model model) {
        Optional<User> user = userProvider.getCurrentUser();
        if (user.isPresent()) {
            Set<Order> orders = orderProvider.getOrdersByUser(user.get());
            model.addAttribute("orders", orders);
            return "my-orders";
        } else {
            return "redirect:/login";
        }
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/order-details")
    public String viewOrderDetails(@RequestParam Long id, Model model) {
        Order order = orderProvider.getOrderById(id);
        double cost = orderProvider.getTotalCost(order);
        model.addAttribute("order", order);
        model.addAttribute("totalCost", cost);
        return "order-details";
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("/place-order")
    public String placeOrder(@RequestParam long addressId, Model model, RedirectAttributes redirectAttributes) {
        Optional<User> user = userProvider.getCurrentUser();
        if (user.isPresent()) {
            if (!orderProvider.createOrder(user.get(), addressId)) {
                redirectAttributes.addFlashAttribute("errorMessage", "Заказ невозможен");
                return "redirect:/cart";
            }
            model.addAttribute("orderSuccess", true);
            return "order-success";
        } else {
            return "redirect:/login";
        }
    }
}

package com.example.BookStore.controller;

import com.example.BookStore.model.Cart;
import com.example.BookStore.model.User;
import com.example.BookStore.provider.CartProvider;
import com.example.BookStore.provider.RoleProvider;
import com.example.BookStore.provider.UserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {
    @Autowired
    private UserProvider userProvider;

    @Autowired
    private RoleProvider roleProvider;

    @Autowired
    private CartProvider cartProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showRegistrationPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                                          @RequestParam String password,
                                          RedirectAttributes redirectAttributes) {
        if (userProvider.existsByUsername(username)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Пользователь с таким именем уже существует");
            return "redirect:/register";
        }
        userProvider.saveUserWithRoleUser(username, password);

        redirectAttributes.addFlashAttribute("successMessage", "Пользователь создан успешно");
        return "redirect:/login";
    }

    //Для добаввления через postman`а
    @PostMapping("/register0")
    @ResponseBody
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        if(userProvider.existsByUsername(user.getUsername())) {
            return ResponseEntity.badRequest().body("Username is already taken");
        }

        userProvider.saveUser(user);

        return ResponseEntity.ok("User registered successfully");
    }
}

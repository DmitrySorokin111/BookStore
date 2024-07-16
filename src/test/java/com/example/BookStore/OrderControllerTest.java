package com.example.BookStore;

import com.example.BookStore.dao.RoleRepository;
import com.example.BookStore.dao.UserRepository;
import com.example.BookStore.model.*;
import com.example.BookStore.provider.BookProvider;
import com.example.BookStore.provider.OrderProvider;
import com.example.BookStore.provider.UserProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashSet;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @MockBean
    private OrderProvider orderProvider;

    @MockBean
    private UserProvider userProvider;

    @MockBean
    private BookProvider bookProvider;

    @Test
    @WithMockUser(username = "user", roles = {"USER", "ADMIN"})
    public void testCreateOrderSuccess() throws Exception {
        User user = new User();
        user.setPassword("111");
        user.setId(1L);
        user.setUsername("user");
        user.setRoles(new HashSet<>(roleRepository.findAll()));

        Mockito.when(userProvider.getCurrentUser()).thenReturn(Optional.of(user));
        Mockito.when(orderProvider.createOrder(Mockito.any(User.class), Mockito.eq(1L))).thenReturn(true);

        MvcResult result = mockMvc.perform(post("/place-order")
                        .param("addressId", "1"))
                .andExpect(status().isOk())
                .andReturn();

        System.out.println("Response: " + result.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER", "ADMIN"})
    public void testCreateOrderFailureCreation() throws Exception {
        User user = new User();
        user.setPassword("111");
        user.setId(1L);
        user.setUsername("user");
        user.setRoles(new HashSet<>(roleRepository.findAll()));

        Mockito.when(userProvider.getCurrentUser()).thenReturn(Optional.of(user));
        Mockito.when(orderProvider.createOrder(Mockito.any(User.class), Mockito.eq(1L))).thenReturn(false);

        MvcResult result = mockMvc.perform(post("/place-order")
                        .param("addressId", "1"))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "/order-details"))
                .andReturn();

        System.out.println("Response: " + result.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER", "ADMIN"})
    public void testCreateOrderFailureLogin() throws Exception {
        Mockito.when(orderProvider.createOrder(Mockito.any(User.class), Mockito.eq(1L))).thenReturn(false);

        MvcResult result = mockMvc.perform(post("/place-order")
                        .param("addressId", "1"))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "/login"))
                .andReturn();

        System.out.println("Response: " + result.getResponse().getContentAsString());
    }
}

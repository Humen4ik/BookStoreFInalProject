package com.epam.rd.autocode.spring.project.service;

import com.epam.rd.autocode.spring.project.dto.CartDTO;
import com.epam.rd.autocode.spring.project.dto.OrderDTO;

public interface CartService {
    void addBook(String email, String name, Integer quantity);

    CartDTO getCartByClient(String username);
    OrderDTO createOrder(String clientEmail);

    void removeBookFromCart(String username, String bookName);
}

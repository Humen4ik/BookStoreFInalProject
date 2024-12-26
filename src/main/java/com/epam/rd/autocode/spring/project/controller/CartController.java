package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.dto.CartDTO;
import com.epam.rd.autocode.spring.project.dto.OrderDTO;
import com.epam.rd.autocode.spring.project.service.CartService;
import com.epam.rd.autocode.spring.project.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/cart")
public class CartController {

    private CartService cartService;
    private OrderService orderService;

    public CartController(CartService cartService, OrderService orderService) {
        this.cartService = cartService;
        this.orderService = orderService;
    }

    @GetMapping
    public String showCart(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        CartDTO cartDTO = cartService.getCartByClient(userDetails.getUsername());
        model.addAttribute("cart", cartDTO);
        return "cart";
    }

    @PostMapping("/{name}/add")
    public String addBookItem(@AuthenticationPrincipal UserDetails userDetails, @PathVariable String name, @ModelAttribute(name = "quantity") Integer quantity) {
        cartService.addBook(userDetails.getUsername(), name, quantity);
        return "redirect:/books/all";
    }

    @PostMapping("/confirm")
    public ResponseEntity<OrderDTO> confirm(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(cartService.createOrder(userDetails.getUsername()));
    }

    @PostMapping("/delete")
    public ResponseEntity<Void> deleteBookFromCart(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Map<String, String> request) {
        String bookName = request.get("bookName");
        cartService.removeBookFromCart(userDetails.getUsername(), bookName);
        return ResponseEntity.ok().build();
    }

}

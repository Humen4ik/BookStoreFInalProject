package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.dto.OrderDTO;
import com.epam.rd.autocode.spring.project.service.BookService;
import com.epam.rd.autocode.spring.project.service.ClientService;
import com.epam.rd.autocode.spring.project.service.OrderService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/order")
public class OrderController {

    private BookService bookService;
    private OrderService orderService;
    private ClientService clientService;

    public OrderController(BookService bookService, OrderService orderService, ClientService clientService) {
        this.bookService = bookService;
        this.orderService = orderService;
        this.clientService = clientService;
    }

    @GetMapping("/{email}")
    public String getOrders(@PathVariable(name = "email") String email, Model model) {
        List<OrderDTO> orders = orderService.getOrderByClient(email);
        model.addAttribute("orders", orders); // Add the list of orders to the model
        model.addAttribute("bookItems", orders.stream().flatMap(order -> order.getBookItems().stream()).collect(Collectors.toList()));
        return "orders"; // Return the view name
    }

    @PostMapping("/{name}/add")
    public String addBook(@AuthenticationPrincipal UserDetails userDetails, @PathVariable String name, @ModelAttribute(name = "quantity") Integer quantity) {
        orderService.addOrder(userDetails.getUsername(), name, quantity);
        return "redirect:/books/all";
    }

    @GetMapping("/{email}/confirm")
    public String confirmOrder(@AuthenticationPrincipal UserDetails userDetails, @PathVariable String email) {
        orderService.confirmOrder(userDetails.getUsername(), email);
        return "redirect:/order/" + email;
    }

}

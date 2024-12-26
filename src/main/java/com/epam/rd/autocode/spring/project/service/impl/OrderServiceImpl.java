package com.epam.rd.autocode.spring.project.service.impl;

import com.epam.rd.autocode.spring.project.dto.OrderDTO;
import com.epam.rd.autocode.spring.project.model.*;
import com.epam.rd.autocode.spring.project.repo.*;
import com.epam.rd.autocode.spring.project.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;
    private ClientRepository clientRepository;
    private BookRepository bookRepository;
    private EmployeeRepository employeeRepository;
    private ModelMapper mapper;

    public OrderServiceImpl(OrderRepository orderRepository, ClientRepository clientRepository, BookRepository bookRepository, EmployeeRepository employeeRepository, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.bookRepository = bookRepository;
        this.clientRepository = clientRepository;
        this.employeeRepository = employeeRepository;
        this.mapper = modelMapper;
    }

    @Override
    public List<OrderDTO> getOrderByClient(String clientEmail) {
        Client client = clientRepository.getClientByEmail(clientEmail).orElseThrow(() -> new RuntimeException("Client not found"));
        List<Order> orders = orderRepository.findByClient(client);

        return orders.stream()
                .map(order -> mapper.map(order, OrderDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> getOrdersByEmployee(String employeeEmail) {
        return null;
    }

    @Override
    public void confirmOrder(String employeeEmail, String clientEmail) {
        Client client = clientRepository.getClientByEmail(clientEmail).orElseThrow(() -> new RuntimeException("Client not found"));
        Employee employee = employeeRepository.findByEmail(employeeEmail).orElseThrow(() -> new RuntimeException("Employee not found"));
        List<Order> orders = orderRepository.findByClient(client);
        orders.stream().forEach(order -> {
            order.setOrderDate(LocalDateTime.now());
            order.setEmployee(employee);
        });
        orderRepository.saveAll(orders);
    }

}

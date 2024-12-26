package com.epam.rd.autocode.spring.project.service;

import com.epam.rd.autocode.spring.project.dto.BookItemDTO;
import com.epam.rd.autocode.spring.project.dto.OrderDTO;
import com.epam.rd.autocode.spring.project.model.Client;

import java.util.*;

public interface OrderService {

    List<OrderDTO> getOrderByClient(String clientEmail);

    List<OrderDTO> getOrdersByEmployee(String employeeEmail);

    void confirmOrder(String employeeEmail, String clientEmail);
}

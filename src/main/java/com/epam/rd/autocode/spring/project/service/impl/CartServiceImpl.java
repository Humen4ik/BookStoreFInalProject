package com.epam.rd.autocode.spring.project.service.impl;

import com.epam.rd.autocode.spring.project.dto.CartDTO;
import com.epam.rd.autocode.spring.project.dto.OrderDTO;
import com.epam.rd.autocode.spring.project.model.BookItem;
import com.epam.rd.autocode.spring.project.model.Cart;
import com.epam.rd.autocode.spring.project.model.Client;
import com.epam.rd.autocode.spring.project.model.Order;
import com.epam.rd.autocode.spring.project.repo.BookRepository;
import com.epam.rd.autocode.spring.project.repo.CartRepository;
import com.epam.rd.autocode.spring.project.repo.ClientRepository;
import com.epam.rd.autocode.spring.project.repo.OrderRepository;
import com.epam.rd.autocode.spring.project.service.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    private ClientRepository clientRepository;
    private CartRepository cartRepository;
    private BookRepository bookRepository;
    private OrderRepository orderRepository;
    private ModelMapper mapper;

    public CartServiceImpl(
            ClientRepository clientRepository,
            CartRepository cartRepository,
            BookRepository bookRepository,
            OrderRepository orderRepository,
            ModelMapper mapper
    ) {
        this.clientRepository = clientRepository;
        this.cartRepository = cartRepository;
        this.bookRepository = bookRepository;
        this.orderRepository = orderRepository;
        this.mapper = mapper;
    }

    @Override
    public void addBook(String email, String name, Integer quantity) {
        Client client = clientRepository.getClientByEmail(email).get();
        Cart cart = cartRepository.findByClient(client);
        if (cart == null) {
            cart = new Cart();
            cart.setClient(client);
            cart.setBookItems(new ArrayList<>());
        }

        BookItem bookItem = new BookItem();
        bookItem.setBook(bookRepository.findByName(name).get());
        bookItem.setQuantity(quantity);

        cart.getBookItems().add(bookItem);
        cartRepository.save(cart);
    }

    @Override
    public CartDTO getCartByClient(String username) {
        Client client = clientRepository.getClientByEmail(username).get();
        Cart cart = cartRepository.findByClient(client);
        return mapper.map(cart, CartDTO.class);
    }

    @Override
    public OrderDTO createOrder(String clientEmail) {
        // Copy BookItems from Cart to Order and clear the Cart's BookItems to avoid shared references
        Client client = clientRepository.getClientByEmail(clientEmail).get();
        Cart cart = cartRepository.findByClient(client);

        List<BookItem> bookItemsFromCart = cart.getBookItems().stream()
                .map(bookItem -> {
                    BookItem newBookItem = new BookItem();
                    newBookItem.setBook(bookItem.getBook());
                    newBookItem.setQuantity(bookItem.getQuantity());
                    return newBookItem;
                })
                .collect(Collectors.toList());

        // Detach the BookItems from the Cart to avoid shared reference issues
        cart.getBookItems().clear();

        // Create and save the Order
        Order order = new Order();
        order.setClient(client);
        order.setBookItems(bookItemsFromCart);
        bookItemsFromCart.forEach(bookItem -> bookItem.setOrder(order));

        // Calculate total price
        BigDecimal totalPrice = bookItemsFromCart.stream()
                .map(bookItem -> bookItem.getBook().getPrice().multiply(BigDecimal.valueOf(bookItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setPrice(totalPrice);

        // Save the Order and the new BookItems
        orderRepository.save(order);

        return mapper.map(order, OrderDTO.class);
    }

    @Override
    public void removeBookFromCart(String username, String bookName) {
        Cart cart = cartRepository.findByClient(clientRepository.getClientByEmail(username).get());
        cart.getBookItems().removeIf(bookItem -> bookItem.getBook().getName().equals(bookName));
        cartRepository.save(cart);
    }
}

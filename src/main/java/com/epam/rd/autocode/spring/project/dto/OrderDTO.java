package com.epam.rd.autocode.spring.project.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Email;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO{
    @NotEmpty(message = "Client email must not be empty")
    @Email(message = "Client email must be valid")
    private String clientEmail;

    @NotEmpty(message = "Employee email must not be empty")
    @Email(message = "Employee email must be valid")
    private String employeeEmail;

    @NotNull(message = "Order date must not be null")
    @PastOrPresent(message = "Order date must not be in the future")
    private LocalDateTime orderDate;

    @NotNull(message = "Price must not be null")
    private BigDecimal price;

    @NotNull(message = "Book items must not be null")
    private List<BookItemDTO> bookItems;
}

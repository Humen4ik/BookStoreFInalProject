package com.epam.rd.autocode.spring.project.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Email;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {
    @NotEmpty(message = "Client email must not be empty")
    @Email(message = "Client email must be valid")
    private String clientEmail;

    @NotNull(message = "Book items must not be null")
    private List<BookItemDTO> bookItems;
}

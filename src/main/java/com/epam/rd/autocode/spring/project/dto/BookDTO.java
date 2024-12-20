package com.epam.rd.autocode.spring.project.dto;

import com.epam.rd.autocode.spring.project.model.enums.AgeGroup;
import com.epam.rd.autocode.spring.project.model.enums.Language;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {

    @NotEmpty(message = "Book's name should not be empty")
    private String name;

    @NotEmpty(message = "Book's genre should not be empty")
    private String genre;

    @NotNull(message = "Book's age group should not be null")
    private AgeGroup ageGroup;

    @NotNull(message = "Book's price should not be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Book's price must be greater than 0")
    private BigDecimal price;

    @PastOrPresent(message = "Publication date must be in the past or today")
    private LocalDate publicationDate;

    @NotEmpty(message = "Author's name should not be empty")
    private String author;

    @NotNull(message = "Number of pages should not be null")
    @Min(value = 1, message = "Book must have at least one page")
    private Integer pages;

    private String characteristics;

    private String description;

    @NotNull(message = "Book's language should not be null")
    private Language language;

}

package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.dto.BookDTO;
import com.epam.rd.autocode.spring.project.service.BookService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/books")
public class BookController {

    private BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/all")
    public String getAllBooks(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "books";
    }

    @GetMapping("/search")
    public String findBooks(@RequestParam("query") String query, Model model) {
        List<BookDTO> books = bookService.findBooks(query);
        model.addAttribute("books", books);
        return "books";
    }

    @GetMapping
    public String getBook(
            Model model,
            @RequestParam("name") String name)
    {
        model.addAttribute("books", List.of(bookService.getBookByName(name)));
        return "books";
    }

//    @GetMapping("/edit")
//    public String editBook(@ModelAttribute(name = "bookName") String name, Model model) {
//        BookDTO bookDTO = bookService.getBookByName(name);
//        model.addAttribute("book", bookDTO);
//        return "book-edit";
//    }

    @GetMapping("/{name}/edit")
    public String editBook(@PathVariable String name, Model model) {
        model.addAttribute("book", bookService.getBookByName(name));
        return "book-edit";
    }

    @PostMapping("/{name}/edit")
    public String updateBook(
            @PathVariable String name,
            @Valid @ModelAttribute("book") BookDTO book,
            BindingResult result
    ) {
        if (result.hasErrors())
            return "book-edit";
        bookService.updateBookByName(name, book);
        return "redirect:/books/all";
    }

    @GetMapping("/{name}/delete")
    public String deleteBook(@PathVariable("name") String name) {
        bookService.deleteBookByName(name);
        return "redirect:/books/all";
    }


    @GetMapping("/add")
    public String createBook(Model model) {
        BookDTO book = new BookDTO();
        model.addAttribute("book", book);
        return "book-create";
    }

    @PostMapping("/add")
    public String addBook(
            @Valid @ModelAttribute(name = "book") BookDTO book,
            BindingResult result,
            Model model
    ) {
        System.out.println(result);
        if (result.hasErrors()) {
            model.addAttribute("book", book);
            return "book-create";
        }
        bookService.addBook(book);
        return "redirect:/books/all";
    }

}

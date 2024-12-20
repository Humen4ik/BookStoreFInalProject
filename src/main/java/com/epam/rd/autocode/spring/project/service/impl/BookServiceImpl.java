package com.epam.rd.autocode.spring.project.service.impl;

import com.epam.rd.autocode.spring.project.dto.BookDTO;
import com.epam.rd.autocode.spring.project.exception.NotFoundException;
import com.epam.rd.autocode.spring.project.model.Book;
import com.epam.rd.autocode.spring.project.repo.BookRepository;
import com.epam.rd.autocode.spring.project.service.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private BookRepository bookRepo;
    private ModelMapper mapper;

    public BookServiceImpl(BookRepository bookRepo, ModelMapper mapper) {
        this.bookRepo = bookRepo;
        this.mapper = mapper;
    }

    @Override
    public List<BookDTO> getAllBooks() {
        List<Book> books = bookRepo.findAll();
        return books.stream().map(book -> mapper.map(book, BookDTO.class)).toList();
    }

    @Override
    public BookDTO getBookByName(String name) {
        return mapper.map(bookRepo.findByName(name), BookDTO.class);
    }

    @Override
    public BookDTO updateBookByName(String name, BookDTO book) {
        Book oldBook = bookRepo.findByName(name).orElseThrow(() -> new NotFoundException("Book with name: \"" + name + "\" not found:"));
        oldBook.setName(book.getName());
        oldBook.setGenre(book.getGenre());
        oldBook.setAgeGroup(book.getAgeGroup());
        oldBook.setAuthor(book.getAuthor());
        oldBook.setPrice(book.getPrice());
        oldBook.setCharacteristics(book.getCharacteristics());
        oldBook.setDescription(book.getDescription());
        oldBook.setPages(book.getPages());
        oldBook.setLanguage(book.getLanguage());
        oldBook.setPublicationDate(book.getPublicationDate());
        Book newBook = bookRepo.save(oldBook);
        return mapper.map(newBook, BookDTO.class);
    }

    @Override
    public void deleteBookByName(String name) {
        bookRepo.deleteByName(name);
    }

    @Override
    public BookDTO addBook(BookDTO book) {
        Book bookEntity = mapper.map(book, Book.class);
        return mapper.map(bookRepo.save(bookEntity), BookDTO.class);
    }

    @Override
    public List<BookDTO> findBooks(String query) {
        List<Book> books = bookRepo.findBooks(query);
        return books.stream().map(book -> mapper.map(book, BookDTO.class)).toList();
    }

}

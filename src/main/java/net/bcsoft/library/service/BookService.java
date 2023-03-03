package net.bcsoft.library.service;

import net.bcsoft.library.model.Book;

import java.util.List;

public interface BookService {

    Book saveBook(Book book);
    Book readBookById(Long id);
    Book readBookBySerialCode(String serialCode);
    List<Book> readBooksByAuthor(String author);
    List<Book> readAllBooks();
    Book updateBook(Book book);
    void deleteBook(Long id);
}
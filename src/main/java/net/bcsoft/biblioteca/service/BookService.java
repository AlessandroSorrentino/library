package net.bcsoft.biblioteca.service;

import net.bcsoft.biblioteca.model.Book;

import java.util.List;

public interface BookService {

    List<Book> findBooksByAuthor(String author);
    Book findBookBySerialNumber(String serialNumber);
    List<Book> getAllBooks();
}

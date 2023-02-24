package net.bcsoft.library.service;

import net.bcsoft.library.model.Book;

import java.util.List;

public interface BookService {

    void saveBook(Book book);
    Book readBookById(Long id);
    List<Book> readBooksBySerialCode(String serialCode);
    List<Book> readBooksByAuthor(String author);
    List<Book> readAllBooks();
    void updateBook(Book book);
    void deleteBook(Long id);
}

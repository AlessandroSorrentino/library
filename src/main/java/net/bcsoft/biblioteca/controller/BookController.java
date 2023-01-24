package net.bcsoft.biblioteca.controller;

import lombok.AllArgsConstructor;
import net.bcsoft.biblioteca.model.Book;
import net.bcsoft.biblioteca.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/book")
public class BookController {

private final BookService bookService;

// FUNZIONA
@GetMapping("/get-by-serialNumber/{serialNumber}")
public ResponseEntity<Book> findBookBySerialNumber(@PathVariable String serialNumber) {
    Book bookBySerialNumber = bookService.findBookBySerialNumber(serialNumber);
    return new ResponseEntity<>(bookBySerialNumber, HttpStatus.OK);
}

// FUNZIONA
@GetMapping("/get-by-author/{author}")
public ResponseEntity<List<Book>> findBooksByAuthor(@PathVariable String author) {
    List<Book> booksByAuthor = bookService.findBooksByAuthor(author);
    return new ResponseEntity<>(booksByAuthor, HttpStatus.OK);
}

// FUNZIONA
@GetMapping("/get-all")
public ResponseEntity<List<Book>> getAllBooks() {
    List<Book> books = bookService.getAllBooks();
    return new ResponseEntity<>(books, HttpStatus.OK);
}
}

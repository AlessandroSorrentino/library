package net.bcsoft.library.controller;

import lombok.AllArgsConstructor;
import net.bcsoft.library.model.Book;
import net.bcsoft.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/books")
public class BookController {

    private BookService bookService;

    @PostMapping
    public ResponseEntity<Void> createBook(@RequestBody Book book) {
        bookService.saveBook(book);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.readAllBooks();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable("id") Long id) {
        Book book = bookService.readBookById(id);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    @GetMapping("/serialCode/{serialCode}")
    public ResponseEntity<List<Book>> getBooksBySerialCode(@PathVariable("serialCode") String serialCode) {
        List<Book> books = bookService.readBooksBySerialCode(serialCode);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/author/{author}")
    public ResponseEntity<List<Book>> getBooksByAuthor(@PathVariable("author") String author) {
        List<Book> books = bookService.readBooksByAuthor(author);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateBook(@PathVariable("id") Long id, @RequestBody Book book) {
        book.setId(id);
        bookService.updateBook(book);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable("id") Long id) {
        bookService.deleteBook(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

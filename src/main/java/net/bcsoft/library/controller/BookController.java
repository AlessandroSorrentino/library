package net.bcsoft.library.controller;

import lombok.RequiredArgsConstructor;
import net.bcsoft.library.dto.BookDTO;
import net.bcsoft.library.mapper.BookMapper;
import net.bcsoft.library.model.Book;
import net.bcsoft.library.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final BookMapper bookMapper;


    @PostMapping
    public ResponseEntity<Void> createBook(@Valid @RequestBody BookDTO bookDTO) {
        Book book = bookMapper.toEntity(bookDTO);
        bookService.saveBook(book);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        List<Book> books = bookService.readAllBooks();
        List<BookDTO> bookDTOs = books.stream().map(bookMapper::toDTO).collect(Collectors.toList());
        return new ResponseEntity<>(bookDTOs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable("id") Long id) {
        Book book = bookService.readBookById(id);
        return new ResponseEntity<>(bookMapper.toDTO(book), HttpStatus.OK);
    }

    @GetMapping("/serialCode")
    public ResponseEntity<List<BookDTO>> getBooksBySerialCode(@RequestParam("serialCode") String serialCode) {
        List<Book> books = bookService.readBooksBySerialCode(serialCode);
        List<BookDTO> bookDTOs = books.stream().map(bookMapper::toDTO).collect(Collectors.toList());
        return new ResponseEntity<>(bookDTOs, HttpStatus.OK);
    }

    @GetMapping("/author")
    public ResponseEntity<List<BookDTO>> getBooksByAuthor(@RequestParam("author") String author) {
        List<Book> books = bookService.readBooksByAuthor(author);
        List<BookDTO> bookDTOs = books.stream().map(bookMapper::toDTO).collect(Collectors.toList());
        return new ResponseEntity<>(bookDTOs, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@Valid @PathVariable("id") Long id, @RequestBody BookDTO bookDTO) {
        Book book = bookMapper.toEntity(bookDTO);
        book.setId(id);
        bookService.updateBook(book);
        return new ResponseEntity<>(bookDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable("id") Long id) {
        bookService.deleteBook(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
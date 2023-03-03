package net.bcsoft.library.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping("/api/books")
@Api(value = "Book Management System")
public class BookController {

    private final BookService bookService;
    private final BookMapper bookMapper;


    @PostMapping
    @ApiOperation(value = "Create book")
    public ResponseEntity<BookDTO> createBook(@Valid @RequestBody BookDTO bookDTO) {
        Book book = bookMapper.toEntity(bookDTO);
        bookService.saveBook(book);
        return new ResponseEntity<>(bookDTO, HttpStatus.CREATED);
    }

    @GetMapping
    @ApiOperation(value = "Get all books")
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        List<Book> books = bookService.readAllBooks();
        List<BookDTO> bookDTOs = books.stream()
                .map(bookMapper::toDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(bookDTOs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get book by id")
    public ResponseEntity<BookDTO> getBookById(@PathVariable("id") Long id) {
        Book book = bookService.readBookById(id);
        return new ResponseEntity<>(bookMapper.toDTO(book), HttpStatus.OK);
    }

    @GetMapping("/serialCode")
    @ApiOperation(value = "Get book by serial code")
    public ResponseEntity<BookDTO> getBookBySerialCode(@RequestParam("serialCode") String serialCode) {
        Book book = bookService.readBookBySerialCode(serialCode);
        return new ResponseEntity<>(bookMapper.toDTO(book), HttpStatus.OK);
    }
    @GetMapping("/author")
    @ApiOperation(value = "Get books by author")
    public ResponseEntity<List<BookDTO>> getBooksByAuthor(@RequestParam("author") String author) {
        List<Book> books = bookService.readBooksByAuthor(author);
        List<BookDTO> bookDTOs = books.stream()
                .map(bookMapper::toDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(bookDTOs, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update book by id")
    public ResponseEntity<BookDTO> updateBook(@PathVariable("id") Long id, @Valid @RequestBody BookDTO bookDTO) {
        Book book = bookMapper.toEntity(bookDTO);
        book.setId(id);
        bookService.updateBook(book);
        return new ResponseEntity<>(bookDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete book by id")
    public ResponseEntity<Void> deleteBook(@PathVariable("id") Long id) {
        bookService.deleteBook(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
package net.bcsoft.biblioteca.controller;

import lombok.AllArgsConstructor;
import net.bcsoft.biblioteca.model.Book;
import net.bcsoft.biblioteca.model.Library;
import net.bcsoft.biblioteca.service.LibraryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/library")
public class LibraryController {

    private final LibraryService libraryService;

    // FUNZIONA
    @PostMapping("/create")
    public ResponseEntity<Void> createLibrary(@Validated @RequestBody Library library) {
        libraryService.createLibrary(library);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // FUNZIONA
    @PostMapping("/{libraryId}/add-book")
    public ResponseEntity<Void> addBookToLibrary(@PathVariable Long libraryId, @RequestBody Book book) {
        libraryService.addBookToLibrary(libraryId, book);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // WARN 6480 --- [nio-8080-exec-2] .w.s.m.s.DefaultHandlerExceptionResolver : Resolved [org.springframework.http.converter.HttpMessageNotWritableException: Could not write JSON: failed to lazily initialize a collection of role: net.bcsoft.biblioteca.model.Library.books, could not initialize proxy - no Session; nested exception is com.fasterxml.jackson.databind.JsonMappingException: failed to lazily initialize a collection of role: net.bcsoft.biblioteca.model.Library.books, could not initialize proxy - no Session (through reference chain: net.bcsoft.biblioteca.model.Library["books"])]
    // FUNZIONA
    @GetMapping("/get-by-id/{libraryId}")
    public ResponseEntity<Library> findLibraryById(@PathVariable Long libraryId) {
        Library libraryById = libraryService.findLibraryById(libraryId);
        return new ResponseEntity<>(libraryById, HttpStatus.OK);
    }


    // FUNZIONA
    @PutMapping("/{libraryId}/update-book-amount/{amount}")
    public ResponseEntity<Void> updateBookAmount(@PathVariable("libraryId") Long libraryId, @PathVariable("amount") int amount) {
        libraryService.updateBookAmount(libraryId, amount);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // FUNZIONA
    @PutMapping("/{userId}/borrow-book/{bookId}")
    public ResponseEntity<Void> borrowBook(@PathVariable Long userId, @PathVariable Long bookId) {
        try {
            libraryService.borrowBook(userId, bookId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}





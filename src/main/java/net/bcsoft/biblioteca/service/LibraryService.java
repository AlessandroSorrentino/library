package net.bcsoft.biblioteca.service;

import net.bcsoft.biblioteca.model.Book;
import net.bcsoft.biblioteca.model.Library;


public interface LibraryService {

    void createLibrary(Library library);
    void addBookToLibrary(Long libraryId, Book book);
    Library findLibraryById(Long id);
    void updateBookAmount(Long libraryId, int amount);
    void borrowBook(Long userId, Long bookId);

}

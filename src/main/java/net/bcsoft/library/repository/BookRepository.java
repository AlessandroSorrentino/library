package net.bcsoft.library.repository;

import net.bcsoft.library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT b FROM Book b WHERE lower(b.serialCode) = lower(:serialCode)")
    List<Book> findBySerialCode(@Param("serialCode") String serialCode);

    @Query("SELECT b FROM Book b WHERE lower(b.author) LIKE lower(:author)")
    List<Book> findByAuthor(@Param("author") String author);

    @Query("SELECT b FROM Book b JOIN b.users u")
    List<Book> findAllLoanedBooks();

    boolean existsBySerialCode(String serialCode);
}
package net.bcsoft.biblioteca.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "books")
public class Book {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    @NotBlank
    @Size(max = 100)
    private String title;

    @Column(name = "author", nullable = false)
    @NotBlank
    @Size(max = 100)
    private String author;

    @Column(name = "isbn", nullable = false, unique = true)
    @NotBlank
    @Size(max = 20)
    private String isbn;

    @Column(name = "serial_number", nullable = false, unique = true)
    @NotBlank
    @Size(max = 20)
    private String serialNumber;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "library_id")
    private Library library;

}

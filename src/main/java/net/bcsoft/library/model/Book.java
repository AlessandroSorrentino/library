package net.bcsoft.library.model;

import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.*;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter @Setter
@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title cannot be blank")
    @Column
    private String title;

    @NotBlank(message = "Author cannot be blank")
    @Column
    private String author;

    @NotBlank(message = "Serial code cannot be blank")
    @Column(name = "serial_code")
    private String serialCode;

    @NotNull(message = "Quantity cannot be null")
    @Min(value = 0, message = "Quantity cannot be negative")
    @Column
    private Integer quantity;

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name = "loans",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users;

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", serialCode='" + serialCode + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}

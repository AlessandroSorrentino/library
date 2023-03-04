package net.bcsoft.library.dto;

import lombok.*;

import java.util.List;

@Getter @Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanDTO {

    private String bookTitle;
    private String bookAuthor;
    private List<UserDTO> users;

    @Override
    public String toString() {
        return "LoanDTO{" +
                "bookTitle='" + bookTitle + '\'' +
                ", bookAuthor='" + bookAuthor + '\'' +
                ", users=" + users +
                '}';
    }
}

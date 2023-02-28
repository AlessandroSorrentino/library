package net.bcsoft.library.dto;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanDTO {

    private String bookTitle;
    private String bookAuthor;
    private List<UserDTO> users;
}

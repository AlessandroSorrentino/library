package net.bcsoft.library.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDTO {

    private String title;
    private String author;
    private String serialCode;
    private Integer quantity;
    private List<UserDTO> users;
}




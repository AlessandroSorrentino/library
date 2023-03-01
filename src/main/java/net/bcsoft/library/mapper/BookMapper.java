package net.bcsoft.library.mapper;


import net.bcsoft.library.dto.LoanDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import net.bcsoft.library.dto.BookDTO;
import net.bcsoft.library.model.Book;

// significa che verrà trattato come un bean e potrà essere inniettato in altre classi
@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(target = "book.users", ignore = true)
    @Mapping(target = "book.id", ignore = true)
    BookDTO toDTO(Book book);

    @Mapping(source = "book.title", target = "bookTitle")
    @Mapping(source = "book.author", target = "bookAuthor")
    @Mapping(target = "book.id", ignore = true)
    LoanDTO toLoanDTO(Book book);

    Book toEntity(BookDTO bookDTO);

}

package net.bcsoft.library.mapper;

import java.util.List;

import net.bcsoft.library.dto.LoanDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import net.bcsoft.library.dto.BookDTO;
import net.bcsoft.library.model.Book;

// significa che verrà trattato come un bean e potrà essere inniettato in altre classi
@Mapper(componentModel = "spring")
public interface BookMapper {

    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    BookDTO toDTO(Book book);

    @Mapping(source = "book.title", target = "bookTitle")
    @Mapping(source = "book.author", target = "bookAuthor")
    @Mapping(source = "book.users", target = "users")
    LoanDTO toLoanDTO(Book book);

    Book toEntity(BookDTO bookDTO);

}

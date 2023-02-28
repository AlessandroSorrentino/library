package net.bcsoft.library.mapper;

import java.util.List;

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

    List<BookDTO> toDTOList(List<Book> books);

    Book toEntity(BookDTO bookDTO);

    List<Book> toEntityList(List<BookDTO> bookDTOs);
}

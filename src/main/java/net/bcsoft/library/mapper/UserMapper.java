package net.bcsoft.library.mapper;

import net.bcsoft.library.dto.UserDTO;
import net.bcsoft.library.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target="user.password", ignore = true)
    UserDTO toDTO(User user);

    User toEntity(UserDTO userDTO);

}


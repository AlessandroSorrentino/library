package net.bcsoft.library.mapper;

import net.bcsoft.library.dto.UserDTO;
import net.bcsoft.library.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDTO toDTO(User user);

    List<UserDTO> toDTOList(List<User> users);

    User toEntity(UserDTO userDTO);

    List<User> toEntityList(List<UserDTO> userDTOs);
}


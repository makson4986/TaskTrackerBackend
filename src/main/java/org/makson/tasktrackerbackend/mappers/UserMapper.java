package org.makson.tasktrackerbackend.mappers;

import org.makson.tasktrackerbackend.dto.UserDto;
import org.makson.tasktrackerbackend.models.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends BaseMapper<User, UserDto> {

}

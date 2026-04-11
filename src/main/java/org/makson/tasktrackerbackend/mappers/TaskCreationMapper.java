package org.makson.tasktrackerbackend.mappers;

import org.makson.tasktrackerbackend.dto.TaskCreationDto;
import org.makson.tasktrackerbackend.models.Task;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface TaskCreationMapper extends BaseMapper<Task, TaskCreationDto> {
}

package org.makson.tasktrackerbackend.mappers;

import org.makson.tasktrackerbackend.dto.TaskResponseDto;
import org.makson.tasktrackerbackend.models.Task;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface TaskResponseMapper extends BaseMapper<Task, TaskResponseDto> {
}

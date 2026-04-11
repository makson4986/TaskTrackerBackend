package org.makson.tasktrackerbackend.services;

import lombok.RequiredArgsConstructor;
import org.makson.tasktrackerbackend.dto.TaskCreationDto;
import org.makson.tasktrackerbackend.dto.TaskResponseDto;
import org.makson.tasktrackerbackend.dto.TaskUpdatingDto;
import org.makson.tasktrackerbackend.exceptions.TaskNotFoundException;
import org.makson.tasktrackerbackend.mappers.TaskCreationMapper;
import org.makson.tasktrackerbackend.mappers.TaskResponseMapper;
import org.makson.tasktrackerbackend.models.Task;
import org.makson.tasktrackerbackend.models.User;
import org.makson.tasktrackerbackend.repositories.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskResponseMapper taskResponseMapper;
    private final TaskCreationMapper taskCreationMapper;

    public List<TaskResponseDto> getAll(User user) {
        List<Task> allTask = taskRepository.findAllByOwner(user);
        return taskResponseMapper.toListDto(allTask);
    }

    public TaskResponseDto getById(User user, Integer taskId) {
        Optional<Task> maybeTask = taskRepository.findByOwnerAndId(user, taskId);
        Task task = checkExists(maybeTask);
        return taskResponseMapper.toDto(task);
    }

    public TaskResponseDto create(User user, TaskCreationDto dto) {
        Task task = taskCreationMapper.toModel(dto);
        task.setOwner(user);
        Task newTask = taskRepository.save(task);
        return taskResponseMapper.toDto(newTask);
    }

    public TaskResponseDto update(User user, TaskUpdatingDto dto,  Integer taskId) {
        Optional<Task> maybeTask = taskRepository.findByOwnerAndId(user, taskId);
        Task task = checkExists(maybeTask);
        task.setTitle(dto.title());
        task.setText(dto.text());
        task.setStatus(dto.status());

        Task updatedTask = taskRepository.save(task);
        return taskResponseMapper.toDto(updatedTask);
    }

    public void delete(User user, Integer taskId) {
        Optional<Task> maybeTask = taskRepository.findByOwnerAndId(user, taskId);
        Task task = checkExists(maybeTask);
        taskRepository.delete(task);
    }

    private Task checkExists(Optional<Task> maybeTask) {
        return maybeTask.orElseThrow(() -> new TaskNotFoundException("Task isn't found"));
    }
}

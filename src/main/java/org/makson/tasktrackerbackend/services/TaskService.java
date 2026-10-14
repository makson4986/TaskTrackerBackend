package org.makson.tasktrackerbackend.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.makson.tasktrackerbackend.dto.TaskCreationDto;
import org.makson.tasktrackerbackend.dto.TaskResponseDto;
import org.makson.tasktrackerbackend.dto.TaskUpdatingDto;
import org.makson.tasktrackerbackend.dto.UserTasksDto;
import org.makson.tasktrackerbackend.exceptions.TaskNotFoundException;
import org.makson.tasktrackerbackend.mappers.TaskCreationMapper;
import org.makson.tasktrackerbackend.mappers.TaskResponseMapper;
import org.makson.tasktrackerbackend.models.Task;
import org.makson.tasktrackerbackend.models.TaskStatus;
import org.makson.tasktrackerbackend.models.User;
import org.makson.tasktrackerbackend.repositories.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskResponseMapper taskResponseMapper;
    private final TaskCreationMapper taskCreationMapper;

    @Transactional(readOnly = true)
    public List<TaskResponseDto> getAll(User user) {
        List<Task> allTask = taskRepository.findAllByOwner(user);
        return taskResponseMapper.toListDto(allTask);
    }

    @Transactional(readOnly = true)
    public TaskResponseDto getById(User user, Integer taskId) {
        Optional<Task> maybeTask = taskRepository.findByOwnerAndId(user, taskId);
        Task task = checkExists(maybeTask);
        return taskResponseMapper.toDto(task);
    }

    @Transactional
    public TaskResponseDto create(User user, TaskCreationDto dto) {
        Task task = taskCreationMapper.toModel(dto);
        task.setOwner(user);
        Task newTask = taskRepository.save(task);
        log.info("Task created with id: {} by user: {}", newTask.getId(), user.getId());

        return taskResponseMapper.toDto(newTask);
    }

    @Transactional
    public TaskResponseDto update(User user, TaskUpdatingDto dto, Integer taskId) {
        Optional<Task> maybeTask = taskRepository.findByOwnerAndId(user, taskId);
        Task task = checkExists(maybeTask);
        applyPartialUpdate(dto, task);

        Task updatedTask = taskRepository.save(task);
        log.info("Task updated with id: {} by user: {}", updatedTask.getId(), user.getId());

        return taskResponseMapper.toDto(updatedTask);
    }

    @Transactional
    public void delete(User user, Integer taskId) {
        Optional<Task> maybeTask = taskRepository.findByOwnerAndId(user, taskId);
        Task task = checkExists(maybeTask);

        log.info("Task deleted with id: {} by user: {}", task.getId(), user.getId());
        taskRepository.delete(task);
    }

    @Transactional(readOnly = true)
    public List<UserTasksDto> getTasksForReport(LocalDate period) {
        Map<String, List<UserTasksDto.TaskDto>> userTasks = new HashMap<>();
        List<Task> tasks = taskRepository.getTasksForReport(period);

        tasks.forEach(t ->
                userTasks.computeIfAbsent(t.getOwner().getEmail(), _ -> new ArrayList<>())
                        .add(taskResponseMapper.toTaskDto(t))
        );

        return userTasks.entrySet().stream()
                .map(e -> new UserTasksDto(e.getKey(), period, e.getValue()))
                .toList();
    }

    private Task checkExists(Optional<Task> maybeTask) {
        return maybeTask.orElseThrow(() -> new TaskNotFoundException("Task isn't found"));
    }

    private void applyPartialUpdate(TaskUpdatingDto dto, Task task) {
        if (dto.title() != null) {
            task.setTitle(dto.title());
        }

        if (dto.text() != null) {
            task.setText(dto.text());
        }

        if (dto.status() != null) {
            task.setStatus(dto.status());

            if (dto.status() == TaskStatus.DONE) {
                task.setPerformedAt(LocalDate.now());
            }
        }
    }
}

package org.makson.tasktrackerbackend.controllers;

import lombok.RequiredArgsConstructor;
import org.makson.tasktrackerbackend.dto.TaskCreationDto;
import org.makson.tasktrackerbackend.dto.TaskResponseDto;
import org.makson.tasktrackerbackend.dto.TaskUpdatingDto;
import org.makson.tasktrackerbackend.models.User;
import org.makson.tasktrackerbackend.services.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<List<TaskResponseDto>> getAllTasks(@AuthenticationPrincipal User user) {
        List<TaskResponseDto> allTask = taskService.getAll(user);
        return ResponseEntity.ok(allTask);
    }

    @GetMapping("{id}")
    public ResponseEntity<TaskResponseDto> getTaskById(@AuthenticationPrincipal User user, @PathVariable Integer id) {
        TaskResponseDto task = taskService.getById(user, id);
        return ResponseEntity.ok(task);
    }

    @PostMapping
    public ResponseEntity<TaskResponseDto> createTask(@AuthenticationPrincipal User user, @Validated @RequestBody TaskCreationDto dto) {
        TaskResponseDto task = taskService.create(user, dto);
        return ResponseEntity.ok(task);
    }

    @PatchMapping("{id}")
    public ResponseEntity<TaskResponseDto> updateTask(@AuthenticationPrincipal User user, @PathVariable Integer id, @Validated @RequestBody TaskUpdatingDto dto) {
        TaskResponseDto task = taskService.update(user, dto, id);
        return ResponseEntity.ok(task);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<TaskResponseDto> deleteTask(@AuthenticationPrincipal User user, @PathVariable Integer id) {
        taskService.delete(user, id);
        return ResponseEntity.noContent().build();
    }
}

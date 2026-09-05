package org.makson.tasktrackerbackend.controllers;

import lombok.RequiredArgsConstructor;
import org.makson.tasktrackerbackend.dto.TaskResponseDto;
import org.makson.tasktrackerbackend.dto.UserDto;
import org.makson.tasktrackerbackend.models.User;
import org.makson.tasktrackerbackend.services.TaskService;
import org.makson.tasktrackerbackend.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class InternalReportController {
    private final TaskService taskService;
    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUser() {
        List<UserDto> users = userService.getAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{id}/tasks")
    public ResponseEntity<List<TaskResponseDto>> getAllTasks(@PathVariable int id) {
        User user = userService.getById(id);
        List<TaskResponseDto> allTask = taskService.getAll(user);
        return ResponseEntity.ok(allTask);
    }
}

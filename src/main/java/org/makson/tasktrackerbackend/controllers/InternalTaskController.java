package org.makson.tasktrackerbackend.controllers;

import lombok.RequiredArgsConstructor;
import org.makson.tasktrackerbackend.dto.UserTasksDto;
import org.makson.tasktrackerbackend.services.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reports/tasks")
@RequiredArgsConstructor
public class InternalTaskController {
    private final TaskService taskService;

    @GetMapping("/pending-generation")
    public ResponseEntity<List<UserTasksDto>> getTasksForReport(@RequestParam LocalDate period) {
        List<UserTasksDto> tasks = taskService.getTasksForReport(period);
        return ResponseEntity.ok(tasks);
    }
}

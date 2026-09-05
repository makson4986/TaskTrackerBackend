package org.makson.tasktrackerbackend.dto;

import org.makson.tasktrackerbackend.models.TaskStatus;

import java.time.LocalDateTime;

public record TaskResponseDto(
        int id,
        String title,
        String text,
        UserDto owner,
        TaskStatus status,
        LocalDateTime createdAt,
        LocalDateTime performedAt
) {
}
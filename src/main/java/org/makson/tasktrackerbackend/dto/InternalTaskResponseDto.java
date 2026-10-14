package org.makson.tasktrackerbackend.dto;

import org.makson.tasktrackerbackend.models.TaskStatus;

import java.time.LocalDate;

public record InternalTaskResponseDto(
        String title,
        String text,
        TaskStatus status,
        LocalDate createdAt,
        LocalDate performedAt
) {
}

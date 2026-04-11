package org.makson.tasktrackerbackend.dto;

import org.makson.tasktrackerbackend.models.TaskStatus;

public record TaskUpdatingDto(
        String title,

        String text,

        TaskStatus status
) {
}

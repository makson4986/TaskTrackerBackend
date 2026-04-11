package org.makson.tasktrackerbackend.dto;

import jakarta.validation.constraints.NotBlank;

public record TaskCreationDto(
        @NotBlank(message = "Title must not be empty")
        String title,

        @NotBlank(message = "Text must not be empty")
        String text
) {
}

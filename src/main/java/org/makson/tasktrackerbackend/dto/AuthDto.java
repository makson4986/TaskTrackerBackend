package org.makson.tasktrackerbackend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthDto(
        @Email(message = "Email is not correct")
        @NotBlank(message = "Email must not be empty")
        String email,

        @Size(min = 5, max = 20, message = "The password size must be no less than 5 and no more than 20 characters")
        @NotBlank(message = "Password must not be empty")
        String password) {
}

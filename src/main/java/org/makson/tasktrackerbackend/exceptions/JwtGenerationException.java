package org.makson.tasktrackerbackend.exceptions;

public class JwtGenerationException extends RuntimeException {
    public JwtGenerationException(String message) {
        super(message);
    }

    public JwtGenerationException() {
        super();
    }

    public JwtGenerationException(String message, Throwable cause) {
        super(message, cause);
    }

    public JwtGenerationException(Throwable cause) {
        super(cause);
    }
}

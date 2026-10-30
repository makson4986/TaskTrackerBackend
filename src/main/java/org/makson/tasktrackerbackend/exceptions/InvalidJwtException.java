package org.makson.tasktrackerbackend.exceptions;

public class InvalidJwtException extends RuntimeException {
    public InvalidJwtException(String message) {
        super(message);
    }

    public InvalidJwtException() {
        super();
    }

    public InvalidJwtException(String detail, Throwable ex) {
        super(detail, ex);
    }
}

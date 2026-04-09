package org.makson.tasktrackerbackend.exceptions;

public class SecretLengthException extends RuntimeException {
    public SecretLengthException(String message) {
        super(message);
    }

    public SecretLengthException() {
        super();
    }

    public SecretLengthException(String message, Throwable cause) {
        super(message, cause);
    }

    public SecretLengthException(Throwable cause) {
        super(cause);
    }
}

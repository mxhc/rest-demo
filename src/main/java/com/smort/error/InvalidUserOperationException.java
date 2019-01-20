package com.smort.error;

public class InvalidUserOperationException extends RuntimeException {

    public InvalidUserOperationException() {
    }

    public InvalidUserOperationException(String message) {
        super(message);
    }

    public InvalidUserOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidUserOperationException(Throwable cause) {
        super(cause);
    }

    public InvalidUserOperationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}

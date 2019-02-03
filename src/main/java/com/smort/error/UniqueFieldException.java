package com.smort.error;

public class UniqueFieldException extends RuntimeException {

    public UniqueFieldException() {
    }

    public UniqueFieldException(String message) {
        super(message);
    }

    public UniqueFieldException(String message, Throwable cause) {
        super(message, cause);
    }

    public UniqueFieldException(Throwable cause) {
        super(cause);
    }

    public UniqueFieldException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}

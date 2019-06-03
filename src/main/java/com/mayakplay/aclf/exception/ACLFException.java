package com.mayakplay.aclf.exception;

public class ACLFException extends RuntimeException {

    public ACLFException() {
    }

    public ACLFException(String message) {
        super(message);
    }

    public ACLFException(String message, Throwable cause) {
        super(message, cause);
    }

    public ACLFException(Throwable cause) {
        super(cause);
    }

    public ACLFException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

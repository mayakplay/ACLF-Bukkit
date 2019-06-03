package com.mayakplay.aclf.exception;

/**
 * Created by Mayakplay on 21.04.2019.
 */
public class ACLFCommandException extends RuntimeException {

    public ACLFCommandException() {
    }

    public ACLFCommandException(String message) {
        super(message);
    }

    public ACLFCommandException(String message, Throwable cause) {
        super(message, cause);
    }

    public ACLFCommandException(Throwable cause) {
        super(cause);
    }

    public ACLFCommandException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

package com.zlagoda.exception;

public class EntityLinkException extends RuntimeException {
    public EntityLinkException(String message) {
        super(message);
    }

    public EntityLinkException(String message, Throwable cause) {
        super(message, cause);
    }
}

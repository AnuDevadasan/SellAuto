package com.sellauto.exceptions;

@SuppressWarnings("serial")
public class AllException extends RuntimeException {

    public AllException(String message) {
        super(message);
    }

    public AllException(String message, Throwable cause) {
        super(message, cause);
    }
}

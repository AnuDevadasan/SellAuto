package com.sellauto.exceptions;

@SuppressWarnings("serial")
public class StorageFileNotFoundException extends AllException {

    public StorageFileNotFoundException(String message) {
        super(message);
    }

    public StorageFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
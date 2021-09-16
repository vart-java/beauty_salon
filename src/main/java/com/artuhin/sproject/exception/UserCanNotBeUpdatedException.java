package com.artuhin.sproject.exception;

public class UserCanNotBeUpdatedException extends RuntimeException {
    public UserCanNotBeUpdatedException(String message) {
        super(message);
    }
}

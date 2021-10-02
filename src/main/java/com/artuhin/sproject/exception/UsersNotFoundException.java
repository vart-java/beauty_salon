package com.artuhin.sproject.exception;

public class UsersNotFoundException extends RuntimeException {
    public UsersNotFoundException(String usersNotFoundMessage) {
        super(usersNotFoundMessage);
    }
}

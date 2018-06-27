package com.rsm.role;

public class RoleDoesNotExistException extends RuntimeException {
    public RoleDoesNotExistException() {
        super();
    }

    public RoleDoesNotExistException(String message) {
        super(message);
    }
}

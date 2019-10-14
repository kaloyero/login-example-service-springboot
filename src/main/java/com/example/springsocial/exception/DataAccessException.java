package com.example.springsocial.exception;

public class DataAccessException extends org.springframework.dao.DataAccessException{

    private String errorKey;

    public DataAccessException(String errorKey, String message) {
        super(message);
        this.errorKey = errorKey;
    }

    public String getErrorKey() {
        return this.errorKey;
    }
}

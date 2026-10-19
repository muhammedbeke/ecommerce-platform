package com.emin.e_commerce.exception;

public class AlreadyExistsException extends RuntimeException {
    
    public AlreadyExistsException(String message){
        super(message);
    }
}

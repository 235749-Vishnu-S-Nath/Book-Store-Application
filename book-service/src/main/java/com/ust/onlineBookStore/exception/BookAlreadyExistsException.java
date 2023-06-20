package com.ust.onlineBookStore.exception;

public class BookAlreadyExistsException extends RuntimeException{
    public BookAlreadyExistsException(String message){
        super(message);

    }
}

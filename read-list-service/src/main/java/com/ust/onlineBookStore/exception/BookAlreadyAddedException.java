package com.ust.onlineBookStore.exception;

public class BookAlreadyAddedException extends RuntimeException{
    public BookAlreadyAddedException(String message){
        super(message);

    }
}

package com.ust.onlineBookStore.exception;

public class BookAlreadyRatedException extends RuntimeException{
    public BookAlreadyRatedException(String message){
        super(message);

    }
}

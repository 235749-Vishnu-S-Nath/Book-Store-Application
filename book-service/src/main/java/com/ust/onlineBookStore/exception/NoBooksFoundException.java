package com.ust.onlineBookStore.exception;

public class NoBooksFoundException extends RuntimeException{
    public NoBooksFoundException(String message){
        super(message);
    }

}

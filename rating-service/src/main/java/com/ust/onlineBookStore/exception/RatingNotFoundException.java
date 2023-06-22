package com.ust.onlineBookStore.exception;

public class RatingNotFoundException extends RuntimeException{
    public RatingNotFoundException (String message){
        super(message);
    }

}

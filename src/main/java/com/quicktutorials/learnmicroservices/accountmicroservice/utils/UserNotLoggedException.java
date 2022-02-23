package com.quicktutorials.learnmicroservices.accountmicroservice.utils;

public class UserNotLoggedException extends Exception {
    
    public UserNotLoggedException(String errorMessage){
        super(errorMessage);
    }
}

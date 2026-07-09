package org.example.project3.exceptions;

public class DbOperationException extends Exception{

    public DbOperationException(String message){
        super(message);
    }

    public DbOperationException(String message, Throwable cause){

        super(message, cause);
    }

}
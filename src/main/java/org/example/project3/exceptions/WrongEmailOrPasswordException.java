package org.example.project3.exceptions;

public class WrongEmailOrPasswordException extends Exception {


    public WrongEmailOrPasswordException(String message) {
        super(message);
    }

    //nel caso in cui email e password siano sbagliate

}

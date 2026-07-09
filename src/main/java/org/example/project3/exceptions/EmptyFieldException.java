package org.example.project3.exceptions;


public class EmptyFieldException extends Exception {
    public EmptyFieldException(String message) {
        super(message);
    }

    //Mostra un messaggio quando ci sono fields vuoti
}

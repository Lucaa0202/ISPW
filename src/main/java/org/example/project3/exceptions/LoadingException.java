package org.example.project3.exceptions;

public class LoadingException extends RuntimeException {
    public LoadingException(String message, Throwable cause) {
        super(message, cause);
    }

    //Usato nel caso in cui il caricamento della scena fallisca
}

package org.example.project3.exceptions;

public class LoginAndRegistrationException extends Exception{
    public LoginAndRegistrationException(String message) {
        super(message);
    }
    public LoginAndRegistrationException() {
        super();
    }
    public LoginAndRegistrationException(String message, Throwable cause) {
        super(message, cause);
    }

    // nel caso in cui i dati per il login siano errati
}

package org.example.project3.exceptions;


public class MailAlreadyExistsException extends Exception{

    public MailAlreadyExistsException(String message){super(message);}

    //nel caso la mail esista già
}
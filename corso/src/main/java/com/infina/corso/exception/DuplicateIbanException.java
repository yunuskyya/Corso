package com.infina.corso.exception;

public class DuplicateIbanException extends  RuntimeException{

    public DuplicateIbanException(String message) {
        super(message);
    }
}

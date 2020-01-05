package com.revolut.backendtest.exceptions;

public class NotEnoughFoundsException extends RuntimeException {
    public NotEnoughFoundsException(String message) {
        super(message);
    }
}

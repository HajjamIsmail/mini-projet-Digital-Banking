package com.example.digitalbanking.exception;

public class BalanceNotSufficientException extends Throwable {
    public BalanceNotSufficientException(String message) {
        super(message);
    }
}

package com.ebank.ebank.Exceptions;

public class BalanceNotSufficientException extends Exception {

    public BalanceNotSufficientException(String message) {
        super(message);
    }
}

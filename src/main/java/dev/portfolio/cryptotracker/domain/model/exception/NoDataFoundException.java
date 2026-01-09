package dev.portfolio.cryptotracker.domain.model.exception;

public class NoDataFoundException extends RuntimeException {
    public NoDataFoundException(String message) {
        super(message);
    }
}

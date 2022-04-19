package bsb.group5.converter.controllers.exceptions;

public class PaginationException extends RuntimeException {
    public PaginationException(String message) {
        super(message);
    }
}

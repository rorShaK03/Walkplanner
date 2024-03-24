package ru.hse.walkplanner.exception;

public class ClientErrorException extends RuntimeException {

    public Integer code;

    public ClientErrorException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}

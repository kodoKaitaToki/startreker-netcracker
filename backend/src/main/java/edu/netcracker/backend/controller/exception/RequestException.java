package edu.netcracker.backend.controller.exception;

public class RequestException extends RuntimeException {

    private int errorCode;

    public RequestException() {
        super();
    }

    public RequestException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public RequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestException(Throwable cause) {
        super(cause);
    }

    protected RequestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public int getErrorCode() {
        return errorCode;
    }
}

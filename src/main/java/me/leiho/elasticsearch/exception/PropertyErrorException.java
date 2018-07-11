package me.leiho.elasticsearch.exception;

public class PropertyErrorException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public PropertyErrorException() {
    }

    public PropertyErrorException(String message) {
        super(message);
    }

    public PropertyErrorException(Throwable cause) {
        super(cause);
    }

    public PropertyErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public PropertyErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

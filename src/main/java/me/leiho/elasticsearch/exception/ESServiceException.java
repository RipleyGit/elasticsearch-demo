package me.leiho.elasticsearch.exception;

public class ESServiceException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ESServiceException() {
    }

    public ESServiceException(String message) {
        super(message);
    }

    public ESServiceException(Throwable cause) {
        super(cause);
    }

    public ESServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ESServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

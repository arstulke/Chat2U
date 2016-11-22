package de.chat2u.exceptions;

/**
 * Created AccessDeniedException in de.chat2u
 * by ARSTULKE on 17.11.2016.
 */
public class AccessDeniedException extends IllegalArgumentException {
    private String message;

    /**
     * Exception
     */
    public AccessDeniedException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

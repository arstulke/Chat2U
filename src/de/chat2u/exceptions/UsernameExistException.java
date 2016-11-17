package de.chat2u.exceptions;

/**
 * Created UsernameExistException in de.chat2u.authentication
 * by ARSTULKE on 17.11.2016.
 */
public class UsernameExistException extends IllegalArgumentException {
    private String message;

    @Override
    public String getMessage() {
        return message;
    }

    /**
     * Exception
     * */
    public UsernameExistException(String message) {
        this.message = message;
    }
}

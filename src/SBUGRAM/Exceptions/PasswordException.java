package SBUGRAM.Exceptions;

public class PasswordException extends Exception {
    private String message;
    public PasswordException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String toString() {
        return this.message;
    }
}


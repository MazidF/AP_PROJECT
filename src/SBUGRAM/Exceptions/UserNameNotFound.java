package SBUGRAM.Exceptions;

public class UserNameNotFound extends Exception {

    public UserNameNotFound() {
        super("userName not found.");
    }

    @Override
    public String toString() {
        return "userName not found.";
    }
}

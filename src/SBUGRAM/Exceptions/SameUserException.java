package SBUGRAM.Exceptions;

public class SameUserException extends Exception {
    public SameUserException() {
        super("server contains same Users.");
    }
    @Override
    public String toString() {
        return "server contains same Users.";
    }
}

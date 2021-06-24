package SBUGRAM.Exceptions;

public class SameUserNameException extends Exception {
    public SameUserNameException() {
        super("same UserName exist.");
    }

    @Override
    public String toString() {
        return "same UserName exist.";
    }
}

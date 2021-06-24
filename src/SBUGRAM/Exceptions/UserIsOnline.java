package SBUGRAM.Exceptions;

public class UserIsOnline extends Exception {
    public UserIsOnline() {
        super("user name that you entered, is online.");
    }
}

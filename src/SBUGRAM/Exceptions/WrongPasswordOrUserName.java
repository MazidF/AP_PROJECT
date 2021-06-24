package SBUGRAM.Exceptions;

public class WrongPasswordOrUserName extends Exception{
    public WrongPasswordOrUserName() {
        super("UserName or Password is wrong.");
    }

    @Override
    public String toString() {
        return "UserName or Password is wrong.";
    }
}

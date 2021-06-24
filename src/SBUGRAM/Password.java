package SBUGRAM;

import SBUGRAM.Exceptions.PasswordException;

import java.io.Serial;
import java.io.Serializable;
import java.util.Scanner;

public class Password implements Serializable {
    @Serial
    private static final long serialVersionUID = 6529685820267757690L;
    public static final String Question1 = "who is your favorite teacher?";
    public static final String Question2 = "what is your favorite movie?";

    private boolean forgotPassword1 = false;
    private boolean forgotPassword2 = false;
    private String password;
    private String question1;
    private String question2;

    public Password(String password, String repeat) throws PasswordException {
        if (!password.equals("") && password.equals(repeat)) {
            if (password.equals("fill this part")) throw new PasswordException("it is empty");
            this.password = password;
        }
        else {
            throw new PasswordException("passwords are different !!!");
        }
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public void FirstQuestion(String answer) {
        forgotPassword1 = true;
        question1 = answer;
    }
    public void SecondQuestion(String answer) {
        forgotPassword2 = true;
        question2 = answer;
    }

    public String getQuestion1() {
        return question1;
    }

    public String getQuestion2() {
        return question2;
    }

    public boolean isForgotPassword1() {
        return forgotPassword1;
    }

    public boolean isForgotPassword2() {
        return forgotPassword2;
    }

    public String getPassword() {
        return password;
    }

    public boolean passwordChecker(String password) {
        return password.equals(this.password);
    }
}


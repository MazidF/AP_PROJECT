package SBUGRAM;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class Comment implements Serializable {
    @Serial
    private static final long serialVersionUID = 4529685098267757690L;

    public String userName;
    private final String message;
    private DATE date;

    public Comment(String userName, String message) {
        this.userName = userName;
        this.message = message;
        this.date = new DATE();
    }

    public String getUser() {
        return userName;
    }

    public DATE getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
/*        if (!Objects.equals(userName, comment.userName)) {
            Tools.printException(new Exception("equals 2"));
            return false;
        }
        if (!Objects.equals(message, comment.message)) {
            Tools.printException(new Exception("equals 3"));
            return false;
        }*/
        return Objects.equals(userName, comment.userName) && Objects.equals(message, comment.message)/* && Objects.equals(date, comment.date)*/;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, message, date);
    }

    @Override
    public String toString() {
        return userName + ":\n\t" + message;
    }

    public int compareTo(Object object) {
        Comment comment = (Comment) object;
        return this.getDate().compareTo(comment.getDate());
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
}

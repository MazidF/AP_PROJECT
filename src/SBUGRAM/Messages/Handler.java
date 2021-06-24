package SBUGRAM.Messages;

import SBUGRAM.Server;
import SBUGRAM.User;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public abstract class Handler implements Serializable {
    @Serial
    private static final long serialVersionUID = 6529685098220567690L;


    private List<Object> objects = new ArrayList<>();
    private String userName = null;
    private AddOrRemove addOrRemove = null;

    public void setAddOrRemove(AddOrRemove addOrRemove) {
        this.addOrRemove = addOrRemove;
    }

    public AddOrRemove getAddOrRemove() {
        return addOrRemove;
    }

    public void setObjects(List<Object> objects) {
        this.objects = objects;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Handler() {}
    public Handler(String userName) {
        this.userName = userName;
    }
    public Handler(String userName, Object... objects) {
        this(userName);
        this.objects.addAll(Arrays.asList(objects));
    }

    public abstract void handle(User user);
    public abstract void handle(Server server);

    public List<Object> getObjects() {
        return objects;
    }

    public String getUser() {
        return userName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Handler handler = (Handler) o;
        return Objects.equals(objects, handler.objects);
    }

    @Override
    public int hashCode() {
        return Objects.hash(objects);
    }

    public void work(Object object) {
//        System.out.println(this);
        if (object instanceof User) handle((User) object);
        else if (object instanceof Server) handle((Server) object);
    }

    @Override
    public String toString() {
        return "Handler{" +
                "objects=" + objects +
                ", userName='" + userName + '\'' +
                ", addOrRemove=" + addOrRemove +
                '}';
    }
}

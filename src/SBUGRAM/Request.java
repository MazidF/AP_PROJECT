package SBUGRAM;

import SBUGRAM.Messages.Handler;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

public class Request extends Handler {
    /*
    * field is allUser or onlineUser
    * first index of objects is your request
    * enjoy!!
    * */
    private Field field;

    public void setField(Field field) {
        this.field = field;
    }

    public Field getField() {
        return field;
    }

    public Request(Field field) {
        this.field = field;
    }

    public Request(String userName, Field field) {
        super(userName);
        this.field = field;
    }

    public Request(String userName, Field field, Object... objects) {
        super(userName, objects);
        this.field = field;
    }

    public Request() {
    }

    public Request(String userName) {
        super(userName);
    }

    public Request(String userName, Object... objects) {
        super(userName, objects);
    }

    @Override
    public void handle(User user) {

    }

    @Override
    public void handle(Server server) {
        ObjectOutputStream out;
        ObjectInputStream in;
        Handler handler;
        try {
            Class clazz = Server.class;
            this.field.setAccessible(true);
            Map<String, User> map = (Map<String, User>) field.get(server);
            Set<String> keys = map.keySet();
            for (String key : keys) {
                out = (ObjectOutputStream) server.connections.get(key).get("out");
                in = (ObjectInputStream) server.connections.get(key).get("in");
                out.writeObject(getObjects().get(0));
                out.flush();
                handler = Tools.reader(server, in);
                handler.work(server);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

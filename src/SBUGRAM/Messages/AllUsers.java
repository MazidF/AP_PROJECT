package SBUGRAM.Messages;

import SBUGRAM.Server;
import SBUGRAM.Tools;
import SBUGRAM.User;

import java.util.concurrent.ConcurrentHashMap;

public class AllUsers extends Handler {
    public AllUsers(String userName, Object... objects) {
        super(userName, objects);
    }

    @Override
    public void handle(User user) {
        user.server.allUsers = (ConcurrentHashMap<String, User>) getObjects().get(0);
    }

    @Override
    public void handle(Server server) {
        Tools.Outer(Tools.getOutStream(server, getUser()), new AllUsers(getUser(), server.allUsers));
    }
}

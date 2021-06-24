package SBUGRAM.Messages;

import SBUGRAM.Server;
import SBUGRAM.User;

public class Disconnect extends Handler{
    public Disconnect(String userName) {
        super(userName);
    }

    @Override
    public void handle(User user) {
//        user.disconnectServer();
    }

    @Override
    public void handle(Server server) {
        synchronized (server) {
            server.onlineUsers.remove(getUser());
            server.connections.remove(getUser());
        }
    }
}

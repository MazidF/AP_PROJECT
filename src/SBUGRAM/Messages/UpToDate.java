package SBUGRAM.Messages;

import SBUGRAM.Messages.Handler;
import SBUGRAM.Server;
import SBUGRAM.Tools;
import SBUGRAM.User;

import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UpToDate extends Handler {
    public UpToDate(String userName, Object... objects) {
        super(userName, objects);
    }

    @Override
    public void handle(User user) {
        Server server = (Server) getObjects().get(0);
        user.server = server;

        User copy = server.allUsers.get(getUser());
        try {
            user.copy(copy);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(85258231);
        }
    }

    @Override
    public void handle(Server server) {
        synchronized (server){
            UpToDate upToDate = new UpToDate(getUser(), server.getInstance());
            Tools.Outer(Tools.getOutStream(server, getUser()), upToDate);
        }
    }


    @Override
    public String toString() {
        return "upToDate" + "{" +
                "user: " + getUser() +
                (this.getAddOrRemove() != null ? (", kind: " + this.getAddOrRemove()) : "") +
                "}\n";
    }


}

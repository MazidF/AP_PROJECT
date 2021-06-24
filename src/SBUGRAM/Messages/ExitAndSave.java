package SBUGRAM.Messages;

import SBUGRAM.Server;
import SBUGRAM.User;

import java.io.Serializable;

public class ExitAndSave extends Handler implements Serializable{


    public ExitAndSave(String user, Object... objects) {
        super(user, objects);
    }

    @Override
    public void handle(User user) {
//        user.saveAndExit();
    }

    @Override
    public void handle(Server server) {
//        server.allUsers.put(this.getUser(), (User) getObjects().get(0));
        server.connections.remove(getUser());
        server.onlineUsers.remove(getUser());
    }

}

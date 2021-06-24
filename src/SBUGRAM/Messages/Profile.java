package SBUGRAM.Messages;

import SBUGRAM.Messages.Handler;
import SBUGRAM.Server;
import SBUGRAM.Tools;
import SBUGRAM.User;

import java.io.ObjectOutputStream;
import java.io.Serializable;

/*
public class Profile extends Handler {
    public Profile(String userName, Object... objects) {
        super(userName, objects);
    }

    @Override
    public void handle(User user) {

    }

    @Override
    public void handle(Server server) {
        Tools.Outer(Tools.getOutStream(server, getUser()), server.allUsers.get(getUser()));
    }
}
*/
// we have access to all user :)
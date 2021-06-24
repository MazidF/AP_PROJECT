package SBUGRAM.Messages;

import SBUGRAM.Server;
import SBUGRAM.Tools;
import SBUGRAM.User;

import java.io.ObjectOutputStream;


public class LogIn extends Handler {

    public LogIn(String userName) {
        super(userName);
    }

    public LogIn(String user, Object... objects) {
        super(user, objects);
    }

    @Override
    public void handle(User user) {
//        if (((User) getObjects().get(0)).getUserName().equals(user.getUserName())) {
//            user.posts = ((User) getObjects().get(0)).posts;
//            user.chats = ((User) getObjects().get(0)).chats;
//            return;
//        }
    }

    @Override
    public void handle(Server server) {
        if (getAddOrRemove() == AddOrRemove.ADD) {
            LogIn logeIn = new LogIn(getUser(), server.allUsers.get(this.getUser()));
            Tools.Outer(Tools.getOutStream(server, (String) getObjects().get(0)), logeIn);
        }
        else {
            Tools.Outer((ObjectOutputStream) getObjects().get(0), server.allUsers.get(this.getUser()));
        }

    }

    @Override
    public String toString() {
        return "LogIN ".concat(super.toString());
    }
}

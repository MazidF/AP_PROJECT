package SBUGRAM.Messages;

import SBUGRAM.Server;
import SBUGRAM.Tools;
import SBUGRAM.User;

public class SignIn extends Handler{
    public SignIn(String userName, Object... objects) {// 1.user
        super(userName, objects);
    }

    @Override
    public void handle(User user) {
        user.setServerID((Integer) getObjects().get(0));
    }

    @Override
    public void handle(Server server) {
        User user = (User) getObjects().get(0);
        user.setServerID(server.userID.incrementAndGet());
        server.allUsers.put(getUser(), user);
        server.onlineUsers.add(getUser());
        server.addingUserName = getUser();

    }
}

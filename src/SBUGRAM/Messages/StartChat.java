package SBUGRAM.Messages;

import SBUGRAM.Server;
import SBUGRAM.User;

import java.util.Vector;

public class StartChat extends Handler {
    public StartChat(String userName, String targetName) {
        super(userName, targetName);
    }

    @Override
    public void handle(User user) {
        if (!user.chats.containsKey(getUser())) {
            user.chats.put(getUser(), new Vector<>());
        }
    }

    @Override
    public void handle(Server server) {
        handle(server.allUsers.get((String) getObjects().get(0)));
    }
}

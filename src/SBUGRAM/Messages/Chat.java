package SBUGRAM.Messages;

import SBUGRAM.ChatMessage;
import SBUGRAM.Server;
import SBUGRAM.User;

public class Chat extends Handler {
    public Chat(String userName, Object... objects) {
        super(userName, objects);
    }

    @Override
    public void handle(User user) {
        if (getAddOrRemove() == AddOrRemove.ADD) {
            user.addChat((String) getObjects().get(0), (ChatMessage) getObjects().get(1));
        } else {
            user.removeChat((String) getObjects().get(0), (ChatMessage) getObjects().get(1));
        }
    }

    @Override
    public void handle(Server server) {
        handle(server.allUsers.get(getUser()));
    }
}

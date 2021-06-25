package SBUGRAM.Messages;

import SBUGRAM.Server;
import SBUGRAM.Tools;
import SBUGRAM.User;

public class Block extends Handler{
    public Block(String userName, Object... objects) {
        super(userName, objects);
    }

    @Override
    public void handle(User user) {
        if (getAddOrRemove() == AddOrRemove.ADD) {
            user.BlockedBy((String) getObjects().get(0));
        }
        else {
            user.UnBlockedBy((String) getObjects().get(0));
        }
    }

    @Override
    public void handle(Server server) {
        handle(server.allUsers.get(getUser()));
        if (getAddOrRemove() == AddOrRemove.ADD) {
            server.allUsers.get((String) getObjects().get(0)).block(getUser());
        }
        else {
            server.allUsers.get((String) getObjects().get(0)).unBlock(getUser());
        }
    }
}

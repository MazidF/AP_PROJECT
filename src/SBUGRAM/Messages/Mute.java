package SBUGRAM.Messages;

import SBUGRAM.Server;
import SBUGRAM.Tools;
import SBUGRAM.User;

public class Mute extends Handler{
    public Mute(String userName, Object... objects) {
        super(userName, objects);
    }

    @Override
    public void handle(User user) {
        if (getAddOrRemove() == AddOrRemove.ADD) {
            user.mute((String) getObjects().get(0));
        }
        else {
            user.unMute((String) getObjects().get(0));
        }
    }

    @Override
    public void handle(Server server) {
        handle(server.allUsers.get(getUser()));
    }


    @Override
    public String toString() {
        return "mute".concat(super.toString());
    }
}

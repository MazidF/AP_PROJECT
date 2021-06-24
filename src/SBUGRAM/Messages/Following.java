package SBUGRAM.Messages;

import SBUGRAM.Tools;
import SBUGRAM.Server;
import SBUGRAM.User;

import java.io.ObjectOutputStream;

public class Following extends Handler{
    /*
     * first object of objects should be target user's name.
     * second current user , and user's answer.
     * */
    public Following(String userName, Object... objects) {
        super(userName, objects);
    }

    @Override
    public void handle(User user) {
        if (getAddOrRemove() == AddOrRemove.ADD) {
            if ((boolean) getObjects().get(1)) {
                user.Following((String) getObjects().get(0));
            } else {
                // user doesnt accept :)
            }
        }
        else {
            user.unFollower((String) getObjects().get(0));
        }
    }

    @Override
    public void handle(Server server) {
        handle(server.allUsers.get(getUser()));
        if (getAddOrRemove() == AddOrRemove.ADD) {
            server.allUsers.get(((String) getObjects().get(0))).answerToFollowerRequest(getUser(), (boolean) getObjects().get(1));
        }
    }
}

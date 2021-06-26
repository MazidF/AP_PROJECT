package SBUGRAM.Messages;

import SBUGRAM.Server;
import SBUGRAM.Tools;
import SBUGRAM.User;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class Follower extends Handler{

    /*
    * first object of objects should be user's name.
    * */
    public Follower(String userName, Object... objects) {
        super(userName, objects);
    }

    @Override
    public void handle(User user) {
        if (getAddOrRemove() == AddOrRemove.ADD) {
            user.addFollowerRequest((String) getObjects().get(0));
        }
        else {
            user.UnFollowing((String) getObjects().get(0));
        }
    }

    @Override
    public void handle(Server server) {
        handle(server.allUsers.get(getUser()));
//        Tools.Outer(Tools.getOutStream(server, getUser()), this);
    }


    @Override
    public String toString() {
        return "follower".concat(super.toString());
    }
}

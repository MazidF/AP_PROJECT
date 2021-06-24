package SBUGRAM.Messages;

import SBUGRAM.Server;
import SBUGRAM.User;

import java.io.ObjectOutput;
import java.io.Serial;

public class Post extends Handler{
    /*
    * user should set add or remove
    * first part of this object should be the post.
    * */

    public Post(String userName, Object... objects) {
        super(userName, objects);
    }

    @Override
    public void handle(User user) {
        if (getAddOrRemove() == AddOrRemove.ADD) {
            user.addPost((SBUGRAM.Post) getObjects().get(0));
        }
        else {
            user.removePost((SBUGRAM.Post) getObjects().get(0));
        }
    }

    @Override
    public void handle(Server server) {
        handle(server.allUsers.get(getUser()));
        if (getAddOrRemove() == AddOrRemove.REMOVE) {
            SBUGRAM.Post post = (SBUGRAM.Post) getObjects().get(0);
            post.getRepost().forEach(s -> server.allUsers.get(s).removeRePost(post));
        }
    }
}

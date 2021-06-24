package SBUGRAM.Messages;

import SBUGRAM.Post;
import SBUGRAM.Server;
import SBUGRAM.Tools;
import SBUGRAM.User;

public class Like extends Handler{

    public Like(String userName, Object... objects) {
        super(userName, objects);
    }

    @Override
    public void handle(User user) {
        if (getAddOrRemove() == AddOrRemove.ADD) {
            user.LikedBy((Post) getObjects().get(0), (String) getObjects().get(1));
        }
        else {
            user.UnLikedBy((Post) getObjects().get(0), (String) getObjects().get(1));
        }
    }

    @Override
    public void handle(Server server) {
        handle(server.allUsers.get(getUser()));
    }
}

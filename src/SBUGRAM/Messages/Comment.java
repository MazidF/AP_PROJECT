package SBUGRAM.Messages;

import SBUGRAM.Post;
import SBUGRAM.Server;
import SBUGRAM.Tools;
import SBUGRAM.User;

import java.io.IOException;
import java.io.ObjectOutput;
import java.io.Serial;
import java.io.Serializable;

public class Comment extends Handler {
    /*
     * user should set add or remove
     * first object of objects will be target post , second item will be other user comment
    * */
    public Comment(String userName, Object... objects) {
        super(userName, objects);
    }

    @Override
    public void handle(User user) {
        if (user.getPosts() == null) {
            Tools.printException(new Exception(user.getUserName() + ": post is null."));
            return;
        }
        if (!user.getPosts().contains((Post) getObjects().get(0))) {
            Tools.printException(new Exception(user.getUserName() + ": post doesnt exist."));
            user.getPosts()
                    .forEach(System.out::println);
            System.out.println((Post) getObjects().get(0));
            System.out.println(user.getPosts().get(0).equals((Post) getObjects().get(0)));
            return;
        }
        if (getAddOrRemove() == AddOrRemove.ADD) {
            user.commentBy((Post) getObjects().get(0), (SBUGRAM.Comment) getObjects().get(1));
        }
        else {
            user.removeCommentBy((Post) getObjects().get(0), (SBUGRAM.Comment) getObjects().get(1));
        }
    }

    @Override
    public void handle(Server server) {
        if (server.allUsers.get(getUser()).getPosts() == null) {
            Tools.printException(new Exception(getUser() + ": post is null."));
            return;
        }
        if (!server.allUsers.get(getUser()).getPosts().contains((Post) getObjects().get(0))) {
            Tools.printException(new Exception(getUser() + ": post doesnt exist."));
            return;
        }
        handle(server.allUsers.get(getUser()));
/*        if (server.onlineUsers.containsKey(getUser())) {
            Tools.Outer(Tools.getOutStream(server, getUser()), this);
        }*/
    }
}

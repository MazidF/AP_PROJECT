package SBUGRAM.Messages;

import SBUGRAM.Post;
import SBUGRAM.Server;
import SBUGRAM.User;

public class Repost extends Handler {

    public Repost(String userName, Post post) {
        super(userName, post);
    }

    @Override
    public void handle(User user) {
        if (getAddOrRemove() == AddOrRemove.ADD) {
            user.addRePost((Post) getObjects().get(0));
        } else {
            user.removeRePost((Post) getObjects().get(0));
        }
    }

    @Override
    public void handle(Server server) {
        handle(server.allUsers.get(getUser()));
        if (getAddOrRemove() == AddOrRemove.ADD) {
            server.allUsers.get(((Post) getObjects().get(0)).getUser()).posts.stream()
                    .filter(post -> post.equals(getObjects().get(0)))
                    .forEach(post -> post.Repost(getUser()));
        } else {
            server.allUsers.get(((Post) getObjects().get(0)).getUser()).posts.stream()
                    .filter(post -> post.equals(getObjects().get(0)))
                    .forEach(post -> post.UnRepost(getUser()));
        }
    }


    public String toString() {
        return "repost{" +
                "user: " + this.getUser() +
                ", target: " + ((Post) getObjects().get(0)).getUser() +
                ", for post: " + ((SBUGRAM.Post) this.getObjects().get(0)).getTitle() +
                (this.getAddOrRemove() != null ? (", kind: " + getAddOrRemove()) : "") +
                "}\n";
    }
}

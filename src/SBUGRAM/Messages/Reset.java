package SBUGRAM.Messages;

import SBUGRAM.Comment;
import SBUGRAM.Post;
import SBUGRAM.Server;
import SBUGRAM.Tools;
import SBUGRAM.User;

import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Reset extends Handler{
    public Reset(String userName) {
        super(userName);
    }

    @Override
    public void handle(User user) {
        user.UnBlockedBy(getUser());
        user.UnBlock(getUser());
        user.UnFollower(getUser());
        user.UnFollowing(getUser());
        user.UnMute(getUser());
        user.getChats().remove(getUser());
        user.getFollowRequest().remove(getUser());
        user.isMute.remove(getUser());
        user.isBlock.remove(getUser());
        user.chats.remove(getUser());
        user.blockedBy.remove(getUser());
        user.followRequest.remove(getUser());
        user.follower.remove(getUser());
        user.following.remove(getUser());
        user.reposts.removeAll(user.reposts.stream()
                .filter(rePost -> rePost.post.getUser().equals(getUser()))
                .collect(Collectors.toList()));
        for (Post post : user.posts) {
            List<Comment> comments = post.getComments();
            for (Comment comment : comments) {
                if (comment.getUser().equals(getUser())) {
                    post.UnComment(comment);
                }
            }
        }
    }

    @Override
    public void handle(Server server) {
        synchronized (server){
            server.allUsers.remove(getUser());
        }
        server.allUsers.keySet().stream()
                .map(key -> server.allUsers.get(key))
                .forEach(this::handle);
    }
}

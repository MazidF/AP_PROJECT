package SBUGRAM.Messages;

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
    }

    @Override
    public void handle(Server server) {
        synchronized (server.allUsers){
            server.allUsers.remove(getUser());
        }
        server.allUsers.keySet().stream()
                .map(key -> server.allUsers.get(key))
                .forEach(this::handle);
    }
}

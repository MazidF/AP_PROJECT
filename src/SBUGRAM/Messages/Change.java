package SBUGRAM.Messages;

import SBUGRAM.ChatMessage;
import SBUGRAM.Post;
import SBUGRAM.Server;
import SBUGRAM.User;

import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class Change extends Handler {

    public Change(String userName, Object... objects) {
        super(userName, objects);
    }

    /*User(String userName, String firstName, String lastName, Password password, DATE birthDay)*/
    @Override
    public void handle(User user) {
        Vector<ChatMessage> chatMessages = user.chats.get(getUser());
        if (user.chats.containsKey(getUser())) {
            user.chats.remove(getUser());
            user.chats.put(getUser(), chatMessages);
        }

        List<String> strings = Arrays.asList("isMute", "isBlock", "blockedBy", "following", "follower", "followRequest");
        Field [] fields = User.class.getFields();
        for (Field field : fields) {
            if (strings.contains(field.getName())) {
                field.setAccessible(true);
                try {
                    ConcurrentSkipListSet<String> set =  (ConcurrentSkipListSet<String>) field.get(user);
                    if (set.contains(getUser())) {
                        set.remove(getUser());
                        set.add((String) getObjects().get(0));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        user.reposts.stream()
                .filter(rePost -> rePost.post.userName.equals(getUser()))
                .forEach(rePost -> rePost.post.userName = (String) getObjects().get(0));

        user.posts.stream()
                .filter(post -> post.getUser().equals(getUser()))
                .forEach(post -> post.userName = (String) getObjects().get(0));

        user.posts.stream()//TODO check this part and complete this
                .map(Post::getComments)
                .forEach(comments -> comments.stream()
                                            .filter(comment -> comment.getUser().equals(getUser()))
                                            .forEach(comment -> comment.userName = (String) getObjects().get(0)));
    }

    @Override
    public void handle(Server server) {
        User user;
        synchronized (server.allUsers) {
            server.allUsers.get(getUser())   .changed(getObjects());
            user = server.allUsers.get(getUser());// TODO check if user change or not :)
            server.allUsers.remove(getUser());
            server.allUsers.put(user.getUserName(), user);

            server.allUsers.keySet().stream()
                    //TODO check this part
//                    .filter(s -> !(s.equals(user.getUserName())))
                    .map(s -> server.allUsers.get(s))
                    .forEach(this::handle);
        }
        synchronized (server.onlineUsers){
            server.onlineUsers.remove(getUser());
            server.onlineUsers.add(user.getUserName());
        }
        synchronized (server.connections){
            ConcurrentHashMap<String, Object> map = server.connections.get(getUser());
            server.connections.remove(getUser());
            server.connections.put(user.getUserName(), map);
        }
    }


}

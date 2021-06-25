package SBUGRAM;

import SBUGRAM.Exceptions.UserIsOnline;
import SBUGRAM.Messages.*;
import javafx.scene.image.Image;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


public class User extends Thread implements Serializable{
    @Serial
    private static final long serialVersionUID = 6529685098267757690L;
    public final static int PORT = 8765;
    public final static String HOST = "localhost";



    public User(Socket socket, String userName, String firstName, String lastName, Password password
            , DATE birthDay) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.birthDay = birthDay;
        this.socket = socket;
        this.posts = new Vector<>();
        this.reposts = new Vector<>();
        try {
            streamMaker();
            start();
            Outer(new SignIn(userName, this));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-2);
        }
    }

    public User(Socket socket, String userName, String firstName, String lastName, Password password
            , DATE birthDay, byte[] image, String phoneNumber) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.birthDay = birthDay;
        this.socket = socket;
        this.posts = new Vector<>();
        this.reposts = new Vector<>();
        this.image = image;
        this.phoneNumber = phoneNumber;
        try {
            streamMaker();
            start();
            Outer(new SignIn(userName, this));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-2);
        }
    }

    public User(Socket socket, String userName) throws Exception {
        this.socket = socket;
        User loginUser = LogIn(socket, userName);
        Field [] fields = loginUser.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                if (!Modifier.isStatic(field.getModifiers())) {
                    if (field.getName().equals("inputStream") || field.getName().equals("outputStream") || field.getName().equals("socket") || field.getName().equals("server")) {
                        continue;
                    }
                    field.set(this, field.get(loginUser));
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(-3);
            }
        }
        if (this.outputStream == null || this.inputStream == null) {
            streamMaker();
        }
        this.start();
    }

    public void copy(User user) throws Exception {
        synchronized (this) {
            Field[] fields = user.getClass().getDeclaredFields();
            for (Field field : fields) {
                try {
                    field.setAccessible(true);
                    if (!Modifier.isStatic(field.getModifiers())) {
                        if (field.getName().equals("inputStream") || field.getName().equals("outputStream") || field.getName().equals("socket") || field.getName().equals("server")) {
                            continue;
                        }
                        field.set(this, field.get(user));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(-3);
                }
            }
            if (this.server == null) throw new Exception();
        }
    }

    public void upToDate() {
        Outer(new UpToDate(this.getUserName()));
    }

    public User LogIn(Socket socket, String userName) throws Exception {
        User result = null;
        try {
            this.socket = socket;
            streamMaker();
            Tools.Outer(this.outputStream, new LogIn(userName));
            Tools.sleep(100);
            result = (User) this.inputStream.readObject();
            if (result != null) {
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-8797);
        }
        throw new UserIsOnline();
    }

    private long serverID = -1;
    private String phoneNumber = null;
    private String userName = null;
    private String firstName = null;
    private String lastName = null;
    private Password password = null;
    private DATE birthDay = null;
    public Vector<Post> posts = null;
    public Vector<RePost> reposts = null;
    private byte[] image = null;
    public final ConcurrentHashMap<String, Vector<ChatMessage>> chats = new ConcurrentHashMap<>();
    public final ConcurrentSkipListSet<String> isMute = new ConcurrentSkipListSet<>();
    public final ConcurrentSkipListSet<String> isBlock = new ConcurrentSkipListSet<>();
    public final ConcurrentSkipListSet<String> blockedBy = new ConcurrentSkipListSet<>();
    public final ConcurrentSkipListSet<String> following = new ConcurrentSkipListSet<>();
    public final ConcurrentSkipListSet<String> follower = new ConcurrentSkipListSet<>();
    public final ConcurrentSkipListSet<String> followRequest = new ConcurrentSkipListSet<>();


    private transient ObjectInputStream inputStream = null;
    private transient ObjectOutputStream outputStream = null;
    private transient Socket socket;
    public Server server = null;



    public void addChat(String userName, ChatMessage message) {
        if (!chats.containsKey(userName)) {
            chats.put(userName, new Vector<>());
            if (this.outputStream != null) {
                StartChat startChatOut = new StartChat(getUserName(), userName);
                Outer(startChatOut);
            }
        }
        chats.get(userName).add(message);
    }

    public void AddChat(String userName, ChatMessage message) {
        addChat(userName, message);
        Chat chatOut = new Chat(this.userName, userName, message);
        chatOut.setAddOrRemove(AddOrRemove.ADD);
        Outer(chatOut);
    }

    public void removeChat(String userName, ChatMessage message) {
        if (!chats.containsKey(userName)) return;
        chats.get(userName).remove(message);
    }

    public void RemoveChat(String userName, ChatMessage message) {
        removeChat(userName, message);
        Chat chatOut = new Chat(this.userName, userName, message);
        chatOut.setAddOrRemove(AddOrRemove.REMOVE);
        Outer(chatOut);
    }

    public void block(String userName) {
        if (!userName.equals(this.userName)) {
            isBlock.add(userName);
        }
    }

    public void Block(String userName) {
        block(userName);
        Block blockOut = new Block(userName, this.getUserName());
        blockOut.setAddOrRemove(AddOrRemove.ADD);
        Outer(blockOut);
        Following followingOUt = new Following(userName, this.getUserName());
        followingOUt.setAddOrRemove(AddOrRemove.REMOVE);
        Outer(followingOUt);
    }

    public void unBlock(String userName) {
        if (userName.equals(this.userName) || (!isBlock.contains(userName))) return;
        isBlock.remove(userName);
    }

    public void UnBlock(String userName) {
        unBlock(userName);
        Block blockOut = new Block(userName, this.getUserName());
        blockOut.setAddOrRemove(AddOrRemove.REMOVE);
        Outer(blockOut);
    }

    public void mute(String userName) {
        if (userName.equals(this.userName)) return;
        isMute.add(userName);
    }

    public void Mute(String userName) {
        mute(userName);
        Mute muteOut = new Mute(this.userName, userName);
        muteOut.setAddOrRemove(AddOrRemove.ADD);
        Outer(muteOut);
    }

    public void unMute(String userName) {
        if (userName.equals(this.userName) || (!isMute.contains(userName))) return;
        isMute.remove(userName);
    }

    public void UnMute(String userName) {
        unMute(userName);
        Mute muteOut = new Mute(this.userName, userName);
        muteOut.setAddOrRemove(AddOrRemove.REMOVE);
        Outer(muteOut);
    }

    public void BlockedBy(String userName) {
        if (userName.equals(this.userName)) return;
        blockedBy.add(userName);
    }

    public void UnBlockedBy(String userName) {
        if (userName.equals(this.userName) || (!blockedBy.contains(userName))) return;
        blockedBy.remove(userName);
    }

    public void Following(String userName) {
        if (userName.equals(this.userName)) return;
        following.add(userName);
    }

    public void unFollowing(String userName) {
        if (userName.equals(this.userName) || (!following.contains(userName))) return;
        following.remove(userName);
    }

    public void UnFollowing(String userName) {
        unFollowing(userName);
        Following followingOut = new Following(userName, this.getUserName());
        followingOut.setAddOrRemove(AddOrRemove.REMOVE);
        Outer(followingOut);
    }

    public void Follower(String userName) {
        if (userName.equals(this.userName)) return;
        follower.add(userName);
    }

    public void unFollower(String userName) {
        if (userName.equals(this.userName) || (!follower.contains(userName))) return;
        follower.remove(userName);
    }

    public void UnFollower(String userName) {
        unFollower(userName);
        Follower followerOut = new Follower(userName, this.getUserName());
        followerOut.setAddOrRemove(AddOrRemove.REMOVE);
        Outer(followerOut);
    }

    public void requestFollowing(String userName) {
        if (userName.equals(this.userName)) return;
        Follower followerOut = new Follower(userName, this.getUserName());
        followerOut.setAddOrRemove(AddOrRemove.ADD);
        Outer(followerOut);
    }

    public void addFollowerRequest(String userName) {
        if (userName.equals(this.userName)) return;
        followRequest.add(userName);
    }

    public void removeFollowerRequest(String userName) {
        if (userName.equals(this.userName) || (!followRequest.contains(userName))) return;
        followRequest.remove(userName);
    }

    public void answerToFollowerRequest(String userName, boolean answer) {
        if (!followRequest.contains(userName)) return;
        if (answer) {
            Follower(userName);
        }
        removeFollowerRequest(userName);
    }

    public void AnswerToFollowerRequest(String userName, boolean answer) {
        answerToFollowerRequest(userName, answer);
        Following followingOut = new Following(userName, this.getUserName(), answer);
        followingOut.setAddOrRemove(AddOrRemove.ADD);
        Outer(followingOut);
    }

    public void commentBy(Post post, Comment comment) {
        if (post == null) return;
        if (!posts.contains(post)) return;
        for (Post value : posts) {
            if (value.equals(post)) {
                value.Comment(comment);
                break;
            }
        }
    }

    public void removeCommentBy(Post post, Comment comment) {
        if (!posts.contains(post)) return;
        for (Post value : posts) {
            if (value.equals(post)) {
                value.UnComment(comment);
                break;
            }
        }
    }

    public void addPost(Post post) {
        if (this.posts == null) {
            this.posts = new Vector<>();
        }
        this.posts.add(post);
    }

    public void Post(Post post) {
        if (this.posts.contains(post)) return;
        addPost(post);
        SBUGRAM.Messages.Post postOut = new SBUGRAM.Messages.Post(getUserName(), post);
        postOut.setAddOrRemove(AddOrRemove.ADD);
        Outer(postOut);
    }

    public void addRePost(Post post) {
        if (this.reposts == null) {
            this.reposts = new Vector<>();
        }
        this.reposts.add(new RePost(post));
    }


    public void RePost(Post post) {
        addRePost(post);
        SBUGRAM.Messages.Repost repostOut = new SBUGRAM.Messages.Repost(getUserName(), post);
        repostOut.setAddOrRemove(AddOrRemove.ADD);
        Outer(repostOut);
    }

    public void removePost(Post post) {
        this.posts.remove(post);
    }

    public void RemovePost(Post post) {
        removePost(post);
        SBUGRAM.Messages.Post postOut = new SBUGRAM.Messages.Post(getUserName(), post);
        postOut.setAddOrRemove(AddOrRemove.REMOVE);
        Outer(postOut);
    }

    public void removeRePost(Post post) {
        RePost rePost = this.reposts.stream()
                .filter(rePost1 -> rePost1.getInnerPost().equals(post))
                .collect(Collectors.toList()).get(0);
        this.reposts.remove(rePost);
    }

    public void RemoveRepost(Post post) {
        removeRePost(post);
        SBUGRAM.Messages.Repost repostOut = new SBUGRAM.Messages.Repost(getUserName(), post);
        repostOut.setAddOrRemove(AddOrRemove.REMOVE);
        Outer(repostOut);
    }

    public void Comment(Post post, Comment comment) {
        SBUGRAM.Messages.Comment commentOut = new SBUGRAM.Messages.Comment(post.getUser(),post, comment);
        commentOut.setAddOrRemove(AddOrRemove.ADD);
        Outer(commentOut);
    }

    public void UNComment(Post post,Comment comment) {
        SBUGRAM.Messages.Comment commentOut = new SBUGRAM.Messages.Comment(post.getUser(),post, comment);
        commentOut.setAddOrRemove(AddOrRemove.REMOVE);
        Outer(commentOut);
    }

    public void Like(Post post) {
        if (post.getUser().equals(userName)) {
            return;
        }
        Like likeOut = new Like(post.getUser(), post, userName);
        likeOut.setAddOrRemove(AddOrRemove.ADD);
        Outer(likeOut);
    }

    public void UnLike(Post post) {
        if (post.getUser().equals(userName)) {
            return;
        }
        Like likeOut = new Like(post.getUser(), post, userName);
        likeOut.setAddOrRemove(AddOrRemove.REMOVE);
        Outer(likeOut);
    }

    public void LikedBy(Post post, String userName) {
        if (post == null) return;
        if (!posts.contains(post)) return;
        if (this.posts.stream().filter(post1 -> post1.equals(post)).count() != 1) {
            try {
                throw new Exception("we have same post!!");
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(-232332323);
            }
        }
        this.posts.stream()
                .filter(post1 -> post1.equals(post))
                .forEach(post1 -> post1.Like(userName));
    }

    public void UnLikedBy(Post post, String userName) {
        if (post == null) return;
        if (!posts.contains(post)) return;
        if (this.posts.stream().filter(post1 -> post1.equals(post)).count() != 1) {
            try {
                throw new Exception("we have same post!!");
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(-232332329);
            }
        }
        this.posts.stream()
                .filter(post1 -> post1.equals(post))
                .forEach(post1 -> post1.UnLike(userName));
    }

////////////////////////////////////////////////////////////////


    public void saveAndExit() {
        synchronized (this){
            Outer(new ExitAndSave(getUserName(), this));
        }
        try {
            Tools.sleep(1000);
            System.out.println(new DATE().toString());
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-7777);
        }
    }

    public static Socket socketMaker() throws InterruptedException {
        AtomicReference<Socket> socket = new AtomicReference<>();
        Thread thread = new Thread(() ->
        {
            try {
                socket.set(new Socket(HOST, PORT));
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(-1);
            }
        });
        thread.start();
        thread.join();
        return socket.get();
    }

    public void streamMaker() throws Exception{
        this.outputStream = new ObjectOutputStream(this.socket.getOutputStream());
        this.inputStream = new ObjectInputStream(this.socket.getInputStream());
    }

    private void streamMaker(Socket socket) throws Exception{
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        this.inputStream = new ObjectInputStream(socket.getInputStream());
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) throws Exception {
        this.socket = socket;
        streamMaker();
    }

    public void setServerID(long serverID) {
        this.serverID = serverID;
    }

    public void change(String userName, String firstName, String lastName, Password password, DATE birthDay) {
        Object[] objects = {userName, firstName, lastName, password, birthDay};
        this.changed(Arrays.asList(objects));
    }
    public void Change(String userName, String firstName, String lastName, Password password, DATE birthDay) {
        Object [] objects = {userName, firstName, lastName, password, birthDay};
        Change changeOut = new Change(getUserName(), objects);
        Outer(changeOut);
        change(userName, firstName, lastName, password, birthDay);
    }
    public void changed(List<Object> objects) {
        int index = 0;
        for (Object object : objects) {
            if (object != null) {
                switch (index) {
                    case 0 -> this.userName = (String) object;
                    case 1 -> this.firstName = (String) object;
                    case 2 -> this.lastName = (String) object;
                    case 3 -> this.password = (Password) object;
                    case 4 -> this.birthDay = (DATE) object;
//                    case 5 -> this.location = (String) object;
                    case 6 -> this.phoneNumber = (String) object;
                }
            }
            index++;
        }
    }

    public void changeUserName(String userName) {
        this.userName = userName;
    }

    public void changeFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void changeLastName(String lastName) {
        this.lastName = lastName;
    }

    public void changeImage(byte... image) {
        this.image = image;
    }

    public void changePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public ObjectOutputStream getOutputStream() {
        return outputStream;
    }

    public ObjectInputStream getInputStream() {
        return inputStream;
    }

    public ConcurrentSkipListSet<String> getBlockedBy() {
        return blockedBy;
    }

    public ConcurrentSkipListSet<String> getFollowRequest() {
        return followRequest;
    }

    public Server getServer() {
        return server;
    }

    public ConcurrentSkipListSet<String> getFollower() {
        return follower;
    }

    public ConcurrentSkipListSet<String> getFollowing() {
        return following;
    }

    public long getServerID() {
        return serverID;
    }

    public Password getPassword() {
        return password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getUserName() {
        return userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public DATE getBirthDay() {
        return birthDay;
    }

    public Vector<Post> getPosts() {
        return posts;
    }

    public Vector<RePost> getReposts() {
        return reposts;
    }

    public ConcurrentHashMap<String, Vector<ChatMessage >> getChats() {
        return chats;
    }

    public Vector<ChatMessage> getMessages(String userName) {
        return chats.get(userName);
    }

    public byte[] getImage() {
        return image;
    }

/*    public String getLocation() {
        return location;
    }*/

    public ConcurrentSkipListSet<String> getIsBlock() {
        return isBlock;
    }

    public ConcurrentSkipListSet<String> getIsMute() {
        return isMute;
    }





    public String reset(String password) {
        if (this.password.passwordChecker(password)) {

            return "done.";
        }
        return "wrong password.";
    }

    public void Outer(Object object) {
        try {
            this.outputStream.writeUnshared(object);
            this.outputStream.flush();
            this.outputStream.reset();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-987645);
        }
    }

        @Override
    public String toString() {
        String result = userName + ": " + firstName + " " + lastName;
        if (phoneNumber != null) {
            result +="\n\tphoneNumber : " + phoneNumber;
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userName, user.userName);//Objects.equals(this.serverID, user.serverID) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(birthDay, user.birthDay) && Objects.equals(location, user.location) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName/*, firstName, lastName, password, birthDay, posts, chats, image, location, isBlock, isMute*/);
    }

    public int compareTo(User user) {
        return (int) (this.serverID - user.serverID);
    }

    @Override
    public void run() {
        Object object = null;
        Handler handler = null;
        while (true) {
            try {
                if ((object = inputStream.readObject()) != null) {
                    handler = (Handler) object;
                    synchronized (this){
                        handler.work(this);
                    }
                    if (handler instanceof UpToDate) {
                        System.out.print("");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }

    @Override
    public void interrupt() {
        super.interrupt();
    }

    public static void main(String[] args) throws Exception {
        User user = new User(User.socketMaker(), "m.m", "m", "m", new Password("1", "1"), new DATE());
    }

}

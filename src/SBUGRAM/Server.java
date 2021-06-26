package SBUGRAM;

import SBUGRAM.Exceptions.PasswordException;
import SBUGRAM.Exceptions.SameUserNameException;
import SBUGRAM.Exceptions.UserNameNotFound;
import SBUGRAM.Messages.*;
import SBUGRAM.Scenes.ProfileLabel;
import SBUGRAM.Scenes.Viewer;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Server implements Callable<Server>, Serializable {
    @Serial
    private static final long serialVersionUID = 7529685098267757690L;

//    public final int PORT = 8765;
    public final int PORT = 4200;
    public final String SOURCE_PATH = "C:\\Users\\vcc\\IdeaProjects\\first fx\\src\\SOURCE\\SBUGRAM_SOURCE.text";
    public final AtomicInteger userID = new AtomicInteger(0);
    public Boolean hasMade = false;
    public Vector<Post> posts;
    public Vector<Handler> unHandled;
    public ConcurrentHashMap<String, User> allUsers = new ConcurrentHashMap<>();
    public transient ConcurrentHashMap<String, ConcurrentHashMap<String, Object>> connections;
    public ConcurrentSkipListSet<String> onlineUsers;
    public transient ServerSocket socketServer;
    public transient Future<Server> future;
    public String addingUserName;



    public Server getInstance() {
        return this;
    }

    public void getInstance(String userName) {
        this.connections.get(userName).get("out");
    }

    public Server(Server serverStream) {
        ServerMaker(serverStream);
    }

    public void ServerMaker(Server serverStream) {
        try {
            Class<Server> clazz = Server.class;
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if (!Modifier.isStatic(field.getModifiers())) {
                    field.set(this, field.get(serverStream));
                }
            }
            hasMade = false;
            onlineUsers = new ConcurrentSkipListSet<>();
            posts = new Vector<>();
            unHandled = new Vector<>();
            connections = new ConcurrentHashMap<>();
            try {
                this.socketServer = new ServerSocket(PORT);
                synchronized (this.hasMade) {
                    hasMade = true;
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(-6);
            }
            try {
                this.future = Executors.newSingleThreadExecutor().submit(this);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(-100000);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            System.exit(988);
        }
    }

    private Server() {
        try {
            if (Files.exists(Paths.get(SOURCE_PATH))) {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(SOURCE_PATH));
                Server server = null;
                while (true) {
                    server = (Server) in.readObject();
                    if (server != null) {
                        in.close();
                        Files.deleteIfExists(Paths.get(SOURCE_PATH));
                        ServerMaker(server);
                        System.out.println("server loaded");
                        return;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-33332547);
        }
        hasMade = false;
        onlineUsers = new ConcurrentSkipListSet<>();
        posts = new Vector<>();
        unHandled = new Vector<>();
        connections = new ConcurrentHashMap<>();
        try {
            this.socketServer = new ServerSocket(PORT);
            synchronized (this.hasMade) {
                hasMade = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-6);
        }
        try {
            this.future = Executors.newSingleThreadExecutor().submit(this);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-100000);
        }
    }

    @Override
    public Server call() throws Exception {
        Socket socket;
        while (true) {
            try {
                socket = socketServer.accept();
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                outputStream.writeObject(this);
                outputStream.flush();
                new Client(socket);
                socket = null;
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
        endServer();
        System.out.println("this is closed.");
        return this;
    }

    public void endServer() throws Exception {
        synchronized (this) {
            System.out.println(Thread.currentThread());
        }
        ObjectOutputStream objectOutputStream;
        ObjectInputStream objectInputStream;
        User object;
        //Todo make all them in a class
        if (!Files.exists(Path.of(SOURCE_PATH))) {
            File file = new File(SOURCE_PATH);
        }
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(SOURCE_PATH));) {
            List<String> userList = new ArrayList<>(this.onlineUsers);
            int size = onlineUsers.size();
            for (int i = 0; i < size; i++) {
                Tools.Outer(Tools.getOutStream(this, userList.get(i)), new ExitAndSave(userList.get(i)));
            }
            Tools.sleep(2000);// wait to receive Users data ...
            out.writeObject(null);
            out.writeObject(this);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
            // we have problem so i dont save the server.
            Files.deleteIfExists(Paths.get(SOURCE_PATH));
            System.out.println("file deleted :(");
        }
        try {
            socketServer.close();
        } catch (Exception e) {
            e.printStackTrace();
            //ignore
        }
        System.exit(1);
    }

    private Server starter() throws Exception {
        if (Files.exists(Paths.get(SOURCE_PATH))) {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(SOURCE_PATH));
            return (Server) in.readObject();
        }
        return new Server();
    }

    public Boolean getHasMade() {
        return hasMade;
    }


    public Post postFinder(Post post) {
        synchronized (allUsers) {
            User filtered = allUsers.keySet().stream()
                    .map(key -> allUsers.get(key))
                    .filter(user -> user.getPosts().contains(post))
                    .collect(Collectors.toList()).get(0);
            return filtered.getPosts().stream().filter(post1 -> post1.equals(post)).collect(Collectors.toList()).get(0);
        }
    }

    public Comment commentFinder(Comment comment) {
        return allUsers.keySet().stream()
                .map(key -> allUsers.get(key))
                .filter(user -> user.getPosts().stream().anyMatch(post -> post.getComments().contains(comment)))
                .collect(Collectors.toList()).get(0)
                .getPosts().stream()
                .filter(post -> post.getComments().contains(comment))
                .collect(Collectors.toList()).get(0).getComments().stream().filter(comment1 -> comment1.equals(comment)).collect(Collectors.toList()).get(0);

    }


    public void addPost(Post post) {
        synchronized (posts) {

            if (this.posts == null) {
                this.posts = new Vector<>();
            }
            this.posts.add(post);
        }
    }

    public void removePost(Post post) {
        synchronized (posts){
            this.posts.remove(post);
        }
    }

    public void addUnreadObject(List<Handler> unHandled) {
        synchronized (this.unHandled){
            if (this.unHandled == null) {
                this.unHandled = new Vector<>();
            }
            this.unHandled.addAll(unHandled);
        }
    }

    public Future<Server> getFuture() {
        return future;
    }

    public void userNameChecker(String userName) throws Exception {
        synchronized (this.allUsers) {
            if (this.allUsers.containsKey(userName)) {
                throw new SameUserNameException();
            }
        }
    }

    public void passwordChecker(String userName, String password) throws Exception {
        synchronized (this) {
            if (!this.allUsers.containsKey(userName)) {
                throw new UserNameNotFound();
            }
            if (!this.allUsers.get(userName).getPassword().passwordChecker(password)) {
                throw new PasswordException("the password is wrong.");
            }
        }
    }

    public void finish(boolean write) throws IOException {
        synchronized (this) {
            try {
                if (write) {
                    this.endServer();
                } else {
                    this.socketServer.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Files.deleteIfExists(Paths.get(SOURCE_PATH));
            }
        }
    }


    public static void main(String[] args) throws Exception {
        Server socketServer = new Server();
        if (socketServer.getHasMade()) {
            System.out.println("this is ready\n");
        }
        BufferedReader scanner = new BufferedReader(new InputStreamReader(System.in));
        String message;
        while (true) {
            if (scanner.ready()) {
                message = scanner.readLine();
                if (message.equals("exit")) {
                    socketServer.endServer();
                    break;
                }
                if (message.equals("users")) {
                    System.out.println(socketServer.allUsers.keySet() + "\n");
                }
                if (socketServer.allUsers.containsKey(message)) {
                    User user = socketServer.allUsers.get(message);
                    System.out.println("follower: " + user.follower);
                    System.out.println("following: " + user.following);
                    System.out.println("blockedBy: " + user.blockedBy);
                    System.out.println("isBlock: " + user.isBlock);
                    System.out.println("isMute: " + user.isMute + "\n");
                }
            }
        }
    }


    class Client extends Thread {
        public Server clientServer = getInstance();
        public Socket socket;
        public User user = null;
        public long id_user;
        public ConcurrentHashMap<String, Object> map;
        public ObjectOutputStream objectOutputStream;
        public ObjectInputStream objectInputStream;

        public Client(Socket socket) {
            this.socket = socket;
            try {
                objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                objectInputStream = new ObjectInputStream(socket.getInputStream());
                map = new ConcurrentHashMap<>();
                map.put("out", objectOutputStream);
                map.put("in", objectInputStream);
            } catch (Exception e) {
                if (e instanceof SocketException) return;
                e.printStackTrace();
            }
            this.start();
            System.out.println(Tools.getTime() + " start()");
        }


        @Override
        public void run() {
            Handler handler;
            Object object;
            while (true) {
                try {
                    if ((object = objectInputStream.readObject()) != null) {
                        handler = (Handler) object;
                        clientServer = getInstance();
                        if (handler instanceof UpToDate){
                            System.out.print("");
                        }
                        if (handler instanceof LogIn && handler.getAddOrRemove() == null) {//TODO check this condition ??
                            this.user = clientServer.allUsers.get(handler.getUser());
                            onlineUsers.add(user.getUserName());
                            Tools.Outer(this.objectOutputStream, clientServer.allUsers.get(handler.getUser()));
                            synchronized (clientServer) {
                                clientServer.connections.put(handler.getUser(), map);
                            }
                            System.out.println(user.getUserName() + " is online.");
                            continue;
                        }
                        synchronized (clientServer){
                            handler.work(getInstance());
                            if (handler instanceof SignIn) {
                                this.user = clientServer.allUsers.get(addingUserName);
                                onlineUsers.add(user.getUserName());
                                synchronized (clientServer.connections) {
                                    clientServer.connections.put(addingUserName, map);
                                }
                                Tools.Outer(Tools.getOutStream(clientServer, addingUserName), new SignIn(addingUserName, clientServer.userID.intValue()));
                                System.out.println(addingUserName + " has made");
                            }
                        }
                        if ((handler instanceof ExitAndSave)) {
                            try {
                                System.out.println(user.getUserName() + " left at " + new DATE());
                                this.map = null;
                                this.objectInputStream.close();
                                this.objectOutputStream.close();
                                this.socket.close();
                                break;
                            } catch (Exception e) {
                                e.printStackTrace();
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    new Disconnect(user.getUserName()).handle(clientServer);
                    try {
                        System.out.print(user.getUserName() + " disconnect at ");Tools.Time();
                        this.map = null;
                        this.objectInputStream.close();
                        this.objectOutputStream.close();
                        this.socket.close();
                        break;
                    } catch (Exception exception) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        }

        @Override
        public void interrupt() {
            super.interrupt();
            try {
                this.socket.close();
            } catch (IOException e) {
//            e.printStackTrace();(ignore)
            }
        }
    }
}
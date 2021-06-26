package SBUGRAM.Scenes;

import SBUGRAM.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import sample.Main;


import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class Viewer extends Node implements Serializable {
    private static Main main;
    private static User user = null;
    private static Server server = null;
    private static LinkedList<Viewer> viewerLinkedList = new LinkedList<>();
    private Viewer lastViewer = null;
    private Pane pane = null;
    private Stage stage = null;
    private Scene scene = null;
    private Scene lastScene = null;
    private List<Object> objects = null;

    static {
        long serial = Comment.getSerialVersionUID();
        serial = Post.getSerialVersionUID();
        serial = DATE.getSerialVersionUID();
        serial = Password.getSerialVersionUID();
    }

    public static void setMain(Main main) {
        Viewer.main = main;
    }

    public static Main getMain() {
        return main;
    }

    public static void setUser(User user) {
        Viewer.user = user;
    }

    public static void setServer(Server server) {
        Viewer.server = server;
    }

    public Viewer(Stage stage, Viewer lastViewer) {
        this.stage = stage;
        this.lastViewer = lastViewer;
        this.lastScene = stage.getScene();
    }

    public Viewer(Stage stage, Viewer lastViewer, Object... objects) {
        this(stage, lastViewer);
        this.objects = Arrays.asList(objects);
    }

    public void initialization(Stage stage, double x, double y) {
        //nothing
    }

    public Viewer getLastViewer() {
        return lastViewer;
    }

    public void refresh() {
        if (this instanceof MainPage) {
            Viewer.user.server = null;
            Viewer.user.upToDate();
            Tools.sleep(100);
            ((MainPage) this).server = Viewer.user.server;
            setServer(Viewer.user.server);// up tp date server
            if (getServer() == null/* || getUser() == null*/) {
                try {
                    throw new Exception();
                } catch (Exception e){
                    e.printStackTrace();
                }
                Tools.Time();
                System.out.println(Viewer.getUser().getUserName());
                System.exit(4545120);
            }
        } else {
            viewerLinkedList.addLast(this);
            this.lastViewer.refresh();
            viewerLinkedList.pop();
        }
        if (this instanceof BlockedList) {
            try {
                ((BlockedList) this).users = new ArrayList<>(Viewer.getServer().allUsers.get(((BlockedList) this).user.getUserName()).getIsBlock());
                if (((BlockedList) this).users == null) throw new Exception();
            } catch (Exception e) {
                e.printStackTrace();
                this.back();
            }
        }
        if (this instanceof MutedList) {
            try {
                ((MutedList) this).users = new ArrayList<>(Viewer.getServer().allUsers.get(((MutedList) this).user.getUserName()).getIsBlock());
                if (((MutedList) this).users == null) throw new Exception();
            } catch (Exception e) {
                e.printStackTrace();
                this.back();
            }
        }
        if (this instanceof Chat) {
            try {
                ((Chat) this).user = Viewer.getServer().allUsers.get(((Chat) this).user.getUserName());
                if (((Chat) this).user == null) throw new Exception();
            } catch (Exception e) {
                e.printStackTrace();
                this.back();
            }
        }
        if (this instanceof Search) {
            try {
                ((Search) this).server = Viewer.getServer();
                if (((Search) this).server == null) throw new Exception();
            } catch (Exception e) {
                e.printStackTrace();
                this.back();
            }
        }
        if (this instanceof ProfileViewer) {
            try {
                ((ProfileViewer) this).user = Viewer.getServer().allUsers.get(((ProfileViewer) this).user.getUserName());
                if (((ProfileViewer) this).user == null) throw new Exception();
            } catch (Exception e) {
                e.printStackTrace();
                this.back();
            }
        }
        if (this instanceof PostViewer) {
            try {
                if (this.getLastViewer() instanceof ProfileViewer){
                    Post post = ((PostViewer) this).post;
                    ((PostViewer) this).post = ((ProfileViewer) this.lastViewer).user.getPosts().stream()
                            .filter(post::equals)
                            .collect(Collectors.toList()).get(0);
                    if (((PostViewer) this).post == null) throw new Exception();
                }
            } catch (Exception e) {
                e.printStackTrace();
                this.back();
            }
        }
        if (this instanceof CommentListViewer) {
            try {
                ((CommentListViewer) this).post = ((PostViewer) this.lastViewer).post;
                ((CommentListViewer) this).comments = ((PostViewer) this.lastViewer).post.getComments();
                if (((CommentListViewer) this).post == null || ((CommentListViewer) this).comments == null) throw new Exception();
            } catch (Exception e) {
                e.printStackTrace();
                this.back();
            }
        }
        if (this instanceof CommentViewer) {
            try {
                Comment comment = ((CommentViewer) this).comment;
                ((CommentViewer) this).comment = ((CommentListViewer) this.getLastViewer()).comments.stream()
                        .filter(comment1 -> comment1.equals(comment))
                        .collect(Collectors.toList()).get(0);
                if (((CommentViewer) this).comment == null) throw new Exception();
            } catch (Exception e) {
                e.printStackTrace();
                this.back();
            }

            //TODO check this new part
            this.getLastViewer().initialization(this.getStage(), this.getStage().getScene().getWidth(), this.getStage().getScene().getHeight());
            this.getLastViewer().show();
            return;
        }


        this.initialization(this.getStage(), this.getStage().getScene().getWidth(), this.getStage().getScene().getHeight());
        if (viewerLinkedList.isEmpty() && !(this instanceof Chat)) {
            // TODO put it here or upper line
            this.show();
        }
    }


    public void show() {
        if (scene == null) {
            System.exit(-45666);
        }
        stage.setScene(scene);
        stage.show();
    }

    public void back() {
        if (lastViewer == null) {
            System.out.println(this + " doesnt have lastViewer.");
            stage.setScene(lastScene);
        }
        else {
            if (lastViewer instanceof PostViewer || lastViewer instanceof PostListViewer) {
                lastViewer.lastViewer.initialization(stage, stage.getScene().getWidth(), stage.getScene().getHeight());
                stage.setScene(lastViewer.lastViewer.getMadeScene());
                stage.show();
                return;
            }
            lastViewer.initialization(stage, stage.getScene().getWidth(), stage.getScene().getHeight());
            stage.setScene(lastViewer.getMadeScene());
        }
        stage.show();
    }

    public void setPane(Pane pane) {
        this.pane = pane;
    }

    public Pane getMadePane() {
        return pane;
    }

    public Stage getStage() {
        return stage;
    }


    public Scene getMadeScene() {
        return scene;
    }

    public static User getUser() {
        try {
            if (user == null) throw new Exception("user not initialize!!!!");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-455422212);
        }
        return user;
    }

    public static Server getServer() {
        return server;
    }

    public Scene getLastScene() {
        return lastScene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
        if (scene == null) {
            try {
                throw new Exception("scene is null");
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.exit(-45666);
        }
    }

    public List<Object> getObjects() {
        if (objects == null) return new ArrayList<>();
        return objects;
    }

}

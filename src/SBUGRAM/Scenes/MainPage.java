package SBUGRAM.Scenes;

import SBUGRAM.Post;
import SBUGRAM.Server;
import SBUGRAM.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.*;
import java.util.stream.Collectors;


public class MainPage extends Viewer {
    public Server server;
    public User user;

    public MainPage(Stage stage, Viewer lastViewer, User user, Server server) {
        super(stage, lastViewer);
        setServer(server);
        this.server = getServer();
        this.user = Viewer.getUser();
        if (!user.equals(this.user)) {
            System.exit(-659432310);
        }
        Scene s = getLastScene();
        initialization(stage, s.getWidth(), s.getHeight());
    }

    @Override
    public void initialization(Stage stage, double x, double y) {
        HBox labelBox = new HBox();
        labelBox.setAlignment(Pos.CENTER_LEFT);
        Label SBU_GRAM = new Label("SBU_GRAM");
        SBU_GRAM.setOnMouseClicked(actionEvent -> {
            new SBU_GRAM(stage, this).show();
        });
        labelBox.getChildren().add(SBU_GRAM);

        ScrollPane followingScroll = new ScrollPane();
        HBox followings = new HBox(10);
        followings.setAlignment(Pos.CENTER);
        if (user.getFollowing().size() != 0) {
            List<Pane> panes = user.getFollowing().stream()
                    .filter(following -> !Viewer.getUser().isBlock.contains(following))
                    .map(following -> new ProfileLabel(stage, this, server.allUsers.get(following)).getMadePane())
                    .collect(Collectors.toList());
            followings.getChildren().addAll(panes);
        } else {
            Label empty = new Label("no following yet");
            empty.setAlignment(Pos.CENTER);
            empty.setPrefHeight(60);
            followings.getChildren().add(empty);
        }
        followings.setAlignment(Pos.CENTER);
        followingScroll.setContent(followings);


        List<Post> postList = server.allUsers.keySet().stream()
                .filter(key -> user.getFollowing().contains(key))
                .filter(key -> !user.getIsBlock().contains(key))
                .filter(key -> !server.allUsers.get(key).getIsBlock().contains(user.getUserName()))
                .map(key -> server.allUsers.get(key))
                .flatMap(user1 -> user1.getPosts().stream())
                .collect(Collectors.toList());

        postList = postList.stream().sorted(Post::compareTo).collect(Collectors.toList());

        ScrollPane postScroll = new ScrollPane();
        postScroll.setPadding(new Insets(15));
        VBox posts = new VBox(15);
        posts.setAlignment(Pos.CENTER_LEFT);
        if (!postList.isEmpty()) {
            List<Pane> panes = postList.stream()
                    .map(post -> new PostViewer(stage, this, post).getMadePane())
                    .collect(Collectors.toList());
            posts.getChildren().addAll(panes);
        } else {
            Label empty = new Label("no post yet");
            empty.setAlignment(Pos.CENTER);
            empty.setPrefSize(330, 200);
            posts.getChildren().add(empty);
        }
        postScroll.setContent(posts);


        HBox bottom = new HBox(50);
        bottom.setAlignment(Pos.CENTER);
        Button exit = new Button("exit");
        exit.setOnAction(actionEvent -> {
            stage.close();
        });
        Button search = new Button("search");
        search.setOnAction(actionEvent -> {
            new Search(stage, this, server).show();
        });
        Button refresh = new Button("refresh");
        refresh.setOnAction(actionEvent -> {
            this.refresh();
        });
        Button post = new Button("new post");
        post.setOnAction(actionEvent -> {
            new PostMaker(user, stage, this).show();
        });
        Button profile = new Button("profile");
        profile.setOnAction(actionEvent -> {
            new ProfileViewer(stage, this, user).show();
        });
        bottom.getChildren().addAll(exit, search, refresh, post, profile);


        VBox all = new VBox(10);
        all.setAlignment(Pos.CENTER);
        all.getChildren().addAll(labelBox, followingScroll, postScroll, bottom);
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.getChildren().add(all);
        setPane(gridPane);
        setScene(new Scene(gridPane, x, y));
    }
}

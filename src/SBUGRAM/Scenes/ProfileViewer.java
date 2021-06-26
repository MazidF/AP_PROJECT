package SBUGRAM.Scenes;

import SBUGRAM.Messages.Reset;
import SBUGRAM.Tools;
import SBUGRAM.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.Main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProfileViewer  extends Viewer{
    public User user;
    private Image image;


    public ProfileViewer(Stage stage, Viewer lastViewer, User user) {
        super(stage, lastViewer, user);
        this.user = user;
        Scene s = getLastScene();
        initialization(stage, s.getWidth(), s.getHeight());
    }

    @Override
    public void initialization(Stage stage, double x, double y) {

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(15, 15, 15, 15));


        VBox mainBox = new VBox(10);
        mainBox.setAlignment(Pos.CENTER);

        VBox backBox = new VBox();
        Button back = new Button("back");
        back.setOnAction(actionEvent -> {
            back();
        });
        backBox.setAlignment(Pos.CENTER_LEFT);
        backBox.getChildren().add(back);


        HBox userBox = new HBox(350);
        userBox.setAlignment(Pos.CENTER_LEFT);
        Label userName = new Label(user.getUserName());
        userName.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));


        MenuButton options = new MenuButton("Options");

        MenuItem block = new CheckMenuItem("Block");
        MenuItem mute = new CheckMenuItem("Mute");
        MenuItem chat = new CheckMenuItem("Chat");
        MenuItem followRequest = null;
        MenuItem logOUt = null;
        MenuItem setting = null;
        MenuItem deleteAccount = null;

        if (user.getUserName().equals(Viewer.getUser().getUserName())) {
            followRequest = new CheckMenuItem("Follow Requests");
            logOUt = new CheckMenuItem("Log OUt");
            setting = new CheckMenuItem("Setting");
            deleteAccount = new CheckMenuItem("delete account");

            deleteAccount.setOnAction(actionEvent -> {
                Reset reset = new Reset(Viewer.getUser().getUserName());
                Viewer.getUser().Outer(reset);
                Viewer.getUser().saveAndExit();
                Tools.sleep(100);
                try {
                    getMain().reStart(stage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            followRequest.setText("Follow Requests");
            followRequest.setOnAction(actionEvent -> {
                new FollowRequestList(stage, this, Viewer.getUser()).show();
            });

            logOUt.setText("Log Out");
            logOUt.setOnAction(actionEvent -> {
                Viewer.getUser().saveAndExit();
                Tools.sleep(100);
                try {
                    getMain().reStart(stage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            setting.setText("Setting");
            setting.setOnAction(actionEvent -> {
                new Setting(stage, this, Viewer.getUser()).show();
            });

            block.setText("Blocked List");
            block.setOnAction(actionEvent -> {
                new BlockedList(stage, this, user).show();
            });

            mute.setText("Muted List");
            mute.setOnAction(actionEvent -> {
                new MutedList(stage, this, user).show();
            });

            chat.setText("Chat List");
            chat.setOnAction(actionEvent -> {
                List<String> users = new ArrayList<>(user.getChats().keySet());
                new ChatList(stage, this, users).show();
            });

        } else {

            block.setText("Block");
            if (!Viewer.getUser().isBlock.contains(user.getUserName())) {
                block.setOnAction(actionEvent -> {
                    Viewer.getUser().Block(user.getUserName());
                    refresh();
                    back();
                });
            } else {
                block.setStyle("-fx-text-fill: green; -fx-font-size: 12px;");
                block.setOnAction(actionEvent -> {
                    Viewer.getUser().UnBlock(user.getUserName());
                    refresh();
                });
            }

            mute.setText("Mute");
                if (!Viewer.getUser().isMute.contains(user.getUserName())) {
                    mute.setOnAction(actionEvent -> {
                        Viewer.getUser().Mute(user.getUserName());
                        refresh();
                    });
                } else {
                    mute.setStyle("-fx-text-fill: green; -fx-font-size: 12px;");
                    mute.setOnAction(actionEvent -> {
                        Viewer.getUser().UnMute(user.getUserName());
                        refresh();
                    });
                }


            chat.setText("Chat");
            if (!Viewer.getUser().isBlock.contains(this.user.getUserName())){
                chat.setOnAction(actionEvent -> {
                    refresh();
                    new Chat(stage, this, user).show();
                });
            } else {
                chat.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
            }
        }
        if (followRequest == null) {
            options.getItems().addAll(block, mute, chat);
        } else {
            options.getItems().addAll(block, mute, chat, followRequest, logOUt, setting, deleteAccount);
        }

        userBox.getChildren().addAll(userName, options);



        ImageView imageView = new ImageView();
        if (user.getImage() != null && !Viewer.getUser().isBlock.contains(this.user.getUserName())) {
            image = Tools.byteToImage(user.getImage());
            imageView.setImage(image);
        } else {
            try {
                image = new Image(new FileInputStream("C:\\Users\\vcc\\IdeaProjects\\first fx\\src\\SOURCE\\profileImage.png"));
                imageView.setImage(image);
            } catch (FileNotFoundException e) {
                imageView.setVisible(false);
                imageView.setManaged(false);
            }
        }
        imageView.setOnMouseClicked(mouseEvent -> {
            Stage newStage = new Stage();
            VBox vBox = new VBox();
            ImageView view = new ImageView(image);
            vBox.setAlignment(Pos.CENTER);
            vBox.getChildren().add(view);
            view.setFitHeight(400);
            view.setFitWidth(400);
            view.maxHeight(400);
            view.maxWidth(400);
            newStage.setScene(new Scene(vBox, 500, 500));
            newStage.show();
        });
        imageView.setFitHeight(150);
        imageView.setFitWidth(150);
        imageView.maxHeight(150);
        imageView.maxWidth(150);
        gridPane.add(imageView, 0, 0, 3, 3);


        List<Label> labels  = new ArrayList<>();
        List<Text> texts  = new ArrayList<>();

        labels.add(new Label("Name:"));
        texts.add(new Text(user.getFirstName()));
        labels.add(new Label("Last Name:"));
        texts.add(new Text(user.getLastName()));
        labels.add(new Label("Birth Day:"));
        texts.add(new Text(user.getBirthDay().toString()));
        if (user.getPhoneNumber() != null) {
            labels.add(new Label("Phone:"));
            texts.add(new Text(user.getPhoneNumber()));
        }
        VBox labelsBox = new VBox(10);
        VBox textsBox = new VBox(10);
        labelsBox.getChildren().addAll(labels);
        textsBox.getChildren().addAll(texts);

        List<Label> nullLabel = new ArrayList<>();
        for (int i = 0; i < labels.size(); i++) {
            nullLabel.add(new Label("  "));
        }
        VBox nullBox = new VBox(10);
        nullBox.getChildren().addAll(nullLabel);

        HBox vBoxes = new HBox(10);
        vBoxes.getChildren().addAll(nullBox, labelsBox, textsBox);
        gridPane.add(vBoxes, 10, 0, 12, 3);
//        gridPane.add(labelsBox, 4, 0, 5, 3);
//        gridPane.add(textsBox, 5, 0, 6, 3);

        HBox AllFollow = new HBox(50);
        AllFollow.setAlignment(Pos.CENTER);
        Button follow = new Button();
        if (this.user.getFollower().contains(Viewer.getUser().getUserName())) {
            follow.setText("Following");
        }
        else if (this.user.getFollowRequest().contains(Viewer.getUser().getUserName())) {
            follow.setText("Requested");
        } else {
            follow.setText("Follow");
        }
        follow.setOnAction(actionEvent -> {
            if (this.user.getFollower().contains(Viewer.getUser().getUserName())) {
                Viewer.getUser().UnFollowing(this.user.getUserName());
            }
            else if (this.user.getFollowRequest().contains(Viewer.getUser().getUserName())) {
                // nothing it has sent. online
            } else {
                Viewer.getUser().requestFollowing(this.user.getUserName());
            }
            Tools.sleep(100);
            this.refresh();
        });
        HBox followInformation = new HBox(20);
        followInformation.setAlignment(Pos.CENTER);
        VBox followers = new VBox(10);
        VBox followings = new VBox(10);
        Label followersLabel = new Label("Followers");
        Label followingsLabel = new Label("Followings");
        Text numberOfFollower = new Text(Integer.toString(user.getFollower().size()));
        Text numberOfFollowing = new Text(Integer.toString(user.getFollowing().size()));

        followers.getChildren().addAll(followersLabel, numberOfFollower);
        followings.getChildren().addAll(followingsLabel, numberOfFollowing);
        followInformation.getChildren().addAll(followers, followings);


        if (this.user.equals(Viewer.getUser())) {
            AllFollow.getChildren().addAll(followInformation);
        } else {
            AllFollow.getChildren().addAll(follow, followInformation);
        }
        gridPane.add(AllFollow, 0, 4, 12, 6);

        HBox refreshBox = new HBox();
        refreshBox.setAlignment(Pos.CENTER);
        Button refresh = new Button("refresh");
        refresh.setOnAction(actionEvent -> {
            this.refresh();
        });
        refreshBox.getChildren().add(refresh);


        ScrollPane scrollPane = new ScrollPane();
        VBox posts = new VBox(15);
        posts.setAlignment(Pos.CENTER);
        if (user.getPosts().size() != 0) {
            List<Pane> panes = this.user.getPosts().stream()
//                    .filter(post -> !Viewer.getUser().isMute.contains(post.getUser()) && !Viewer.getUser().isBlock.contains(post.getUser()))
                    .map(post -> new PostViewer(stage, this, post).getMadePane())
                    .collect(Collectors.toList());
            if (user.getReposts() != null) {
                panes.addAll(this.user.getReposts().stream()
//                        .filter(repost -> !Viewer.getUser().isMute.contains(repost.post.getUser()) && !Viewer.getUser().isBlock.contains(repost.post.getUser()))
                        .map(rePost -> rePost.getPost(Viewer.getServer()))
                        .map(post -> new PostViewer(stage, this, post).getMadePane())
                        .collect(Collectors.toList()));
            }
            posts.getChildren().addAll(panes);
        } else if (user.getReposts().size() != 0) {
            List<Pane> panes = this.user.getReposts().stream()
//                    .filter(repost -> !Viewer.getUser().isMute.contains(repost.post.getUser()) && !Viewer.getUser().isBlock.contains(repost.post.getUser()))
                    .map(rePost -> rePost.getPost(Viewer.getServer()))
                    .map(post -> new PostViewer(stage, this, post).getMadePane())
                    .collect(Collectors.toList());
            posts.getChildren().addAll(panes);
        } else {
            Label empty = new Label("no post yet");
            empty.setAlignment(Pos.CENTER);
            empty.setPrefSize(330, 200);
            posts.getChildren().add(empty);
        }
        scrollPane.setPadding(new Insets(20));
        scrollPane.setContent(posts);

        VBox main = new VBox(15);
        main.setAlignment(Pos.CENTER);
        main.getChildren().addAll(refreshBox, scrollPane);

        mainBox.getChildren().addAll(new Label(), backBox, userBox, gridPane, main, new Label());
        GridPane newPane = new GridPane();
        newPane.setAlignment(Pos.CENTER);
        newPane.getChildren().add(mainBox);
        setPane(newPane);
        setScene(new Scene(newPane, x, y));

    }


    public String getUserName() {
        return user.getUserName();
    }
}

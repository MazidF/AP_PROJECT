package SBUGRAM.Scenes;

import SBUGRAM.ChatMessage;
import SBUGRAM.User;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Chat extends Viewer {
    public User user;
    private String messageTyping = "";
    private List<ChatMessage> chatMessagesUser;
    private List<ChatMessage> chatMessagesMainUser;
    private Timeline timeline;

    public Chat(Stage stage, Viewer lastViewer, User user) {
        super(stage, lastViewer, user);
        this.user = user;
        this.chatMessagesUser = user.getMessages(Viewer.getUser().getUserName());
        this.chatMessagesMainUser = Viewer.getUser().getMessages(user.getUserName());
        Scene scene = stage.getScene();
        initialization(stage, scene.getWidth(), scene.getHeight());
    }

    @Override
    public void initialization(Stage stage, double x, double y) {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(20, 20, 20, 20));
        gridPane.setMinSize(600, 450);

        HBox backBox = new HBox(300);
        backBox.setAlignment(Pos.CENTER_LEFT);
        Button back = new Button("back");
        back.setOnAction(actionEvent -> {
            this.timeline.stop();
            this.timeline = null;
            back();
            System.gc();
        });
        Button refresh = new Button("refresh");
        refresh.setOnAction(actionEvent -> {
            refresh();
            this.show();
        });
        backBox.getChildren().addAll(back, refresh);



        HBox top = new HBox(30);
        top.setAlignment(Pos.CENTER_LEFT);
        Label userName = new Label(user.getUserName());
        userName.setFont(Font.font("Tahoma", FontWeight.SEMI_BOLD, 15));
        userName.setOnMouseClicked(mouseEvent -> {
            new ProfileViewer(stage, this.getLastViewer(), user).show();
        });
        Label isOnline = new Label();
        if (getServer().onlineUsers.contains(user.getUserName())) {
            isOnline.setText("ONLINE");
            isOnline.setTextFill(Color.GREEN);
            if (this.user.getChats().get(Viewer.getUser().getUserName()) != null) {
                timeline = new Timeline(new KeyFrame(Duration.seconds(0.5), actionEvent -> {
                    int chats = this.user.getChats().get(Viewer.getUser().getUserName()).size();
                    refresh();
                    if (chats != this.user.getChats().get(Viewer.getUser().getUserName()).size()) {
                        this.show();
                    }
                }));
                timeline.setCycleCount(1);
                timeline.play();
            }
        } else {
            isOnline.setText("OFFLINE");
            isOnline.setTextFill(Color.RED);
        }
        top.getChildren().addAll(userName, isOnline);


        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setMinSize(400, 600);
//        scrollPane.setPadding(new Insets(30, 30, 30, 30));
        VBox messages = new VBox(15);
        if (user.chats.containsKey(Viewer.getUser().getUserName()) || Viewer.getUser().chats.containsKey(user.getUserName())) {
            List<ChatMessage> messageList = new ArrayList<>();
            if (Viewer.getUser().getMessages(user.getUserName()) != null)
                messageList.addAll(Viewer.getUser().getMessages(user.getUserName()));
            if (user.getMessages(Viewer.getUser().getUserName()) != null)
                messageList.addAll(user.getMessages(Viewer.getUser().getUserName()));
            messageList = messageList.stream().sorted(ChatMessage::compareTo).collect(Collectors.toList());
            List<Pane> list = messageList.stream().map(ChatMessage::getPane).collect(Collectors.toList());
            list.add(0, new VBox());
            list.add(new VBox());
            messages.getChildren().addAll(list);
        } else {
            Label noChat = new Label("start chat with ".concat(user.getUserName()));
            noChat.setPrefSize(300, 250);
            noChat.setFont(Font.font("Tahoma", FontWeight.SEMI_BOLD, 15));
            noChat.setAlignment(Pos.CENTER);
            messages.getChildren().add(noChat);
        }
        scrollPane.setContent(messages);

        TextArea write = new TextArea(messageTyping);
/*        if (!this.messageTyping.equals("")) {
            write.setText(messageTyping);
            messageTyping = "";
        }*/
        write.setPromptText("Enter your message");
        write.setWrapText(true);
        write.setMaxWidth(300);
        write.setMinWidth(300);
        write.setMaxHeight(13);

        write.textProperty().addListener((observable, oldValue, newValue) -> {
            messageTyping = newValue;
            System.out.println(messageTyping);
        });


        Button send = new Button("send");
        send.setPrefHeight(35);
        send.setOnAction(actionEvent -> {
            String message = write.getText();
            this.messageTyping = "";
            ChatMessage chatMessage = new ChatMessage(Viewer.getUser().getUserName(), message, this);
            Viewer.getUser().AddChat(user.getUserName(), chatMessage);
            refresh();
            this.show();
        });

        HBox writingBox = new HBox(10);
        writingBox.getChildren().addAll(write, send);

        VBox all = new VBox(15);
        all.setAlignment(Pos.CENTER);
        all.getChildren().addAll(backBox, top, scrollPane, writingBox);

        gridPane.getChildren().add(all);
        setPane(gridPane);
        setScene(new Scene(gridPane, x, y));
    }

}

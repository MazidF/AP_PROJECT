package SBUGRAM.Scenes;

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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;

public class ChatList extends Viewer {
    List<String> users;

    public ChatList(Stage stage, Viewer lastViewer, List<String> users) {
        super(stage, lastViewer, users);
        this.users = users;
        Scene s = getLastScene();
        initialization(stage, s.getWidth(), s.getHeight());
    }

    @Override
    public void initialization(Stage stage, double x, double y) {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);

        Button findOne = new Button("find one");
        findOne.setOnAction(actionEvent -> {
            refresh();
            new Search(stage, this, getServer()).show();
        });
        HBox search = new HBox();
        search.setAlignment(Pos.CENTER);
        search.getChildren().add(findOne);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefSize(300, 250);
        VBox ChatList = new VBox(15);
        ChatList.setAlignment(Pos.CENTER);
        if (users.size() != 0) {
            List<Pane> panes = this.users.stream()
                    .map(userName -> {
                        Button label = new Button(userName);
                        label.setMinWidth(100);
                        label.setPadding(new Insets(10, 10, 10, 10));
                        label.setAlignment(Pos.CENTER);
                        label.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
                        label.setOnMouseClicked(mouseEvent -> {
                            new Chat(stage, this, getServer().allUsers.get(userName)).show();
                        });
                        VBox box = new VBox();
                        box.setAlignment(Pos.CENTER);
                        box.getChildren().add(label);
                        return box;
                    })
                    .collect(Collectors.toList());
            ChatList.getChildren().addAll(panes);
        } else {
            Label noRequest = new Label("no chat yet.");
            noRequest.setPrefSize(300, 250);
            noRequest.setFont(Font.font("Tahoma", FontWeight.SEMI_BOLD, 15));
            noRequest.setAlignment(Pos.CENTER);
            ChatList.getChildren().add(noRequest);
        }
        scrollPane.setContent(ChatList);



        Label chats = new Label("Chat list");
        chats.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));

        Button back = new Button("back");
        back.setOnAction(e -> {
            back();
        });

        VBox top = new VBox(10);
        HBox chatsBox = new HBox();
        chatsBox.getChildren().add(chats);
        chatsBox.setAlignment(Pos.CENTER_RIGHT);

        HBox backBox = new HBox();
        backBox.getChildren().add(back);
        backBox.setAlignment(Pos.CENTER_LEFT);

        top.getChildren().addAll(backBox, chatsBox);
        top.setAlignment(Pos.CENTER);

        VBox all = new VBox(30);
        all.getChildren().addAll(top, search, scrollPane, new Label());

        gridPane.getChildren().add(all);
        setPane(gridPane);
        setScene(new Scene(gridPane, x, y));
    }
}
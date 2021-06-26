package SBUGRAM.Scenes;

import SBUGRAM.User;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MutedList extends Viewer{
    public List<String> users;
    public User user;

    public MutedList(Stage stage, Viewer lastViewer, User user) {
        super(stage, lastViewer, user.getIsMute());
        this.user = user;
        this.users = new ArrayList<>(user.getIsMute());
        Scene s = getLastScene();
        initialization(stage, s.getWidth(), s.getHeight());
    }

    @Override
    public void initialization(Stage stage, double x, double y) {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);

        ScrollPane scrollPane = new ScrollPane();
        VBox FollowerRequestList = new VBox(15);
        if (users.size() != 0) {
            List<Pane> panes = this.users.stream()
                    .map(userName -> new MuteViewer(stage, this, getServer().allUsers.get(userName)).getMadePane())
                    .collect(Collectors.toList());
            FollowerRequestList.getChildren().addAll(panes);
        } else {
            Label noRequest = new Label("no body muted yet.");
            noRequest.setPrefSize(300, 250);
            noRequest.setFont(Font.font("Tahoma", FontWeight.SEMI_BOLD, 15));
            noRequest.setAlignment(Pos.CENTER);
            FollowerRequestList.getChildren().add(noRequest);
        }
        scrollPane.setContent(FollowerRequestList);



        Label blocks = new Label("Muted list");
        blocks.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));

        Button back = new Button("back");
        back.setOnAction(e -> {
            back();
        });

        VBox top = new VBox(10);
        HBox blocksBox = new HBox();
        blocksBox.getChildren().add(blocks);
        blocksBox.setAlignment(Pos.CENTER_RIGHT);

        HBox backBox = new HBox();
        backBox.getChildren().add(back);
        backBox.setAlignment(Pos.CENTER_LEFT);

        top.getChildren().addAll(backBox, blocksBox);
        top.setAlignment(Pos.CENTER);

        VBox all = new VBox(30);
        all.getChildren().addAll(top, scrollPane, new Label());

        gridPane.getChildren().add(all);
        setPane(gridPane);
        setScene(new Scene(gridPane, x, y));
    }
}

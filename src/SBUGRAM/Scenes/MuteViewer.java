package SBUGRAM.Scenes;

import SBUGRAM.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class MuteViewer extends Viewer {
    public User user;

    public MuteViewer(Stage stage, Viewer lastViewer, Object... objects) {
        super(stage, lastViewer, objects);
        this.user = user;
        Scene s = getLastScene();
        initialization(stage, s.getWidth(), s.getHeight());
    }

    @Override
    public void initialization(Stage stage, double x, double y) {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(15, 15, 15, 15));
        gridPane.setAlignment(Pos.CENTER);

        Label userName = new Label(user.getUserName());
        userName.setFont(Font.font("Tahoma", FontWeight.BOLD, 10));
        userName.setOnMouseClicked(mouseEvent -> {
            //TODO last view set this.getLastViewer()
            new ProfileViewer(stage, this.getLastViewer(), user).show();
        });

        HBox buttons = new HBox(40);
        buttons.setAlignment(Pos.CENTER);

        Button unBlock = new Button("Un Mute");
        unBlock.setTextFill(Color.GREEN);
        unBlock.setOnAction(actionEvent -> {
            Viewer.getUser().UnMute(user.getUserName());
            refresh();
        });
        buttons.getChildren().addAll(userName, unBlock);


        gridPane.getChildren().add(buttons);


        setPane(gridPane);
        setScene(new Scene(gridPane, x, y));
    }
}

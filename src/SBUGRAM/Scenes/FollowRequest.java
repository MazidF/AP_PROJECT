package SBUGRAM.Scenes;

import SBUGRAM.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;



public class FollowRequest extends Viewer {
    public User user;

    public FollowRequest(Stage stage, Viewer lastViewer, User user) {
        super(stage, lastViewer, user);
        this.user = user;
        Scene s = getLastScene();
        initialization(stage, s.getWidth(), s.getHeight());
    }

    @Override
    public void initialization(Stage stage, double x, double y) {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(15, 15, 15, 15));
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setMinSize(300, 250);

        Label userName = new Label(user.getUserName());
        userName.setFont(Font.font("Tahoma", FontWeight.BOLD, 10));
        userName.setOnMouseClicked(mouseEvent -> {
            //TODO last view set this.getLastViewer()
            new ProfileViewer(stage, this.getLastViewer(), user).show();
        });

        TextField message = new TextField("wants to be one of your follower.");
        message.setMinWidth(150);
        message.setEditable(false);

        HBox buttons = new HBox(40);
        buttons.setAlignment(Pos.CENTER);

        Button accept = new Button("Accept");
        accept.setTextFill(Color.GREEN);
        accept.setOnAction(actionEvent -> {
            Viewer.getUser().AnswerToFollowerRequest(user.getUserName(), true);
            refresh();
            back();
        });
        Button reject = new Button("Reject");
        reject.setTextFill(Color.RED);
        reject.setOnAction(actionEvent -> {
            Viewer.getUser().AnswerToFollowerRequest(user.getUserName(), false);
            refresh();
            back();
        });
        buttons.getChildren().addAll(reject, accept);


        VBox all = new VBox(10);
        all.getChildren().addAll(userName, message, buttons);
        gridPane.getChildren().add(all);


        setPane(gridPane);
        setScene(new Scene(gridPane, x, y));
    }
}

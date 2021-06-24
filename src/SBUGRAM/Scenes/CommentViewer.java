package SBUGRAM.Scenes;

import SBUGRAM.Comment;
import SBUGRAM.Tools;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javafx.scene.control.*;

public class CommentViewer extends Viewer{
    public Comment comment;


    public CommentViewer(Stage stage, Viewer lastViewer, Object... objects) {
        super(stage, lastViewer, objects);
        this.comment = (Comment) objects[0];
        Scene s = getLastScene();
        initialization(stage, s.getWidth(), s.getHeight());
    }

    @Override
    public void initialization(Stage stage, double x, double y) {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setMinSize(400, 350);

        Label userName = new Label(comment.getUser());
        userName.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
        userName.setOnMouseClicked(r -> {

            //TODO check if it has problem :)
            new ProfileViewer(getStage(), this.getLastViewer(), Viewer.getServer().allUsers.get(comment.getUser())).show();
        });
        gridPane.add(userName, 0, 0);

        if (comment.getUser().equals(Viewer.getUser().getUserName())) {
            Button delete = new Button("delete");
            delete.setOnAction(actionEvent -> {
                Viewer.getUser().UNComment(((CommentListViewer) getLastViewer()).post, comment);
                getLastViewer().refresh();// TODO check this change
                back();
            });
            gridPane.add(delete, 2, 0);
        }


        TextArea message = new TextArea(comment.getMessage());
        message.setPrefSize(330, 200);
        message.setEditable(false);
        message.setWrapText(true);


        VBox date = new VBox();
        Text dateText = new Text(comment.getDate().toString());
        date.getChildren().add(dateText);
        date.setAlignment(Pos.CENTER_RIGHT);

        VBox all = new VBox(10);
        all.getChildren().addAll(message, date);
        gridPane.add(all, 0,1, 3, 5);

        setPane(gridPane);
        setScene(new Scene(gridPane, x, y));
    }
}

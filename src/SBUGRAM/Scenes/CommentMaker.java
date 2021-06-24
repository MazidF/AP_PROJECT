package SBUGRAM.Scenes;

import SBUGRAM.Comment;
import SBUGRAM.DATE;
import SBUGRAM.Post;
import SBUGRAM.User;
import com.sun.javafx.scene.control.LabeledText;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import jdk.dynalink.beans.StaticClass;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class CommentMaker extends Viewer {
    private final Post post;
    private Comment finalComment = null;
    private PostViewer postViewer;
    public CommentMaker(Post post, Viewer lastViewer, Stage stage) {
        super(stage, lastViewer);
        this.post = post;
        this.postViewer = (PostViewer) getLastViewer();
        Scene s = getLastScene();
        initialization(stage, s.getWidth(), s.getHeight());
    }

    @Override
    public void initialization(Stage stage, double x, double y) {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(25, 25, 25, 25));

        VBox Name = new VBox(10);
        VBox labels = new VBox(15);
        VBox texts = new VBox(10);

        Text name = new Text("COMMENT");
        name.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
        Text nuLL = new Text("");
        nuLL.setVisible(false);
        Name.getChildren().addAll(name, nuLL);
        Name.setAlignment(Pos.TOP_CENTER);
        gridPane.add(Name, 3, 0);

        Label userName = new Label("User Name: ");

        TextField userNameText = new TextField(Viewer.getUser().getUserName());
        userNameText.setEditable(false);// if s/he want to change it :)


        Label comment = new Label("Comment: ");

        labels.getChildren().addAll(userName, comment);
        gridPane.add(labels, 0, 3,1,4);

        TextArea commentText = new TextArea();
        commentText.setWrapText(true);
        commentText.setPrefSize(330, 200);


        HBox buttons = new HBox(10);
        Button cancel = new Button("cancel");
        cancel.setOnAction(e -> {
            back();
            //Todo search about this...
        });
        Button send = new Button("send");
        send.setOnAction(r -> {
            String COMMENT = commentText.getText();
            finalComment = new Comment(Viewer.getUser().getUserName(), COMMENT);
            Viewer.getUser().Comment(this.post, finalComment);
            getLastViewer().refresh();
            postViewer.initialization(stage, x, y);
            back();
        });


        buttons.getChildren().addAll(cancel, send);
        buttons.setAlignment(Pos.CENTER_LEFT);

        texts.getChildren().addAll(userNameText, commentText, buttons);
        gridPane.add(texts, 1, 3, 5, 9);

        setPane(gridPane);
        setScene(new Scene(gridPane, x, y));
    }


    public User getCommentUser() {
        return Viewer.getUser();
    }

}

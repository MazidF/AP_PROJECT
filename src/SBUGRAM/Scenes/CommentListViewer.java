package SBUGRAM.Scenes;

import SBUGRAM.Comment;
import SBUGRAM.Post;
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

public class CommentListViewer extends Viewer{
    public List<Comment> comments;
    public Post post;

    public CommentListViewer(Stage stage, Viewer lastViewer, Object... objects) {
        super(stage, lastViewer, objects);
        this.post = ((Post) objects[0]);
        this.comments = post.getComments();
        Scene s = getLastScene();
        initialization(stage, s.getWidth(), s.getHeight());
    }


    @Override
    public void initialization(Stage stage, double x, double y) {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);

        ScrollPane scrollPane = new ScrollPane();
        VBox commentViewerList = new VBox(15);
        if (comments.size() != 0) {
            List<Pane> panes = this.comments.stream()
                    .map(comment -> new CommentViewer(stage, this, comment).getMadePane())
                    .collect(Collectors.toList());
            commentViewerList.getChildren().addAll(panes);
        } else {
            Label noComment = new Label("no comment yet.");
            noComment.setPrefSize(300, 250);
            noComment.setFont(Font.font("Tahoma", FontWeight.SEMI_BOLD, 15));
            noComment.setAlignment(Pos.CENTER);
            commentViewerList.getChildren().add(noComment);
        }

        scrollPane.setContent(commentViewerList);


        Label comments = new Label("Comments");
        comments.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
        Button back = new Button("back");
        back.setOnAction(e -> {
            back();
        });
        VBox top = new VBox(10);
        HBox commentsBox = new HBox();
        commentsBox.getChildren().add(comments);
        commentsBox.setAlignment(Pos.CENTER_RIGHT);

        HBox backBox = new HBox();
        backBox.getChildren().add(back);
        backBox.setAlignment(Pos.CENTER_LEFT);

        top.getChildren().addAll(backBox, commentsBox);
        top.setAlignment(Pos.CENTER);

        VBox all = new VBox(30);
        all.getChildren().addAll(top, scrollPane, new Label());

        gridPane.getChildren().add(all);
        setPane(gridPane);
        setScene(new Scene(gridPane, x, y));
    }
}

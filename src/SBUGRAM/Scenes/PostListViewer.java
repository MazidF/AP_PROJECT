package SBUGRAM.Scenes;

import SBUGRAM.Post;
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

import java.util.List;
import java.util.stream.Collectors;

public class PostListViewer extends Viewer{
    public List<Post> posts;

    public PostListViewer(Stage stage, Viewer lastViewer, Object... objects) {
        super(stage, lastViewer, objects);
        posts = ((User) objects[0]).getPosts();
        Scene s = stage.getScene();
        initialization(stage, s.getWidth(), s.getHeight());
    }

    @Override
    public void initialization(Stage stage, double x, double y) {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);

        ScrollPane scrollPane = new ScrollPane();
        VBox postViewerList = new VBox(15);
        if (posts.size() != 0) {
            List<Pane> panes = this.posts.stream()
                    .map(post -> new PostViewer(stage, this, post).getMadePane())
                    .collect(Collectors.toList());
            postViewerList.getChildren().addAll(panes);
        } else {
            Label noPost = new Label("no post yet.");
            noPost.setPrefSize(300, 250);
            noPost.setFont(Font.font("Tahoma", FontWeight.SEMI_BOLD, 15));
            noPost.setAlignment(Pos.CENTER);
            postViewerList.getChildren().add(noPost);
        }

        scrollPane.setContent(postViewerList);


        Label posts = new Label("Posts");
        posts.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
/*        Button back = new Button("back");
        back.setOnAction(e -> {
            back();
        });*/
        HBox postsBox = new HBox();
        postsBox.getChildren().add(posts);
        postsBox.setAlignment(Pos.CENTER);

/*
        HBox backBox = new HBox();
        backBox.getChildren().add(back);
        backBox.setAlignment(Pos.CENTER_LEFT);
*/


        VBox top = new VBox(10);
        top.getChildren().addAll(/*backBox, */postsBox);
        top.setAlignment(Pos.CENTER);

        VBox all = new VBox(30);
        all.getChildren().addAll(top, scrollPane);

        gridPane.getChildren().add(all);
        setPane(gridPane);
        Scene scene = new Scene(gridPane, x, y);
        setScene(scene);
    }
}

package SBUGRAM.Scenes;

import SBUGRAM.*;
import SBUGRAM.Messages.AddOrRemove;
import SBUGRAM.Messages.Chat;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class PostViewer extends Viewer{
    public Post post;
    private boolean hasBack = false;
    private Boolean mainUser = false;

    public void setHasBack(boolean hasBack) {
        this.hasBack = hasBack;
    }

    public PostViewer(Stage stage, Viewer lastViewer, Object... objects) {
        super(stage, lastViewer, objects);
        this.post = (Post) objects[0];
        Scene s = getLastScene();
        initialization(stage, s.getWidth(), s.getHeight());
        post.check();
    }

    public PostViewer(Stage stage, Viewer lastViewer, boolean mainUser, Object... objects) {
        this(stage, lastViewer, objects);
    }

    @Override
    public void initialization(Stage stage, double x, double y) {
        GridPane gridPane = new GridPane ();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setMaxSize(400, 350);

        HBox top = new HBox(300);
        top.setAlignment(Pos.CENTER_LEFT);

        Label userName = new Label(post.getUser());
        userName.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
        userName.setOnMouseClicked(r -> {
            new ProfileViewer(getStage(), this.getLastViewer(), Viewer.getServer().allUsers.get(post.getUser())).show();
        });

        if (post.getUser().equals(Viewer.getUser().getUserName())) {
            Button delete = new Button("delete");
            delete.setOnAction(actionEvent -> {
                Viewer.getUser().RemovePost(this.post);
/*                SBUGRAM.Messages.Post post = new SBUGRAM.Messages.Post(Viewer.getUser().getUserName(), this.post);
                post.setAddOrRemove(AddOrRemove.REMOVE);
                Tools.viewerOut(post);*/
                refresh();
                back();
            });
            top.getChildren().addAll(userName, delete);
        } else {
            top.getChildren().add(userName);
        }
        gridPane.add(top, 0, 0, 3, 1);


        TextField title = new TextField(post.getTitle());
        title.setEditable(false);

        TextArea message = new TextArea(post.getMessage());
        message.setPrefSize(330, 200);
        message.setEditable(false);
        message.setWrapText(true);


        Button comment = new Button("Comment");
        comment.setOnAction(r -> {
            new CommentMaker(post, this, stage).show();
        });


        int likes = (int) post.getLikes();
        Label like = new Label("Like : ".concat(String.valueOf(likes)));
        if (post.getLike().contains(Viewer.getUser().getUserName())) {
            like.setTextFill(Color.RED);
        }
        like.setOnMouseClicked(r -> {
            if (post.getLike().contains(Viewer.getUser().getUserName())) {
                Viewer.getUser().UnLike(post);
            }
            else {
                Viewer.getUser().Like(post);
            }
//            hasBack = true;
            getLastViewer().refresh();
        });

        int numberOffComments = post.getComments().size();
        Label comments = new Label("Comments : ".concat(String.valueOf(numberOffComments)));
        comments.setOnMouseClicked(a -> {
            new CommentListViewer(stage, this, post).show();
        });


        int repostNumber = (int) post.getReposts();
        Label repost = new Label("RePost : ".concat(String.valueOf(repostNumber)));
        if (post.getRepost().contains(Viewer.getUser().getUserName())) {
            repost.setTextFill(Color.RED);
        } else {
            //TODO insert to delete reposted post
        }
        repost.setOnMouseClicked(action -> {
            if (!post.getRepost().contains(Viewer.getUser().getUserName())) {
                Viewer.getUser().RePost(this.post);//
            } else {
                Viewer.getUser().RemoveRepost(this.post);
            }
            getLastViewer().refresh();
        });


        HBox date = new HBox(40);
        Text dateText = new Text(post.getDate().toString());
        date.getChildren().addAll(like, repost, comments, dateText);
        date.setAlignment(Pos.CENTER);

        VBox commentingButton = new VBox();
        commentingButton.getChildren().add(comment);
        commentingButton.setAlignment(Pos.CENTER_RIGHT);

        VBox all = new VBox(10);
        all.setAlignment(Pos.CENTER);//
        all.getChildren().addAll(commentingButton, title, message, date);
        gridPane.add(all, 0,1, 3, 5);

        setPane(gridPane);
        if (hasBack) {
            HBox backBox = new HBox();
            Button back = new Button("back");
            back.setOnAction(actionEvent -> {
                back();
            });
            backBox.getChildren().add(back);
            gridPane.add(backBox, 0, 6);
        }

        setScene(new Scene(gridPane, x, y));
    }

    public Post getPost() {
        return post;
    }

    public void comment(Comment comment) {
        this.post.Comment(comment);
    }
}

package SBUGRAM.Scenes;

import SBUGRAM.Comment;
import SBUGRAM.Post;
import SBUGRAM.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class PostMaker extends Viewer{
    static String title = "POST";
    static String labelOfMessage = "Post: ";
    private final User user;
    public PostMaker(User user, Stage stage, Viewer lastViewer) {
        super(stage, lastViewer);
        this.user = user;
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

        Text name = new Text(title);
        name.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
        Text nuLL = new Text("");
        nuLL.setVisible(false);
        Name.getChildren().addAll(name, nuLL);
        Name.setAlignment(Pos.TOP_CENTER);
        gridPane.add(Name, 3, 0);

        Label userName = new Label("User Name: ");

        TextField userNameText = new TextField(user.getUserName());
        userNameText.setEditable(false);// if s/he want to change it :)

        Label title = new Label("Title: ");

        TextField titleText = new TextField();


        Label comment = new Label(labelOfMessage);

        labels.getChildren().addAll(userName, title,  comment);
        gridPane.add(labels, 0, 3,1,5);

        TextArea postText = new TextArea();
        postText.setWrapText(true);
        postText.setPrefSize(330, 200);


        HBox buttons = new HBox(10);
        Button cancel = new Button("cancel");
        cancel.setOnAction(e -> {
            back();
            //Todo search about this...
        });
        Button send = new Button("post");
        send.setOnAction(r -> {
            String POST = postText.getText();
            String TITLE = titleText.getText();
            user.Post(new Post(user.getUserName(), POST, TITLE));
            getLastViewer().refresh();
            back();
        });


        buttons.getChildren().addAll(cancel, send);
        buttons.setAlignment(Pos.CENTER_LEFT);

        texts.getChildren().addAll(userNameText, titleText, postText, buttons);
        gridPane.add(texts, 1, 3, 5, 9);


        setPane(gridPane);
        setScene(new Scene(gridPane, x, y));
    }


    public Comment getComment() {
        return (Comment) getObjects().get(0);
    }

}

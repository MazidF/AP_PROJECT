package SBUGRAM.Scenes;

import SBUGRAM.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


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

        Button addImage = new Button("add image");

        final Image[] imageUser = {null};
        final String[] filePath = {null};

        addImage.setOnAction(actionEvent -> {
            Stage newStage = new Stage();
            newStage.setWidth(stage.getWidth());
            newStage.setHeight(stage.getHeight());

            GridPane innerGridPane = new GridPane();
            innerGridPane.setAlignment(Pos.CENTER);

            final Image[] image = new Image[1];
            image[0] = null;

            ImageView imageView = new ImageView();
            try {
                imageView.setImage(new Image(new FileInputStream("C:\\Users\\vcc\\IdeaProjects\\first fx\\src\\SOURCE\\profileImage.png")));
            } catch (FileNotFoundException e) {
                imageView.setVisible(false);
                imageView.setManaged(false);
            }

            Button add = new Button("add");
            add.setOnAction(actionEvent1 -> {
                try {
                    FileChooser chooser = new FileChooser();
                    File file = chooser.showOpenDialog(newStage);
                    filePath[0] = file.getAbsolutePath();
                    image[0] = new Image(new FileInputStream(filePath[0]));
                    imageView.setImage(image[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                    image[0] = null;
                    //TODO add error part
                }
                newStage.show();
            });
            Button delete = new Button("delete");
            delete.setOnAction(actionEvent1 -> {
                try {
                    imageView.setImage(new Image(new FileInputStream("C:\\Users\\vcc\\IdeaProjects\\first fx\\src\\SOURCE\\profileImage.png")));
                } catch (FileNotFoundException e) {
                    imageView.setVisible(false);
                    imageView.setManaged(false);
                }
                newStage.show();
            });
            VBox buttons = new VBox(10);
            buttons.getChildren().addAll(add, delete);
            HBox imagePathBox = new HBox(10);
            imagePathBox.setAlignment(Pos.CENTER);

            imageView.setFitHeight(200);
            imageView.setFitWidth(200);
            imageView.maxHeight(200);
            imageView.maxWidth(200);

            imagePathBox.getChildren().addAll(imageView, buttons);


            Button done = new Button("done");
            done.setOnAction(actionEvent1 -> {
                imageUser[0] = image[0];
                newStage.close();
            });

            VBox allInner = new VBox(15);
            allInner.getChildren().addAll(imagePathBox, new Label(),done);
            allInner.setAlignment(Pos.CENTER);

            innerGridPane.getChildren().add(allInner);
            newStage.setScene(new Scene(innerGridPane, stage.getScene().getWidth(), stage.getScene().getHeight()));
            newStage.show();
        });




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
            Post post = new Post(user.getUserName(), POST, TITLE);
            if (imageUser[0] != null) {
                post.setImage(Tools.imageToByte(imageUser[0]));
            }
            user.Post(post);
            getLastViewer().refresh();
            back();
        });


        buttons.getChildren().addAll(cancel, addImage, send);
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

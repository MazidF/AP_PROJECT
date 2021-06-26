package SBUGRAM.Scenes;

import SBUGRAM.Password;
import SBUGRAM.Server;
import SBUGRAM.User;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Forgot extends Viewer {
    private AtomicInteger page = new AtomicInteger(1);
    private Server server = Viewer.getServer();
    private User user = null;
    private boolean done = false;

    public Forgot(Stage stage) {
        super(stage, null, new ArrayList<>());
        initialization(stage, stage.getScene().getWidth(), stage.getScene().getHeight());
    }

    @Override
    public void initialization(Stage stage, double x, double y) {
        Stage newStage = new Stage();

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);

        VBox VBox = new VBox(10);
        VBox.setAlignment(Pos.CENTER);

        Label userNameInner = new Label("user name: ");
        TextField userNameTextInner = new TextField();
        if (done && user != null) {
            userNameTextInner.setText(user.getUserName());
        }
        HBox userNameBox = new HBox(10);
        userNameBox.setAlignment(Pos.CENTER);
        userNameBox.getChildren().addAll(userNameInner, userNameTextInner);


        VBox questionBox = new VBox(10);
        questionBox.setAlignment(Pos.CENTER);

        Label top = new Label("answer the question to get your password.");
        if (done) {
            top.setText("");
            top.setVisible(false);
        }
        top.setAlignment(Pos.CENTER_LEFT);

        Label question = new Label();
        question.setMinWidth(300);
        if (page.get() == 1){
            question.setText("question : ".concat(Password.Question1));
        } else {
            question.setText("question : ".concat(Password.Question2));
        }
        if (done) {
            question.setText("your password is:");
        }

        TextField answerText = new TextField();
        answerText.setMinWidth(250);
        if (page.get() == 1) {
            answerText.setPromptText("ali");
        } else  {
            answerText.setPromptText("spiderMan");
        }
        if (done && user != null) {
            answerText.setText(user.getPassword().getPassword());
        }

        questionBox.getChildren().addAll(userNameBox, top, question, answerText);

        Button check = new Button("check");
        check.setOnAction(actionEvent -> {
            this.user = Viewer.getServer().allUsers.get(userNameTextInner.getText());
 /*           if (!(user.getPassword().isForgotPassword1() || user.getPassword().isForgotPassword2())) {


            }*/
            try {
                if ((page.get() == 1 && user.getPassword().getQuestion1().equals(answerText.getText()))
                        ||
                        (page.get() == 2 && user.getPassword().getQuestion2().equals(answerText.getText()))) {
                    done = true;
                    newStage.close();
                    initialization(stage, newStage.getScene().getWidth(), newStage.getScene().getHeight());
                }
            } catch (Exception e) {
                e.printStackTrace();
                GridPane gridPane1 = new GridPane();
                gridPane1.setAlignment(Pos.CENTER);
                Label label = new Label("you cant use forgot part.");
                Button back = new Button("close");
                back.setOnAction(actionEvent1 -> {
                    newStage.close();
                });
                VBox box = new VBox(20);
                box.getChildren().addAll(label, back);
                gridPane1.getChildren().add(box);
                newStage.setScene(new Scene(gridPane1, newStage.getScene().getWidth(), newStage.getScene().getHeight()));
                newStage.show();
            }
        });
        if (done) {
            check.setText("close");
            check.setOnAction(actionEvent -> {
                newStage.close();
            });
        }

        Button pageChanger = new Button();
        pageChanger.setMinWidth(100);
        if (page.get() == 1) {
            pageChanger.setText("next question");
        } else {
            pageChanger.setText("previous question");
        }
        if (done) {
            pageChanger.setVisible(false);
        }
        pageChanger.setOnAction(actionEvent -> {
            if (page.get() == 1) {
                page.set(2);
            } else {
                page.set(1);
            }
            newStage.close();
            initialization(stage, newStage.getScene().getWidth(), newStage.getScene().getHeight());
        });

        VBox.getChildren().addAll(questionBox, pageChanger, check);

        gridPane.getChildren().add(VBox);

        newStage.setScene(new Scene(gridPane, stage.getScene().getWidth(), stage.getScene().getHeight()));
        newStage.show();
    }

    @Override
    public void refresh() {
        //nothing
    }

    @Override
    public void show() {
        //nothing
    }
}

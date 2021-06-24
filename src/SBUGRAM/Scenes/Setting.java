package SBUGRAM.Scenes;

import SBUGRAM.DATE;
import SBUGRAM.Exceptions.PasswordException;
import SBUGRAM.Exceptions.SameUserNameException;
import SBUGRAM.Password;
import SBUGRAM.Tools;
import SBUGRAM.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.function.Function;

public class Setting extends Viewer {
    public User user;

    public Setting(Stage stage, Viewer lastViewer, User user) {
        super(stage, lastViewer, user);
        this.user = user;
        Scene scene = getStage().getScene();
        initialization(stage, scene.getWidth(), scene.getHeight());
    }

    @Override
    public void initialization(Stage stage, double x, double y) {
        GridPane grid = new GridPane();
        VBox all = new VBox(15);
        Text name = new Text("Setting");
        name.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
        grid.add(name, 0, 0, 2, 1);

        Label firstName = new Label("First Name: ");
        Label lastName = new Label("Last Name: ");


        TextField firstNameTextField = new TextField(user.getFirstName());
        firstNameTextField.setEditable(true);
        TextField lastNameTextField = new TextField(user.getLastName());
        lastNameTextField.setEditable(true);

        grid.add(firstName, 0, 2);
        grid.add(lastName, 0, 3);
        grid.add(firstNameTextField, 1, 2);
        grid.add(lastNameTextField, 1, 3);


        HBox date = new HBox(7.5);

        Label birthDay = new Label("BirthDay: ");
        grid.add(birthDay, 0, 4);


        String [] time = user.getBirthDay().toString().split("/");

        TextField year = new TextField();
        year.setEditable(true);
        year.setText(time[0]);
        TextField month = new TextField();
        month.setEditable(true);
        month.setText(time[1]);
        TextField day = new TextField();
        day.setEditable(true);
        day.setText(time[2]);
        year.setMaxWidth(45);
        month.setMaxWidth(45);
        day.setMaxWidth(45);

        date.getChildren().addAll(year, month, day);
        grid.add(date,1, 4);


        Label userName = new Label("User Name: ");
        grid.add(userName, 0, 5);

        TextField userNameTextField = new TextField(user.getUserName());
        userNameTextField.setEditable(true);
        grid.add(userNameTextField, 1, 5);


/*        Label password = new Label("Password: ");
        grid.add(password, 0, 6);


        PasswordField passwordTextField = new PasswordField();
        passwordTextField.setPromptText("1380.m.g");
        grid.add(passwordTextField, 1, 6);


        Label passwordAgain = new Label("RePassword: ");
        grid.add(passwordAgain, 0, 7);

        PasswordField passwordAgainTextField = new PasswordField();
        passwordAgainTextField.setPromptText("repeat password");
        grid.add(passwordAgainTextField, 1, 7);*/

        Button creatButton = new Button("save changes");
        creatButton.setOnAction(a -> {
            String firstName_ = firstNameTextField.getText();
            String lastName_ = lastNameTextField.getText();
            String username_ = userNameTextField.getText();
            DATE birthDay_ = new DATE(Integer.parseInt(year.getText()), Integer.parseInt(month.getText()), Integer.parseInt(day.getText()));
/*            String password_ = passwordTextField.getText();
            String password2_ = passwordAgainTextField.getText();*/
            Function<String, String> function = string -> {
                if (string.equals("") || string == null) return null;
                return string;
            };
            user.Change(function.apply(username_), function.apply(firstName_), function.apply(lastName_), null, birthDay_);
            //Viewer.getUser().change(function.apply(username_), function.apply(firstName_), function.apply(lastName_), null, birthDay_);
//            Viewer.getUser().changeUserName(username_);
//            refresh();
            back();
        });

        Button back = new Button("back");
        back.setOnAction(a -> {
            back();
        });
        grid.add(back, 0, 9);

        HBox creatBox = new HBox(10);
        creatBox.setAlignment(Pos.CENTER);
        creatBox.getChildren().add(creatButton);
        grid.add(creatBox, 1, 8);

        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setHgap(10);
        grid.setVgap(10);

        setPane(grid);
        setScene(new Scene(grid, x, y));
    }
}

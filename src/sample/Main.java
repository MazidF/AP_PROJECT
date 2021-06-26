package sample;

import SBUGRAM.*;
import SBUGRAM.Exceptions.PasswordException;
import SBUGRAM.Exceptions.SameUserException;
import SBUGRAM.Exceptions.SameUserNameException;
import SBUGRAM.Exceptions.UserNameNotFound;
import SBUGRAM.Scenes.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;


import java.io.*;
import java.net.Socket;


public class Main extends Application {

    public Server server;
    private User currentUser;
    private Socket socket;
    private Button exit = new Button("exit");
    private Button back = new Button("back");


    public void reStart(Stage stage) throws Exception {
        this.socket = null;
        this.server = null;
        this.currentUser = null;
        this.init();
        this.start2(stage);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
/*        byte [] bytes = Tools.imageToByte(new Image(new FileInputStream("C:/Users/vcc/Pictures/Saved Pictures/image.png")));
        System.out.println(Arrays.toString(bytes));
        ImageView view = new ImageView(Tools.byteToImage(bytes));
        VBox box = new VBox();
        box.getChildren().add(view);
        primaryStage.setScene(new Scene(box, 500, 400));
        primaryStage.show();*/
        new Logo(this, primaryStage, 500, 450).show();
        if(this.socket == null) System.exit(-1121);
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), actionEvent -> {
            logeIn(primaryStage, 500, 450);
        }));
        timeline.setCycleCount(1);
        timeline.play();
    }

    public void start2(Stage primaryStage) throws Exception{
        new Logo(this, primaryStage, primaryStage.getScene().getWidth(), primaryStage.getScene().getHeight()).show();
        if(this.socket == null) System.exit(-1122);
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), actionEvent -> {
            logeIn(primaryStage, primaryStage.getScene().getWidth(), primaryStage.getScene().getHeight());
        }));
        timeline.setCycleCount(1);
        timeline.play();
    }

    @Override
    public void stop() {
        if (currentUser != null) {
            System.out.println("closing the user...");
            if (Viewer.getUser() != null) {
                currentUser = Viewer.getUser();
            }
            currentUser.saveAndExit();
        }
    }

    @Override
    public void init() throws Exception {
        this.socket = User.socketMaker();
        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
        Thread.sleep(100);
        this.server = (Server) inputStream.readObject();
        Viewer.setServer(this.server);
        Viewer.setMain(this);
        if (this.server == null) throw new Exception();
    }

    public void upToDate() {
        currentUser.upToDate();
        Tools.sleep(100);
        this.server = currentUser.server;
    }

    private void MainPage(Stage stage) {
        upToDate();
        if(this.server == null) System.exit(8463130);
        new MainPage(stage, null, this.currentUser, this.server).show();
    }

    public void logeIn(Stage stage, double x, double y) {
        GridPane pane = new GridPane();
        Text name = new Text("SBU GRAM");
        name.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
        pane.add(name, 0, 0, 2, 1);

        Label userName = new Label("User Name: ");
        pane.add(userName, 0, 1);

        TextField userNameTextField = new TextField();
        pane.add(userNameTextField, 1, 1);

        CheckBox passwordViewer = new CheckBox("Show password");
        pane.add(passwordViewer, 0, 3);

        Label password = new Label("Password: ");
        pane.add(password, 0, 2);

        PasswordField passwordTextField = new PasswordField();
        passwordTextField.managedProperty().bind(passwordViewer.selectedProperty().not());
        passwordTextField.visibleProperty().bind(passwordViewer.selectedProperty().not());
        pane.add(passwordTextField, 1, 2);

        TextField passwordTextFieldViewer = new TextField();
        passwordTextFieldViewer.managedProperty().bind(passwordViewer.selectedProperty());
        passwordTextFieldViewer.visibleProperty().bind(passwordViewer.selectedProperty());
        pane.add(passwordTextFieldViewer, 1, 2);

        passwordViewer.setOnAction(a -> {
            if (!passwordViewer.isSelected()) {
                passwordTextField.setText(passwordTextFieldViewer.getText());
            } else {
                passwordTextFieldViewer.setText(passwordTextField.getText());
            }
        });

        Button logInButton = new Button("login");
        logInButton.setOnAction(a -> {
            passwordViewer.fire();
            String username = userNameTextField.getText();
            String password_ = passwordTextField.getText();
            try {
                this.server.passwordChecker(username, password_);
                this.currentUser = new User(socket, username);
                Viewer.setUser(this.currentUser);
                MainPage(stage);
            } catch (Exception e) {
                e.printStackTrace();
                String error = e.toString();
                if (e instanceof SameUserException) {
                    error = "sorry we find a problem.\nplease reconnect later to solve the problem.";
                    System.exit(-1);
                }
                if (e instanceof UserNameNotFound) {
                    error2(userNameTextField, "user not found");
                } else {
                    repair2(userNameTextField);
                }
                if (e instanceof PasswordException) {
                    passwordTextField.setText("password is wrong");
                    passwordTextFieldViewer.setText("password is wrong");
                    error2(passwordTextFieldViewer);
                    error2(passwordTextField);
                    if (!passwordViewer.isSelected()) {
                        passwordViewer.fire();
                    }
                } else {
                    repair2(passwordTextFieldViewer);
                    repair2(passwordTextField);
                }
                HBox errorBox = new HBox(new Text(error));
                ContextMenu contextMenu = new ContextMenu();
                contextMenu.show(errorBox, 1, 1);
            }
        });
        Button newAccountButton = new Button("creat one");
        newAccountButton.setOnAction(a -> {
            Scene scene = stage.getScene();
            newAccount(stage, scene.getWidth(), scene.getHeight());
        });

        HBox buttons = new HBox(10);
        buttons.getChildren().addAll(newAccountButton, logInButton);
        buttons.setAlignment(Pos.CENTER);
        pane.add(buttons,1, 3, 2, 3);

        exit.setOnAction(e -> {
            stage.close();
        });

        HBox exitBox = new HBox(30);
        Label forgot = new Label("forgot password?");
        forgot.setOnMouseClicked(mouseEvent -> {
            new Forgot(stage);
        });
        exitBox.setAlignment(Pos.BOTTOM_LEFT);
        exitBox.getChildren().addAll(exit);
        pane.add(exitBox, 0, 7);
        pane.add(forgot, 1, 7);



        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(25, 25, 25, 25));
        pane.setHgap(10);
        pane.setVgap(10);
        Scene scene = new Scene(pane, x, y);
        stage.setScene(scene);
        stage.show();
    }

    private void newAccount(Stage stage, double x, double y)  {
        GridPane grid = new GridPane();
        VBox all = new VBox(15);
        Text name = new Text("new Account");
        name.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
        grid.add(name, 0, 0, 2, 1);

        Label firstName = new Label("First Name: ");
        Label lastName = new Label("Last Name: ");


        TextField firstNameTextField = new TextField();
        firstNameTextField.setPromptText("your first name:");
        TextField lastNameTextField = new TextField();
        lastNameTextField.setPromptText("your last name:");

        grid.add(firstName, 0, 2);
        grid.add(lastName, 0, 3);
        grid.add(firstNameTextField, 1, 2);
        grid.add(lastNameTextField, 1, 3);


        HBox date = new HBox(7.5);

        Label birthDay = new Label("BirthDay: ");
        grid.add(birthDay, 0, 4);

        DATE now = new DATE();
        String [] time = (now.toString().substring(0, now.toString().indexOf(" "))).split("/");

        TextField year = new TextField();
        year.setPromptText(time[0]);
        TextField month = new TextField();
        month.setPromptText(time[1]);
        TextField day = new TextField();
        day.setPromptText(time[2]);
        year.setMaxWidth(45);
        month.setMaxWidth(45);
        day.setMaxWidth(45);

        date.getChildren().addAll(year, month, day);
        grid.add(date,1, 4);


        Label userName = new Label("User Name: ");
        grid.add(userName, 0, 5);

        TextField userNameTextField = new TextField();
        userNameTextField.setPromptText("mammad_gighar");
        grid.add(userNameTextField, 1, 5);


        Label password = new Label("Password: ");
        grid.add(password, 0, 6);


        PasswordField passwordTextField = new PasswordField();
        passwordTextField.setPromptText("1380.m.g");
        grid.add(passwordTextField, 1, 6);


        Label passwordAgain = new Label("RePassword: ");
        grid.add(passwordAgain, 0, 7);

        PasswordField passwordAgainTextField = new PasswordField();
        passwordAgainTextField.setPromptText("repeat password");
        grid.add(passwordAgainTextField, 1, 7);

        final Image[] imageUser = {null};
        final String[] answerUser = {null, null};
        final String[] phoneNumberUser = {null};
        final String[] filePath = {null};

        CheckBox additionalOption = new CheckBox("more");
        //TODO add forgot password option __ if you have time :)
        additionalOption.setOnAction(actionEvent -> {
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


            VBox questionBox = new VBox(10);
            questionBox.setAlignment(Pos.CENTER);
            Label question1 = new Label("question1 : ".concat(Password.Question1));
            question1.setMinWidth(300);
            Label question2 = new Label("question2 : ".concat(Password.Question2));
            question2.setMinWidth(300);

            TextField answerText1 = new TextField();
            answerText1.setMinWidth(250);
            answerText1.setPromptText("ali");
            TextField answerText2 = new TextField();
            answerText2.setMinWidth(250);
            answerText2.setPromptText("spiderMan");
            questionBox.getChildren().addAll(question1, answerText1, question2, answerText2);


            Label phoneNumber = new Label("phone number: ");
            TextField phoneNumberText = new TextField();
            phoneNumberText.setPromptText("+98912.....79");


            HBox phone = new HBox(10);
            phone.getChildren().addAll(phoneNumber, phoneNumberText);
            phone.setAlignment(Pos.CENTER);

            Button done = new Button("done");
            done.setOnAction(actionEvent1 -> {
                imageUser[0] = image[0];
                answerUser[0] = answerText1.getText();
                answerUser[1] = answerText2.getText();
                phoneNumberUser[0] = phoneNumberText.getText();
                newStage.close();
            });

            VBox allInner = new VBox(15);
            allInner.getChildren().addAll(imagePathBox, phone, questionBox, new Label(),done);
            allInner.setAlignment(Pos.CENTER);

            innerGridPane.getChildren().add(allInner);
            newStage.setScene(new Scene(innerGridPane, stage.getScene().getWidth(), stage.getScene().getHeight()));
            newStage.show();
        });



        Button creatButton = new Button("creat");
        creatButton.setOnAction(a -> {
            String firstName_ = firstNameTextField.getText();  error(firstName, firstNameTextField);
            String lastName_ = lastNameTextField.getText();  error(lastName, lastNameTextField);
            String username = userNameTextField.getText();  error(userName, userNameTextField);
            String password_ = passwordTextField.getText();  error(password, passwordTextField);
            String password2_ = passwordAgainTextField.getText();  error(passwordAgain, passwordAgainTextField);

            DATE birthDay_ = null;
            try {
                birthDay_ = new DATE(Integer.parseInt(year.getText()), Integer.parseInt(month.getText()), Integer.parseInt(day.getText()));
                repair(birthDay);
            } catch (Exception e) {
                error(birthDay, year, month, day);
            }
            Password passwordUser = null;
            try {
                passwordUser = new Password(password_, password2_);
                passwordUser.FirstQuestion(answerUser[0]);
                passwordUser.SecondQuestion(answerUser[1]);
                repair(password);
                repair(passwordAgain);
            } catch (Exception e) {
                if (password_.equals("") && password2_.equals("")) {
                    //do nothing
                } else if (password_.equals("") || password2_.equals("")) {
                    //do nothing
                } else {
                    error(passwordAgain);
                }
            }
            try {
                this.server.userNameChecker(username);
                checker(username, firstName_, lastName_, password_, password2_, passwordUser, year.getText(), month.getText(), day.getText());
                this.currentUser = new User(this.socket, username, firstName_, lastName_, passwordUser, birthDay_, Tools.imageToByte(imageUser[0]), phoneNumberUser[0]);

                Viewer.setUser(this.currentUser);
                MainPage(stage);

            } catch (Exception e) {
                e.printStackTrace();
                String error;
                Text errorText = new Text();
//                errorText.setFill(Color.RED);
                HBox errorBox = new HBox();
                if (e instanceof SameUserNameException) {
                    userName.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
                } else {
                    if (!username.equals("")) {
                        repair(userName);
                    }
                }
/*                if (e instanceof SameUserNameException) {
                    error = e.toString();
                    errorText.setText(error);
                    errorBox.getChildren().add(errorText);
//                    grid.add(errorBox, 2 , 5);
                }*//*
                if (e instanceof PasswordException) {
                    error = e.toString();
                    errorText.setText(error);
                    errorBox.getChildren().add(errorText);
//                    grid.add(errorBox, 2 , 7);
                }*/
                grid.add(errorBox, 2 , 5);
                ContextMenu contextMenu = new ContextMenu();
                contextMenu.show(errorBox, 1, 1);
            }

        });

        back.setOnAction(a -> {
            Scene scene = stage.getScene();
            logeIn(stage, scene.getWidth(), scene.getHeight());
        });
        grid.add(back, 0, 9);

        VBox creatBox = new VBox(10);
        creatBox.setAlignment(Pos.CENTER);
        creatBox.getChildren().addAll(additionalOption, creatButton);
        grid.add(creatBox, 1, 8);

        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setHgap(10);
        grid.setVgap(10);
        Scene scene = new Scene(grid, x, y);
        stage.setScene(scene);
        stage.show();
    }

    public void error2(TextField textField, String... error) {
        if (error.length != 0) {
            textField.setText(error[0]);
        }
        textField.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
    }


    public void error(Label label, TextField... textField) {
        if (textField.length == 0) {
            label.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
            return;
        }

/*        if (error.length == 0) {
            error = new String[1];
            error[0] = "fill this part";
        }
        if (label.getText().equals("") || label.getText().equals("fill this part") || label.getText().equals("-1")) {
            label.setText(error[0]);
            label.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
            return;
        }
        label.setStyle("-fx-text-fill: black; -fx-font-size: 12px;");*/
        for (TextField field : textField) {
            if (field.getText().equals("")) {
                label.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
                return;
            }
        }
        label.setStyle("-fx-text-fill: black; -fx-font-size: 12px;");
    }


    public void error3(TextField textField, String... error) {
        if (error.length == 0) {
            error = new String[1];
            error[0] = "fill this part";
        }
        if (textField.getText().equals("") || textField.getText().equals("fill this part") || textField.getText().equals("-1")) {
            textField.setText(error[0]);
            textField.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
            return;
        }
        textField.setStyle("-fx-text-fill: black; -fx-font-size: 12px;");
    }

    public void repair(Label... labels) {
        for (Label field : labels) {
            field.setStyle("-fx-text-fill: black; -fx-font-size: 12px;");
        }
    }

    public void repair2(TextField... textFields) {
        for (TextField field : textFields) {
            field.setStyle("-fx-text-fill: black; -fx-font-size: 12px;");
        }
    }

    @SafeVarargs
    public final <T> void checker(T... t) throws Exception {
        for (T t1 : t) {
            if (t1 instanceof String) {
                if (t1.equals("") || t1.equals("fill this part") || t1.equals("-1")) {
                    throw new Exception();
                }
            }
            if (t1 == null) {
                throw new Exception();
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
//        Server server = Main.server;
        System.out.println("closed.");
    }
}

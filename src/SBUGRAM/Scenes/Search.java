package SBUGRAM.Scenes;

import SBUGRAM.Server;
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

import java.util.List;
import java.util.stream.Collectors;


public class Search extends Viewer{
    private String error = "";
    public Server server;
    private String searchedUser = null;

    public Search(Stage stage, Viewer lastViewer, Server server) {
        super(stage, lastViewer, server);
        this.server = server;
        Scene scene = stage.getScene();
        initialization(stage, scene.getWidth(), scene.getHeight());
    }

    @Override
    public void initialization(Stage stage, double x, double y) {

        HBox bottom = new HBox();
        bottom.setAlignment(Pos.CENTER);
        Button back = new Button("back");
        back.setOnAction(actionEvent -> {
            back();
        });
        bottom.getChildren().add(back);


        VBox all = new VBox(20);
        all.setAlignment(Pos.CENTER);
        Label search = new Label("Search");
        search.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));

        HBox searchBox = new HBox(15);
        searchBox.setAlignment(Pos.CENTER);
        TextField searchArea = new TextField();
        searchArea.setPromptText("enter userName");
        if (this.searchedUser != null) {
            searchArea.setText(this.searchedUser);
        }

        Text error = new Text(this.error);
        error.setFill(Color.RED);


        Label usersMenu = new Label("users list");
        CheckBox users = new CheckBox();
        users.setOnAction(actionEvent -> {
            Stage newStage = new Stage();
            ScrollPane innerScrollPane = new ScrollPane();
            List<Label> labels = Viewer.getServer().allUsers.keySet().stream()
                    .map(s -> {
                        Label label = new Label(s);
                        label.setOnMouseClicked(mouseEvent -> {
                            this.searchedUser = s;
                            newStage.close();
                            this.refresh();
                        });
                        return label;
                    })
                    .collect(Collectors.toList());
            VBox allUsers = new VBox(10);
            allUsers.setAlignment(Pos.CENTER);
            allUsers.getChildren().addAll(labels);

            innerScrollPane.setPadding(new Insets(15, 25, 15, 25));
            innerScrollPane.setContent(allUsers);

            newStage.setScene(new Scene(innerScrollPane, 500, 450));
            newStage.show();
        });

        HBox allUsersBox = new HBox(10);
        allUsersBox.setAlignment(Pos.CENTER);
        allUsersBox.getChildren().addAll(usersMenu, users);


        Button searchButton = new Button("search");
        searchButton.setOnAction(actionEvent -> {
            refresh(); //TODO check if make problem ??
            this.error = "";
            String userName = searchArea.getText();
//            System.out.println("searched : " + userName);
            if (userName == null || userName.equals(""))  {
                this.error = "Nothing entered";
                initialization(stage, x, y);
                show();
            }
            try {
                new ProfileViewer(stage, this, server.allUsers.get(userName)).show();
            } catch (Exception e) {
                this.error = "User not found";
                initialization(stage, x, y);
                show();
            }

        });
        searchBox.getChildren().addAll(searchArea, searchButton, allUsersBox);



        all.getChildren().addAll(search, new Text(), searchBox, error, bottom);
        setPane(all);
        setScene(new Scene(all, x, y));
    }
}

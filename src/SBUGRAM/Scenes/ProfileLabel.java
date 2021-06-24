package SBUGRAM.Scenes;

import SBUGRAM.User;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class ProfileLabel extends Viewer {
    public final User user;

    public ProfileLabel(Stage stage, Viewer lastViewer, User user) {
        super(stage, lastViewer, user);
        this.user = user;
        Scene s = getLastScene();
        initialization(stage, s.getWidth(), s.getHeight());
    }

    @Override
    public void initialization(Stage stage, double x, double y) {
        VBox box = new VBox();
        Button profileButton = new Button();
        profileButton.setText(user.getUserName());
        profileButton.setPrefSize(60, 60);
        profileButton.setOnAction(actionEvent -> new ProfileViewer(stage, getLastViewer(), user).show());

        box.setAlignment(Pos.CENTER);
        box.getChildren().add(profileButton);

        setPane(box);
    }

    @Override
    public void show() {
        //do nothing
    }
}

package SBUGRAM.Scenes;

import SBUGRAM.User;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ForgotPassword extends Viewer {
    public User user;

    public ForgotPassword(Stage stage, Viewer lastViewer, User user) {
        super(stage, lastViewer, user);
        this.user = user;
        Scene scene = getStage().getScene();
        initialization(stage, scene.getWidth(), scene.getHeight());
    }

    @Override
    public void initialization(Stage stage, double x, double y) {
//        if (user.getPassword())
    }
}

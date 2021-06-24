package SBUGRAM.Scenes;


import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import sample.Main;


import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Logo extends Viewer{
    double x, y;
    private String path = "C:\\Users\\vcc\\IdeaProjects\\first fx\\src\\SOURCE\\logo.png";

    public Logo(Stage stage, Viewer lastViewer) {
        super(stage, lastViewer);
        Scene s = getLastScene();
        initialization(stage, s.getWidth(), s.getHeight());
    }

    public Logo(Main main, Stage stage, double x, double y) {
        super(stage, null);
        this.x = x;
        this.y = y;
        initialization(stage, x, y);
    }

    @Override
    public void initialization(Stage stage, double x, double y) {
        GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        try {
            Image image = new Image(new FileInputStream(path));
            pane.getChildren().add(new ImageView(image));
            pane.setAlignment(Pos.CENTER);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(-11111111);
        }
        setPane(pane);
        setScene(new Scene(pane, x, y));
    }

}

package SBUGRAM.Scenes;

import javafx.scene.Scene;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SBU_GRAM extends Viewer{
    private Stage stage;

    public SBU_GRAM(Stage stage, Viewer lastViewer) {
        super(stage, lastViewer);
        Scene scene = stage.getScene();
        initialization(stage, scene.getWidth(), scene.getHeight());
    }

    @Override
    public void initialization(Stage stage, double x, double y) {
        HBox top = new HBox();
        top.setAlignment(Pos.CENTER_LEFT);
        Button back = new Button("back");
        back.setOnAction(actionEvent -> {
            back();
        });
        top.getChildren().add(back);

        HBox informationBox = new HBox();
        informationBox.setAlignment(Pos.CENTER);
        TextArea information = new TextArea("Name: SBU_GRAM\nBy: Mohammad Mahdi Mazidabadi Farahani\nEmail: m3farahani1382@gmail.com\nVersion: 1.0\nI thank my brother for his good ideas.\nEnjoy :)");
        information.setEditable(false);
        information.setWrapText(true);
        information.setPrefSize(400, 300);
        informationBox.getChildren().add(information);

        VBox all = new VBox(10);
        all.setAlignment(Pos.CENTER);
        all.getChildren().addAll(top, new Logo(stage, getLastViewer()).getMadePane(), informationBox);

        setPane(all);
        setScene(new Scene(all, stage.getMaxWidth(), stage.getMaxHeight()));
    }

    @Override
    public void show() {
        stage = new Stage();
        stage.setScene(getMadeScene());
        stage.show();
    }

    @Override
    public void back() {
        super.back();
        this.stage.close();
    }
}

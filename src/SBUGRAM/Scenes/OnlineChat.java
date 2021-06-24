package SBUGRAM.Scenes;

import SBUGRAM.User;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.Socket;

public class OnlineChat extends Viewer implements Runnable {
    private Stage stage = new Stage();
    private Chat chat;
    private boolean stop = false;

    public OnlineChat(Stage stage, Chat chat) {
        super(stage, chat);
        this.chat = chat;
        run();
    }

    @Override
    public void run() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.5), actionEvent -> {
            refresh();
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

    }
}

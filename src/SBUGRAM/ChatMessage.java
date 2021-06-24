package SBUGRAM;

import SBUGRAM.Scenes.Chat;
import SBUGRAM.Scenes.Viewer;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.Serial;
import java.io.Serializable;

public class ChatMessage implements Serializable {
    @Serial
    private static final long serialVersionUID = 6529685094878757690L;

    private boolean rightClick = false;
    public DATE date = new DATE();
    public String userName;
    public String message;
    public transient TextArea textArea;
    public transient Chat lastViewer;

    public ChatMessage(String userName, String message, Viewer lastViewer) {
        this.message = message;
        this.userName = userName;
        this.lastViewer = (Chat) lastViewer;
        ready();
    }

    private void ready() {
        if (textArea == null) {
            String [] split = this.message.split("\n");
            int lines = split.length;
            for (String s : split) {
                if (s.length() > 35) {
                    lines += s.length()/35;
                }
            }
            this.textArea = new TextArea(this.message);
            this.textArea.setMaxWidth(240);
            this.textArea.setPrefHeight(lines*25);
            this.textArea.setPrefRowCount(1);
            this.textArea.setWrapText(true);
            this.textArea.setEditable(false);
            if (this.userName.equals(Viewer.getUser().getUserName())) {
                this.textArea.setOnMousePressed(mouseEvent -> {
                    this.editable();
                    rightClick = !rightClick;
                    lastViewer.initialization(lastViewer.getStage(), lastViewer.getStage().getScene().getWidth(), lastViewer.getStage().getScene().getHeight());
                });
            }
        }
    }

    public TextArea getMessage() {
        ready();
        return this.textArea;
    }

    public VBox getPane() {
        ready();

        VBox vBox = new VBox(10);

        if (this.rightClick){
            Button delete = new Button("X");
            delete.setTextFill(Color.RED);
            delete.setMaxSize(10, 10);
            delete.setOnAction(actionEvent -> {
                Viewer.getUser().RemoveChat(userName, this);
                rightClick = false;
//                lastViewer.Refresh();
                lastViewer.refresh();
            });

            Button edit = new Button("edit");
            edit.setTextFill(Color.RED);
            edit.setOnAction(actionEvent -> {
                edit(this.textArea.getText());
                rightClick = false;
//                lastViewer.Refresh();
                lastViewer.refresh();
            });

            HBox options = new HBox(10);
            options.getChildren().addAll(edit, delete);
            options.setAlignment(Pos.CENTER_RIGHT);

            vBox.getChildren().addAll(options, getMessage());
        } else {
            vBox.getChildren().add(getMessage());
        }

        vBox.setMinWidth(380);
        if (userName.equals(Viewer.getUser().getUserName())) {
            vBox.setAlignment(Pos.CENTER_RIGHT);
        } else {
            vBox.setAlignment(Pos.CENTER_LEFT);
        }
        return vBox;
    }

    public void editable() {
        this.textArea.setEditable(true);
    }

    public void edit(String message) {
        this.message = message;
        this.textArea.setText(this.message);
        this.textArea.setEditable(false);
    }

    public int compareTo(ChatMessage chatMessage) {
        return this.date.compareTo(chatMessage.date);
    }

}

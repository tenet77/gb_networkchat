package chat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.*;

public class ChatApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("application2.fxml"));
        stage.setTitle("Network chat");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("img/chat.png")));
        stage.setResizable(false);
        stage.setScene(new Scene(root));
        stage.show();

    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        launch(args);
    }
}

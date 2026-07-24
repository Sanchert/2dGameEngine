package org.example.keenmarksmanfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

public class GameApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GameApplication.class.getResource("game_bg.fxml"));
        AnchorPane root = new AnchorPane();
        fxmlLoader.setRoot(root);
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Space Invaders - UNN version");
        stage.setScene(scene);
        stage.show();
    }
}

package org.example.keenmarksmanfx;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

public class GameApplication extends Application {
//    private AnimationTimer gameLoop;
//    private GameWorld gameWorld;
//    private Thread gameThread;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GameApplication.class.getResource("KeenMarksman.fxml"));
        Pane root = new Pane();
        fxmlLoader.setRoot(root);

        Scene scene = new Scene(fxmlLoader.load(), 600, 400);

//        this.gameWorld = new GameWorld();
//        GameController controller = fxmlLoader.getController();
//        controller.setGameWorld(this.gameWorld);

        stage.setTitle("Space Invaders - UNN version");
        stage.setScene(scene);
        stage.show();

//        startGameLoop();
    }

//    private void startGameLoop() {
//        this.gameLoop = new AnimationTimer() {
//            @Override
//            public void handle(long now) {
//                // Этот метод вызывается ~60 раз в секунду
//                // и НЕ БЛОКИРУЕТ JavaFX поток
//                if (gameWorld.getGameState() == GameState.EXIT) {
//                    stop(); // Останавливаем таймер
//                    Platform.exit(); // Закрываем JavaFX приложение
//                    return;
//                }
//                //TODO: race condition (read/write) - non-critical (gameApp - reader, gameController - writer)
//                // synchronised
//                // or atomicreference
//                if (gameWorld.getGameState() == GameState.RUN) {
//                    gameWorld.update();  // Один шаг обновления
//                }
//            }
//        };
//        this.gameLoop.start();
//    }
//
//    @Override
//    public void stop() {
//        if (this.gameLoop != null) {
//            this.gameLoop.stop();
//        }
//    }
}

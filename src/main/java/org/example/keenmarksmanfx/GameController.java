package org.example.keenmarksmanfx;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

public class GameController {
//    @FXML private Rectangle Player;
//    @FXML private Label Score_number, Shoots_number;
//    @FXML private AnchorPane Root;
    private ObjectManager gameWorld;
    private final long MS_PER_UPDATE = 16;


    private int frameCount = 0;
    private long lastFPSCheck = 0;

    public void startGame() {
        AnimationTimer timer = new AnimationTimer() {
            long prev = System.currentTimeMillis();
            long lag = 0;
            @Override
            public void handle(long now) {
                if (gameWorld.getGameState() != GameState.EXIT) {
                    if (gameWorld.getGameState() == GameState.RUN) {
                        frameCount++;
                        if (now - lastFPSCheck >= 1_000_000_000) { // 1 секунда в наносекундах
                            System.out.println("FPS: " + frameCount);
                            frameCount = 0;
                            lastFPSCheck = now;
                        }

                        long current = System.currentTimeMillis();
                        long elapsed = current - prev;
                        prev = current;
                        lag += elapsed;
                        gameWorld.processInput();
                        while (lag >= MS_PER_UPDATE) {
                            gameWorld.update( lag / 1000.0);
//                            gameWorld.update(1.0);
                            lag -= MS_PER_UPDATE;
                        }
                    } else {
                        prev = System.currentTimeMillis();
                    }
                } else {
                    this.stop();
                }
            }
        };
        timer.start();
    }

    public void initialize() {
        gameWorld = new ObjectManager();
        gameWorld.gameWorld_INIT(Root); // Для программного добавления и удаления игровых объектов
        gameWorld.gameWorld_SETUP(Player, Score_number, Shoots_number); // TODO: изменить инициализацию на более общую
        startGame();
    }

    @FXML protected void onPauseBtnClick() {
        gameWorld.setGameState(GameState.PAUSED);
    }
    @FXML protected void onStartBtnClick() { //TODO: restart game state
        gameWorld.setGameState(GameState.RUN);
        gameWorld.gameWorld_SETUP(Player, Score_number, Shoots_number);
    }
    @FXML protected void onExitBtnClick() {
        gameWorld.setGameState(GameState.EXIT);
    }
    @FXML protected void onResumeBtnClick() {
        gameWorld.setGameState(GameState.RUN);
    }

    @FXML protected void onKeyPressed(KeyEvent event) {
        gameWorld.getInputHandler().handleKeyPressed(event.getCode());

    }
    @FXML protected void onKeyReleased(KeyEvent event) {
        gameWorld.getInputHandler().handleKeyReleased(event.getCode());
    }
}

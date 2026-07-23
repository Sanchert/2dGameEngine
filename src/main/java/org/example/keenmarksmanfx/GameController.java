package org.example.keenmarksmanfx;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

public class GameController {
    private final float msPerFrame = 16f;
    @FXML private Rectangle Player;
    @FXML private Label Score_number, Shoots_number;
    @FXML private AnchorPane Root;
    private GameWorld gameWorld;
    private Thread gameThread;

    public void startGame() {
        if (gameThread == null) {
            gameThread = new Thread(() -> {
                while (gameWorld.getGameState() != GameState.EXIT) {
                    while (gameWorld.getGameState() == GameState.RUN) {
                        Platform.runLater(() -> {
                            gameWorld.update();
                        });

                        try {
                            Thread.sleep(12);
                        } catch (InterruptedException e) {
                            return;
                        }
                    }
                }
            });

            gameThread.start();
        }
    }

    public void initialize() {
        gameWorld = new GameWorld();
        gameWorld.gameWorld_INIT(Root);
        gameWorld.gameWorld_SETUP(Player, Score_number, Shoots_number);
        startGame();
    }

    @FXML
    protected void onPauseBtnClick() {
        gameWorld.setGameState(GameState.PAUSED);
    }
    @FXML //TODO: restart game state
    protected void onStartBtnClick() {
        gameWorld.setGameState(GameState.RUN);
        gameWorld.gameWorld_SETUP(Player, Score_number, Shoots_number);
    }
    @FXML
    protected void onExitBtnClick() {
        gameWorld.setGameState(GameState.EXIT);
    }
    @FXML
    protected void onResumeBtnClick() {
        gameWorld.setGameState(GameState.RUN);
    }
    @FXML
    protected void onKeyPressed(KeyEvent event) {
        this.gameWorld.getInputHandler().handleKeyPressed(event.getCode());
        if (event.getCode() == KeyCode.ESCAPE) {
            if (this.gameWorld.getGameState() == GameState.RUN) {
                this.gameWorld.setGameState(GameState.PAUSED);
            }
        }
    }
    @FXML
    protected void onKeyReleased(KeyEvent event) {
        this.gameWorld.getInputHandler().handleKeyReleased(event.getCode());
    }
}

package org.example.keenmarksmanfx;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeType;

import java.util.*;

interface IBulletFactory {
    void createBullet(float x, float y);
}

interface IEnemyFactory {
    void  createEnemy(float pos_x, float pos_y, float speed, int cost);
}

class EnemyFactory implements IEnemyFactory {
    private final AnchorPane gamePane;
    private final List<Enemy> enemies;
    public EnemyFactory(AnchorPane gamePane, List<Enemy> enemies) {
        this.enemies = enemies;
        this.gamePane = gamePane;
    }
    @Override
    public void createEnemy(float pos_x, float pos_y, float speed, int cost) {
        Circle enemyView = new Circle(15);
        enemyView.setId("enemy_" + System.nanoTime());
        enemyView.setStroke(Color.BLACK);
        enemyView.setStrokeType(StrokeType.INSIDE);
        enemyView.setFill(createEnemyGradient());

        Enemy enemy = new Enemy(pos_x, pos_y, speed, cost);
        enemy.setSprite(enemyView);

        enemies.add(enemy);
        gamePane.getChildren().add(enemyView);
    }
    private static RadialGradient createEnemyGradient() {
        List<Stop> stops = Arrays.asList(
                new Stop(1.0, Color.rgb(191, 61, 38)),
                new Stop(0.46717, Color.rgb(191, 60, 40, 0.216565)),
                new Stop(0.0, Color.RED)

        );

        return new RadialGradient(
                0, 0.5, 0.5, 0.5, 0.75, true, CycleMethod.REPEAT, stops
        );
    }
}

class PlayerBulletFactory implements IBulletFactory {
    private final AnchorPane gamePane;
    private final List<Bullet> bullets; // список для логики
    public PlayerBulletFactory(AnchorPane gamePane, List<Bullet> bullets) {
        this.gamePane = gamePane;
        this.bullets = bullets;
    }
    @Override
    public void createBullet(float pos_x, float pos_y) {
        Rectangle bulletView = new Rectangle(10, 10);
        bulletView.setFill(Color.GREEN);
        bulletView.setId("bullet_" + System.nanoTime());

        Bullet bullet = new Bullet(pos_x, pos_y);
        bullet.setSprite(bulletView);

        bullets.add(bullet);
        gamePane.getChildren().add(bulletView);
    }
}

//TODO: singleton?
class InputHandler {
//    private final Set<KeyCode> activeKeys = new HashSet<>();
    private boolean D_Pressed = false;
    private boolean W_Pressed = false;
    private boolean S_Pressed = false;
    public void handleKeyPressed(KeyCode keyCode) {
//        activeKeys.add(keyCode);
        switch (keyCode) {
            case KeyCode.D -> D_Pressed = true;
            case KeyCode.W -> W_Pressed = true;
            case KeyCode.S -> S_Pressed = true;
        }
    }

    public void handleKeyReleased(KeyCode keyCode) {
//        activeKeys.remove(keyCode);
        switch (keyCode) {
            case KeyCode.D -> D_Pressed = false;
            case KeyCode.W -> W_Pressed = false;
            case KeyCode.S -> S_Pressed = false;
        }
    }

    public boolean isDKeyPressed() {
        return D_Pressed;
    }

    public boolean isSKeyPressed() {
        return S_Pressed;
    }

    public boolean isWKeyPressed() {
        return W_Pressed;
    }
}

abstract class GameObject {
    protected float pos_x, pos_y;
    protected boolean destroyed = false;
    protected Shape sprite = null;
    public GameObject(float x, float y) {
        setPosition(x, y);
    }

    public void setSprite(Shape s) {
        this.sprite = s;
    }

    public abstract void update();
    public abstract Rectangle getBounds();
    public void setPosition(float pos_x, float pos_y) {
        this.pos_x = pos_x;
        this.pos_y = pos_y;
        if (sprite == null) return;
        sprite.setLayoutX(pos_x);
        sprite.setLayoutY(pos_y);
    }
//    public void setPositionX(float pos_x) {
//        this.pos_x = pos_x;
//        if (sprite == null) return;
//        sprite.setLayoutX(pos_x);
//    }
//    public void setPositionY(float pos_y) {
//        this.pos_y = pos_y;
//        if (sprite == null) return;
//        sprite.setLayoutY(pos_y);
//    }
    public boolean collidesWith(GameObject other) {
        Rectangle thisBounds = this.getBounds();
        Rectangle otherBounds = other.getBounds();

        return thisBounds.intersects(
                otherBounds.getX(),
                otherBounds.getY(),
                otherBounds.getWidth(),
                otherBounds.getHeight()
        );
    }
    public void destroy() {
        destroyed = true;
        if (sprite != null) {
            sprite.setVisible(false);  // Скрываем спрайт сразу
        }
    }
    public boolean isDestroyed() { return destroyed; }
//    public double getX() { return pos_x; }
//    public double getY() { return pos_y; }
}

enum MoveDirection {
    MOVE_UP,
    MOVE_DOWN
}

enum GameState {
    RUN,
    PAUSED,
    EXIT,
}

class Player extends GameObject {
    private final float SPEED;
    private final float MIN_Y;
    private final float MAX_Y;
    private MoveDirection currentDirection = null;
    private int shoots = 10;
    private Label shootsText;
    private int timer = 60;
    public void setCurrentDirection(MoveDirection moveDirection) {
        this.currentDirection = moveDirection;
    }

    public Player(float pos_x, float pos_y) {
        super(pos_x, pos_y);
        SPEED = 1.0f;
        MIN_Y = 30f;
        MAX_Y = 270f;
    }

    public void setShootText(Label l) {
        this.shootsText = l;
        shootsText.setText(Integer.toString(shoots));
    }

    @Override
    public void update() {
        if (currentDirection != null) {
            move();
        }
        timer--;
    }

    public void shoot(GameWorld g) {
        if (shoots == 0) return;
        if (timer > 0) {
            return;
        } else {
            timer = 60;
        }
        g.getBulletFactory().createBullet(pos_x + 10f,  pos_y + 20f);
        shoots--;
        shootsText.setText(Integer.toString(shoots));
    }

    public void setShoots(int shoots) {
        this.shoots = shoots;
    }

//    public int getShoots() {
//        return this.shoots;
//    }

    protected void move() {
        if (currentDirection == MoveDirection.MOVE_UP) {
            pos_y = Math.max(MIN_Y, pos_y - SPEED);
        } else if (currentDirection == MoveDirection.MOVE_DOWN) {
            pos_y = Math.min(MAX_Y, pos_y + SPEED);
        }
        setPosition(pos_x, pos_y);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(pos_x, pos_y, 40, 50);
    }
}

class Enemy extends GameObject {
    private final int cost;
    private final float speed;
    private MoveDirection moveDirection = MoveDirection.MOVE_DOWN;

    public Enemy(float pos_x, float pos_y, float speed, int cost) {
        super(pos_x, pos_y);
        this.speed = speed;
        this.cost = cost;
    }
    @Override
    public void update() {
        this.move();
    }
    @Override
    public Rectangle getBounds() {
        return new Rectangle(pos_x - 15f, pos_y - 15f, 30, 30);
    }
    public int getCost() {
        return this.cost;
    }
    protected void move() {
        float UPPER_BOUND = 330.0f;
        float LOWER_BOUND = 20.0f;
        if (this.pos_y >= UPPER_BOUND) {
            moveDirection = MoveDirection.MOVE_DOWN;
        } else if (this.pos_y <= LOWER_BOUND) {
            moveDirection = MoveDirection.MOVE_UP;
        }

        switch (moveDirection) {
            case MOVE_UP   -> setPosition(pos_x, pos_y += speed);
            case MOVE_DOWN -> setPosition(pos_x, pos_y -= speed);
        }
    }
}

class Bullet extends GameObject {
    private final float SPEED;

    public Bullet(float pos_x, float pos_y) {
        super(pos_x, pos_y);
        SPEED = 3.5f;
    }

    @Override
    public void update() {
        this.move();
    }

    private void move() {
        setPosition(this.pos_x += SPEED, pos_y);
        if (this.pos_x > 800) { // За экраном
            this.destroy();
        }
    }

    @Override
    public Rectangle getBounds() { // TODO: one init collider
        return new Rectangle(pos_x, pos_y, 10, 10);
    }
}

enum GameDifficulty {
//    EASY(2),
//    MEDIUM(3),
    HARDCORE(5);

    private final int enemyCount;

    GameDifficulty(int enemyCount) {
        this.enemyCount = enemyCount;
    }

    public int mode() {
        return enemyCount;
    }
}

class GameWorld {
    private GameState gameState = GameState.PAUSED;
    private final GameDifficulty gameDifficulty = GameDifficulty.HARDCORE;
    private final InputHandler inputHandler = new InputHandler();
    private IBulletFactory bulletFactory;
    private IEnemyFactory enemyFactory;
    private Label scoreText;
    private Player player;
    private final List<Enemy> enemies = new ArrayList<>();
    private final List<Bullet> bullets = new ArrayList<>();
    private int score = 0;
    public void gameWorld_INIT(AnchorPane root) {
        player = new Player(21, 150);
        enemyFactory = new EnemyFactory(root, enemies);
        bulletFactory = new PlayerBulletFactory(root, bullets);
    }

    public void gameWorld_SETUP(Shape playerView,  Label l1, Label l2) {
        enemies.forEach(Enemy::destroy);
        bullets.forEach(Bullet::destroy);
        cleanDestroyedObjects();
        enemies.clear();
        bullets.clear();

        scoreText = l1; //
        score = 0; //TODO: order
        scoreText.setText(Integer.toString(score)); //


        player.setShoots(10); // TODO: order
        player.setShootText(l2); //
        player.setSprite(playerView);
        player.setPosition(21f,150f);

        float startPosX = 270f;
        float startPosY = 30f;
        float startSpeed = .5f;
        int   startCost = 3;
        for (int i = 0; i < gameDifficulty.mode(); i++) {
            enemyFactory.createEnemy(startPosX, startPosY, startSpeed, startCost);
            if (i >= gameDifficulty.mode() / 2) {
                startCost = 2;
                startSpeed += .75f;
            }
            startPosY +=  4f;
            startPosX += 40f;
        }
    }

    public InputHandler getInputHandler() {
        return inputHandler;
    }

    void update() {
        this.processInput();
        this.allUpdate();
    }

    private void processInput() {
        if (inputHandler.isDKeyPressed()) {
            player.shoot(this);
        } else if (inputHandler.isSKeyPressed()) {
            player.setCurrentDirection(MoveDirection.MOVE_DOWN);
        } else if (inputHandler.isWKeyPressed()) {
            player.setCurrentDirection(MoveDirection.MOVE_UP);
        } else {
            player.setCurrentDirection(null);
        }
    }

    private void allUpdate() {
        //TODO: общий список для всех, кто требует update
        player.update();
        enemies.forEach(GameObject::update);
        bullets.forEach(GameObject::update);

        // TODO: call onCollisionEnter2D()?
        checkCollisions();

        cleanDestroyedObjects();
    }
    private void checkCollisions() {
        for (Bullet bullet : bullets) {
            for (Enemy enemy : enemies) {
                if (bullet.collidesWith(enemy)) {
                    enemy.destroy();
                    bullet.destroy();
                    score += enemy.getCost();
                    updateScore(score);
                    break;
                }
            }
        }
    }
    private void updateScore(int newScore) {
        scoreText.setText(Integer.toString(newScore));
    }
    private void cleanDestroyedObjects() {
        bullets.removeIf(bullet -> {
            if (bullet.isDestroyed()) {
                if (bullet.sprite != null && bullet.sprite.getParent() != null) {
                    ((AnchorPane)bullet.sprite.getParent()).getChildren().remove(bullet.sprite);
                }
                return true;
            }
            return false;
        });

        enemies.removeIf(enemy -> {
            if (enemy.isDestroyed()) {
                if (enemy.sprite != null && enemy.sprite.getParent() != null) {
                    ((AnchorPane)enemy.sprite.getParent()).getChildren().remove(enemy.sprite);
                }
                return true;
            }
            return false;
        });
    }
    public GameState getGameState() { return gameState; }
    public void setGameState(GameState state) { this.gameState = state; }
    public Player getPlayer() { return player; }
    public IBulletFactory getBulletFactory() { return this.bulletFactory; }
}

public class GameController {
    @FXML
    private Rectangle Player;
    @FXML
    private Label Score_number, Shoots_number;
    @FXML
    private AnchorPane Root;

    private GameWorld gameWorld;

    public void setGameWorld(GameWorld g) {
        this.gameWorld = g;
        this.gameWorld.gameWorld_INIT(Root);
        this.gameWorld.gameWorld_SETUP(Player, Score_number, Shoots_number);
    }

    @FXML
    protected void onPauseBtnClick() {
        gameWorld.setGameState(GameState.PAUSED);
    }
    @FXML
    protected void onStartBtnClick() {
        gameWorld.setGameState(GameState.RUN);
        gameWorld.gameWorld_SETUP(Player, Score_number, Shoots_number);
    } //TODO: restart game state
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

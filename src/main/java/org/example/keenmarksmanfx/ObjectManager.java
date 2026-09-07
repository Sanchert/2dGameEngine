package org.example.keenmarksmanfx;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;
import java.util.List;

class ObjectManager {
    private volatile GameState gameState = GameState.PAUSED;
    private final GameDifficulty gameDifficulty = GameDifficulty.HARDCORE;
    private final InputHandler inputHandler = new InputHandler();

    private IBulletFactory bulletFactory;
    private IEnemyFactory enemyFactory;

//    private Label scoreText;

    private Player player;
    private final List<Enemy> enemies = new ArrayList<>();
    private final List<Bullet> bullets = new ArrayList<>();
    private Fire fire;

    private int score = 0;
    private boolean initialised = false;

    private float updateDuration = 0f;

    public float getUpdateDuration() {
        return updateDuration;
    }

    public void gameWorld_INIT(/*AnchorPane root*/) {
        if (initialised) return;
        player = new Player(21, 150);

        fire = new Fire(100, 100);

//        enemyFactory = new EnemyFactory(root, enemies);
//        bulletFactory = new PlayerBulletFactory(root, bullets);
        initialised = true;
    }

    public void gameWorld_SETUP() {
        enemies.forEach(Enemy::destroy);
        bullets.forEach(Bullet::destroy);
        cleanDestroyedObjects();
        enemies.clear();
        bullets.clear();
//
//        scoreText = l1; //
        score = 0; //TODO: order
//        scoreText.setText(Integer.toString(score)); //

        player.setShoots(10); // TODO: order
//        player.setShootText(l2); //
//        player.setSprite(playerView);
        player.setPosition(21f, 150f);

        float enemyStartPosX = 270f;
        float enemyStartPosY = 30f;
        float enemyStartSpeed = 15;
        int enemyStartCost = 3;
        for (int i = 0; i < gameDifficulty.mode(); i++) {
            enemyFactory.createEnemy(enemyStartPosX, enemyStartPosY, enemyStartSpeed, enemyStartCost);
            if (i >= gameDifficulty.mode() / 2) {
                enemyStartCost = 2;
                enemyStartSpeed += .75f;
            }
            enemyStartPosY += 4f;
            enemyStartPosX += 45f;
        }
    }

    public InputHandler getInputHandler() {
        return inputHandler;
    }

//    void update() {
//        processInput();
//        allUpdate();
//        render();
//    }

    public void processInput() {
        if (inputHandler.isKeyPressed(GLFW_KEY_D)) {
            player.shoot(bulletFactory);
        } else if (inputHandler.isKeyPressed(GLFW_KEY_S)) {
            player.setCurrentDirection(MoveDirection.MOVE_DOWN);
        } else if (inputHandler.isKeyPressed(GLFW_KEY_W)) {
            player.setCurrentDirection(MoveDirection.MOVE_UP);
        } else {
            player.setCurrentDirection(null);
        }
    }

    public void update(double step) {
        //TODO: общий список для всех, кто требует update
        player.update(step);
//        fire.updateComponents();
        for (Enemy obj : enemies) {
            obj.update(step);
        }
        for (GameObject obj : bullets) {
            obj.update(step);
        }

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
//        scoreText.setText(Integer.toString(newScore));
    }

    private void cleanDestroyedObjects() {
        bullets.removeIf(bullet -> {
            if (bullet.isDestroyed()) {
                if (bullet.sprite != null && bullet.sprite.getParent() != null) {
//                    ((AnchorPane) bullet.sprite.getParent()).getChildren().remove(bullet.sprite);
                }
                return true;
            }
            return false;
        });

        enemies.removeIf(enemy -> {
            if (enemy.isDestroyed()) {
                if (enemy.sprite != null && enemy.sprite.getParent() != null) {
//                    ((AnchorPane) enemy.sprite.getParent()).getChildren().remove(enemy.sprite);
                }
                return true;
            }
            return false;
        });
    }

    public synchronized GameState getGameState() {
        return gameState;
    }

    public synchronized void setGameState(GameState state) {
        this.gameState = state;
    }
//    public Player getPlayer() { return player; }
//    public IBulletFactory getBulletFactory() { return this.bulletFactory; }
}

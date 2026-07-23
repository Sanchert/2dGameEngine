package org.example.keenmarksmanfx;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;

import java.util.Arrays;
import java.util.List;

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

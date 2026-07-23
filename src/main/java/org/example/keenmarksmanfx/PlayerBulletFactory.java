package org.example.keenmarksmanfx;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.List;

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

package org.example.keenmarksmanfx;

import javafx.scene.shape.Rectangle;

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

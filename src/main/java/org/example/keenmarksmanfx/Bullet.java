package org.example.keenmarksmanfx;

import javafx.scene.shape.Rectangle;

class Bullet extends GameObject {
    private final double SPEED;

    public Bullet(double pos_x, double pos_y) {
        super(pos_x, pos_y);
        SPEED = 3.5f;
    }

    @Override
    public void update(double step) {
        this.move(step);
        this.redraw();
    }

    private void move(double step) {
        setPosition(this.pos_x += SPEED * step, pos_y);
        if (this.pos_x > 800) { // За экраном
            this.destroy();
        }
    }

    @Override
    public Rectangle getBounds() { // TODO: one init collider
        return new Rectangle(pos_x, pos_y, 10, 10);
    }
}

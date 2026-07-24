package org.example.keenmarksmanfx;

import javafx.scene.shape.Rectangle;

class Enemy extends GameObject {
    private final int cost;
    private final double speed;
    private MoveDirection moveDirection = MoveDirection.MOVE_DOWN;

    public Enemy(double pos_x, double pos_y, double speed, int cost) {
        super(pos_x, pos_y);
        this.speed = speed;
        this.cost = cost;
    }

    @Override
    public void update(double step) {
        this.move(step);
        sprite.setLayoutX(pos_x);
        sprite.setLayoutY(pos_y);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(pos_x - 15.0, pos_y - 15.0, 30.0, 30.0);
    }

    public int getCost() {
        return this.cost;
    }

    protected void move(double step) {
        double UPPER_BOUND = 330.0;
        double LOWER_BOUND = 20.0;
        if (this.pos_y >= UPPER_BOUND) {
            moveDirection = MoveDirection.MOVE_DOWN;
        } else if (this.pos_y <= LOWER_BOUND) {
            moveDirection = MoveDirection.MOVE_UP;
        }

        switch (moveDirection) {
            case MOVE_UP -> setPosition(pos_x, pos_y += speed * step);
            case MOVE_DOWN -> setPosition(pos_x, pos_y -= speed * step);
        }
    }
}

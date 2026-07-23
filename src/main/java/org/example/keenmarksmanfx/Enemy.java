package org.example.keenmarksmanfx;

import javafx.scene.shape.Rectangle;

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
            case MOVE_UP -> setPosition(pos_x, pos_y += speed);
            case MOVE_DOWN -> setPosition(pos_x, pos_y -= speed);
        }
    }
}

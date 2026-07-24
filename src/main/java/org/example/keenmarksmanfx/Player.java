package org.example.keenmarksmanfx;

import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;

class Player extends GameObject {
    private final double SPEED;
    private final double MIN_Y;
    private final double MAX_Y;
    private MoveDirection currentDirection = null;
    private int shoots = 10;
    private Label shootsText;
    private int timer = 60;

    public void setCurrentDirection(MoveDirection moveDirection) {
        this.currentDirection = moveDirection;
    }

    public Player(double pos_x, double pos_y) {
        super(pos_x, pos_y);
        SPEED = 1.0;
        MIN_Y = 30.0;
        MAX_Y = 270.0;
    }

    public void setShootText(Label l) {
        this.shootsText = l;
        shootsText.setText(Integer.toString(shoots));
    }

    @Override
    public void update(double step) {
        if (currentDirection != null) {
            move(step);
        }
        timer--;
    }

    public void shoot(IBulletFactory b) {
        if (shoots == 0) return;
        if (timer > 0) {
            return;
        } else {
            timer = 60;
        }
        b.createBullet(pos_x + 10.0, pos_y + 20.0);
        shoots--;
        shootsText.setText(Integer.toString(shoots));
    }

    public void setShoots(int shoots) {
        this.shoots = shoots;
    }

//    public int getShoots() {
//        return this.shoots;
//    }

    protected void move(double step) {
        if (currentDirection == MoveDirection.MOVE_UP) {
            pos_y = Math.max(MIN_Y, pos_y - SPEED * step);
        } else
        if (currentDirection == MoveDirection.MOVE_DOWN) {
            pos_y = Math.min(MAX_Y, pos_y + SPEED * step);
        }
        setPosition(pos_x, pos_y);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(pos_x, pos_y, 40, 50);
    }
}

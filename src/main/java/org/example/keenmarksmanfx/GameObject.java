package org.example.keenmarksmanfx;

import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

abstract class GameObject {
    protected double pos_x, pos_y;
    protected boolean destroyed = false;
    protected Shape sprite = null;

    public GameObject(double x, double y) {
        setPosition(x, y);
    }

    public void setSprite(Shape s) {
        this.sprite = s;
    }

    public abstract void update(double step);

    public abstract Rectangle getBounds();

    public void setPosition(double pos_x, double pos_y) {
        this.pos_x = pos_x;
        this.pos_y = pos_y;
    }

    public void redraw() {
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

    public boolean isDestroyed() {
        return destroyed;
    }
//    public double getX() { return pos_x; }
//    public double getY() { return pos_y; }
}

package org.example.keenmarksmanfx;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

@AutoName(prefix = "SpriteComponent")
public class SpriteComponent extends BaseComponent {
    public ImageView sprite = new ImageView();

    public SpriteComponent(Image sprite, GameObj owner) {
        super(true, owner);
        this.sprite.setImage(sprite);
    }

    @Override
    public void update() {}
}

package org.example.keenmarksmanfx;

import java.util.HashMap;

public class Fire extends GameObj {
    public Fire(float pos_x, float pos_y) {
        super(pos_x, pos_y);
        addComponent(new SpriteComponent(this, -1));
        HashMap<String, Animation> a = new HashMap<>();
        a.put("Idle", new Animation("src/main/resources/Images/burning_loop_3_spritelist.png", 90, 24, 15, 24, 6));
        AnimatorComponent ac = new AnimatorComponent(a, this);
        ac.setActiveAnimation("Idle");
        addComponent(ac);
    }
}

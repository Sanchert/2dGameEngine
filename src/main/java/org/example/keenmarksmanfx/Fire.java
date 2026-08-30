package org.example.keenmarksmanfx;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Fire extends GameObj {
    public Fire(double pos_x, double pos_y) {
        super(pos_x, pos_y);
        ArrayList<Path> paths = new ArrayList<>(
            Arrays.asList(
                Path.of("src/main/resources/Images/burning_loop_3_F1.png"),
                Path.of("src/main/resources/Images/burning_loop_3_F2.png"),
                Path.of("src/main/resources/Images/burning_loop_3_F3.png"),
                Path.of("src/main/resources/Images/burning_loop_3_F4.png"),
                Path.of("src/main/resources/Images/burning_loop_3_F5.png"),
                Path.of("src/main/resources/Images/burning_loop_3_F6.png")
            )
        );
        addComponent(new SpriteComponent(this));

        HashMap<String, Animation> a = new HashMap<>();
        a.put("Idle", new Animation(paths, 100, new ArrayList<>(List.of(16L, 33L, 49L, 65L, 82L, 100L))));
        AnimatorComponent ac = new AnimatorComponent(a, this);
        ac.setActiveAnimation("Idle");
        addComponent(ac);
    }
}

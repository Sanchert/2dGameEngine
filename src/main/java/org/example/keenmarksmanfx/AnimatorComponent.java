package org.example.keenmarksmanfx;

import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AutoName(prefix = "AnimatorComponent")
public class AnimatorComponent extends BaseComponent {
    private final Map<String, Animation> animations;
    private Animation activeAnimation;

    private int activeSpriteInd = 0;
    private long timer = 0;
    private boolean isPlaying = true;
    private int duration = 0;

    private final SpriteComponent spc;

    public AnimatorComponent(@NotNull HashMap<String, Animation> animations, @NotNull GameObj owner) {
        super(true, owner);
        this.animations = animations;
        activeAnimation = animations.entrySet().iterator().next().getValue();
        duration = activeAnimation.getDuration();
        spc = owner.getComponent(SpriteComponent.class);
    }

    public void setActiveAnimation(String animationName) {
        activeAnimation = animations.get(animationName);
        duration = activeAnimation.getDuration();
    }

    public void play() {
        isPlaying = true;
    }

    public void stop() {
        isPlaying = false;
    }

    public void update(long dt) {
        if (isPlaying) {
            timer += dt;

            if (timer >= activeAnimation.getFrameDuration(activeSpriteInd)) {
                timer = 0;
                activeSpriteInd = (activeSpriteInd + 1) % duration;

                spc.setTextureID(activeAnimation.getTextureId());
                spc.setUvCrd(activeAnimation.getUVCrd(activeSpriteInd));
            }
        }
    }
}

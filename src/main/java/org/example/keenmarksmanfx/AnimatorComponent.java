package org.example.keenmarksmanfx;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AutoName(prefix = "AnimatorComponent")
public class AnimatorComponent extends BaseComponent {
    private final Map<String, Animation> animations = new HashMap<>();
    private Animation activeAnimation = null;

    private int activeSpriteInd = 0;
    private float timer = 0;
    private boolean isPlaying = true;

    private final SpriteComponent spc;

    public AnimatorComponent(@NotNull GameObj owner) {
        super(true, owner);
        spc = owner.getComponent(SpriteComponent.class);
    }

    public void addAnimation(String name, Animation animation) {
        if (activeAnimation == null) {
            activeAnimation = animation;
        }
        animations.put(name, animation);
    }

    public void setActiveAnimation(String animationName) {
        activeAnimation = animations.get(animationName);
    }

    public void play() {
        isPlaying = true;
    }

    public void stop() {
        isPlaying = false;
    }

    public void update(float dt) {
        if (isPlaying) {
            timer += dt;

            if (timer >= activeAnimation.getFrameDuration(activeSpriteInd)) {
                timer = 0;
                activeSpriteInd = (activeSpriteInd + 1) % activeAnimation.getFramesCount();
                spc.setTextureID(activeAnimation.getTextureId());
                spc.setUvCrd(activeAnimation.getUVCrd(activeSpriteInd));
            }
        }
    }
}

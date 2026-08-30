package org.example.keenmarksmanfx;

import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.List;

@AutoName(prefix = "AnimatorComponent")
public class AnimatorComponent extends BaseComponent {
    private final HashMap<String, Animation> animations;
    private long currentTime = 0;
    private boolean isPlaying = true;
    private boolean isLooping = true;

    private int activeSpriteInd = 0;
    private Animation activeAnimation;
    private List<Long> frameToFrameIntervals;
    private long totalDuration;



    public AnimatorComponent(@NotNull HashMap<String, Animation> animations, @NotNull GameObj owner) {
        super(true, owner);
        this.animations = animations;
    }

    public void setActiveAnimation(String animationName) {
        this.activeAnimation = animations.get(animationName);
        this.frameToFrameIntervals = activeAnimation.getFrameToFrameIntervals();
        this.totalDuration = activeAnimation.getTotalDuration();
    }

    public void play() {
        isPlaying = true;
    }

    public void stop() {
        isPlaying = false;
    }

    public void update() {
        if (isPlaying) {
            currentTime += 3L; // FIXME

            if (currentTime > frameToFrameIntervals.get(activeSpriteInd)) {
                activeSpriteInd++;
            }
            if (currentTime > totalDuration) {
                currentTime = 0;
                activeSpriteInd = 0;
            }

        }
    }
}

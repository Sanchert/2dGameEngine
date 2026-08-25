package org.example.keenmarksmanfx;

import javafx.scene.image.Image;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@AutoName(prefix = "AnimationComponent")
public class AnimationComponent extends BaseComponent {
    private List<Image> spriteList = new ArrayList<>();
    private final List<Long> frameToFrameIntervals; // TODO: changing the frame rate
    private long totalDuration;
    private long currentTime = 0;
    private boolean isPlaying = false;
    private int activeSpriteInd = 0;

    private final SpriteComponent spc;
    /// @param spriteList frames of an animation in correct order
    /// @param duration time in millis between first and last frames
    public AnimationComponent(List<Image> spriteList, long duration, GameObj owner) {
        super(true, owner);
        this.totalDuration = duration;
        this.spriteList = spriteList;
        this.frameToFrameIntervals = new ArrayList<>(Collections.nCopies(spriteList.size(), duration / spriteList.size()));
        spc = owner.getComponent(SpriteComponent.class);
    }

    /// @param spriteList frames of an animation in correct order
    /// @param duration time in millis between first and last frames
    /// @param intervals frame to frame intervals
    public AnimationComponent(List<Image> spriteList, long duration, ArrayList<Long> intervals, GameObj owner) {
        super(true, owner);
        this.totalDuration = duration;
        this.spriteList = spriteList;
        this.frameToFrameIntervals = intervals;
        spc = owner.getComponent(SpriteComponent.class);
    }

    /// @param paths array of system paths to images in correct order
    /// @param duration time in millis between first and last frames
    public AnimationComponent(ArrayList<Path> paths, long duration, GameObj owner) {
        super(true, owner);
        this.totalDuration = duration;
        paths.forEach(path -> spriteList.add(ImageLoader.loadImageFromPath(path.toString())));
        this.frameToFrameIntervals = new ArrayList<>(Collections.nCopies(spriteList.size(), duration / spriteList.size()));
        spc = owner.getComponent(SpriteComponent.class);
    }

    /// @param paths array of system paths to images in correct order
    /// @param duration time in millis between first and last frames
    /// @param intervals frame to frame intervals
    public AnimationComponent(ArrayList<Path> paths, long duration, ArrayList<Long> intervals, GameObj owner) {
        super(true, owner);
        this.totalDuration = duration;
        this.frameToFrameIntervals = intervals;
        paths.forEach(path -> spriteList.add(ImageLoader.loadImageFromPath(path.toString())));
        spc = owner.getComponent(SpriteComponent.class);
    }

    public void setTotalDuration(long newDuration) {
        this.totalDuration = newDuration;
    }

    public void play() {
        isPlaying = true;
    }

    public void stop() {
        isPlaying = false;
    }

//    public Image getActiveSprite() {
//        return spriteList.get(activeSpriteInd);
//    }

//    public Image update_(double step) {
//        if (isPlaying) {
//            currentTime += 1L;
//
//            if (currentTime > frameToFrameIntervals.get(activeSpriteInd)) {
//                activeSpriteInd++;
//            }
//            if (currentTime > totalDuration) {
//                currentTime = 0;
//                activeSpriteInd = 0;
//            }
//        }
//        return spriteList.get(activeSpriteInd);
//    }

    @Override
    public void update() {
        if (isPlaying) {
            currentTime += 1L;

            if (currentTime > frameToFrameIntervals.get(activeSpriteInd)) {
                activeSpriteInd++;
            }
            if (currentTime > totalDuration) {
                currentTime = 0;
                activeSpriteInd = 0;
            }
            spc.sprite.setImage(spriteList.get(activeSpriteInd));
        }
    }
}

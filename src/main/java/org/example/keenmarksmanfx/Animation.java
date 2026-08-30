package org.example.keenmarksmanfx;

import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Animation {
    private final List<Image> spriteList;
    private final List<Long>  frameToFrameIntervals; // TODO: changing the frame rate
    private final long totalDuration; // TODO: changing duration

    /// @param spriteList frames of an animation in correct order
    /// @param duration time in millis between first and last frames
    public Animation(@NotNull List<Image> spriteList, long duration) {
        this.totalDuration = duration;
        this.spriteList = spriteList;
        this.frameToFrameIntervals = new ArrayList<>(Collections.nCopies(spriteList.size(), duration / spriteList.size()));
    }

    /// @param spriteList frames of an animation in correct order
    /// @param duration time in millis between first and last frames
    /// @param intervals frame to frame intervals
    public Animation(@NotNull List<Image> spriteList, long duration, @NotNull ArrayList<Long> intervals) {
        this.totalDuration = duration;
        this.spriteList = spriteList;
        this.frameToFrameIntervals = intervals;
    }

    /// @param paths array of system paths to images in correct order
    /// @param duration time in millis between first and last frames
    public Animation(@NotNull ArrayList<Path> paths, long duration) {
        this.totalDuration = duration;
        spriteList = new ArrayList<>();
        paths.forEach(path -> spriteList.add(ImageLoader.loadImageFromPath(path.toString())));
        this.frameToFrameIntervals = new ArrayList<>(Collections.nCopies(spriteList.size(), duration / spriteList.size()));
    }

    /// @param paths array of system paths to images in correct order
    /// @param duration time in millis between first and last frames
    /// @param intervals frame to frame intervals
    public Animation(@NotNull ArrayList<Path> paths, long duration, @NotNull ArrayList<Long> intervals) {
        this.totalDuration = duration;
        this.frameToFrameIntervals = intervals;
        spriteList = new ArrayList<>();
        paths.forEach(path -> spriteList.add(ImageLoader.loadImageFromPath(path.toString())));
    }

    /// @param paths array of system paths to images in correct order
    /// @param duration time in millis between first and last frames
    /// @param intervals frame to frame intervals
    public Animation(@NotNull ArrayList<Path> paths, long duration, @NotNull ArrayList<Long> intervals, double scaleX, double scaleY) {
        this.totalDuration = duration;
        this.frameToFrameIntervals = intervals;
        spriteList = new ArrayList<>();
        paths.forEach(path -> {
            Image img = ImageLoader.loadImageFromPath(path.toString());
            spriteList.add(img);
        });
    }

    public Image getSprite(int index) {
        return spriteList.get(index);
    }

    public long getTotalDuration() {
        return totalDuration;
    }

    public List<Long> getFrameToFrameIntervals() {
        return frameToFrameIntervals;
    }
}

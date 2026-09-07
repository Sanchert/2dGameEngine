package org.example.keenmarksmanfx;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Animation {
    private final int textureId;
    private final List<float[]> UVCrd;
    private List<Long> frameDuration;
    private boolean looped = false;
    private final int framesCount;
    private int duration;

    public Animation(@NotNull String fileName, @NotNull List<float[]> crd) {
        textureId =  Loader.textureLoad(fileName, true);
        UVCrd = crd;
        framesCount = crd.size();
    }

    public Animation(@NotNull String fileName,
                     int texWidth, int texHeight,
                     int frameWidth, int frameHeight,
                     int framesCount ) {
        textureId =  Loader.textureLoad(fileName, true);
        this.framesCount = framesCount;
        this.UVCrd = UVBuilder.uvFromPixels(texWidth, texHeight, frameWidth, frameHeight, framesCount);
    }

    public int getTextureId() {
        return textureId;
    }

    public float[] getUVCrd(int frameIndex) {
        return UVCrd.get(frameIndex);
    }

    public void setFrameDuration(List<Long> frameDuration) {
        this.frameDuration = frameDuration;
        frameDuration.forEach(time -> duration += time );
    }

    public Long getFrameDuration(int frameIndex) {
        return frameDuration.get(frameIndex);
    }

    public int getDuration() {
        return  duration;
    }

    public void setLooping(boolean isLooped) {
        this.looped = isLooped;
    }

    public boolean isLooped() {
        return looped;
    }
}

class UVBuilder {
    ///
    /// Extracts UV coordinates of **all frames** from a sprite sheet.
    /// All frames must be located in one line (horizontal strip).
    ///
    /// @param texWidth     width of the texture in pixels
    /// @param texHeight    height of the texture in pixels
    /// @param frameWidth   width of each frame in pixels
    /// @param frameHeight  height of each frame in pixels
    /// @param framesCount  number of frames
    /// @return List of UV coordinates {minU, minV, maxU, maxV} for each frame
    ///
    public static List<float[]> uvFromPixels(int texWidth, int texHeight,
                                             int frameWidth, int frameHeight,
                                             int framesCount) {
        List<float[]> uvCrd = new ArrayList<>(framesCount);

        for (int i = 0; i < framesCount; i++) {
            int x = i * frameWidth;

            uvCrd.add(new float[]{
                    (float) x / texWidth,
                    (float) (texHeight - frameHeight) / texHeight,
                    (float) (x + frameWidth) / texWidth,
                    (float) texHeight / texHeight
            });
        }
        return uvCrd;
    }

    ///
    /// Extracts UV coordinates of **a single frame** from a sprite sheet.
    ///
    /// @param texWidth     width of the texture in pixels
    /// @param texHeight    height of the texture in pixels
    /// @param x            X coordinate of the frame in pixels (top-left corner)
    /// @param y            Y coordinate of the frame in pixels (top-left corner)
    /// @param frameWidth   width of the frame in pixels
    /// @param frameHeight  height of the frame in pixels
    /// @return UV coordinates {minU, minV, maxU, maxV}
    ///
    public static float[] uvFromPixels(int texWidth, int texHeight,
                                       int x, int y,
                                       int frameWidth, int frameHeight) {
        return new float[]{
                (float) x / texWidth,                          // minU
                (float) (texHeight - y - frameHeight) / texHeight, // minV
                (float) (x + frameWidth) / texWidth,           // maxU
                (float) (texHeight - y) / texHeight            // maxV
        };
    }
}

package org.example.keenmarksmanfx;

import org.jetbrains.annotations.NotNull;
import java.util.List;

public class Animation {
    private final int textureId;
    private final List<float[]> UVCrd;
    private List<Long> frameDuration;
    private boolean looped = false;

    public Animation(@NotNull String fileName, @NotNull List<float[]> crd) {
        this.textureId =  Loader.textureLoad(fileName);
        this.UVCrd = crd;
    }

    public int getTextureId() {
        return textureId;
    }

    public float[] getUVCrd(int frameIndex) {
        return UVCrd.get(frameIndex);
    }

    public void setFrameDuration(List<Long> frameDuration) {
        this.frameDuration = frameDuration;
    }

    public Long getFrameDuration(int frameIndex) {
        return frameDuration.get(frameIndex);
    }

    public void setLooping(boolean isLooped) {
        this.looped = isLooped;
    }

    public boolean isLooped() {
        return looped;
    }
}

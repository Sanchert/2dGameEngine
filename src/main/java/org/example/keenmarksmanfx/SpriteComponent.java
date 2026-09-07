package org.example.keenmarksmanfx;

import java.util.Arrays;

@AutoName(prefix = "SpriteComponent")
public class SpriteComponent extends BaseComponent {
    private int textureID;
    private float[] UVCrd;
    private float r = 1.0f, g = 1.0f, b = 1.0f, a = 1.0f;
    private boolean visible = true;
    private int layer;

    public SpriteComponent(GameObj owner, int textureID) {
        super(true, owner);
        this.textureID = textureID;
    }

    public void setRGBA(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public float getR() {
        return r;
    }

    public float getG() {
        return g;
    }

    public float getB() {
        return b;
    }

    public float getA() {
        return a;
    }

    public int getLayer() {
        return layer;
    }

    public int getTextureID() {
        return textureID;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setTextureID(int id) {
        this.textureID = id;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public void setUvCrd(float[] uvCrd) {
        this.UVCrd = uvCrd;
    }

    public void setUVCrd(int texWidth, int texHeight,
                         int x, int y,
                         int frameWidth, int frameHeight) {
        this.UVCrd = UVBuilder.uvFromPixels(texWidth, texHeight, x,  y, frameWidth, frameHeight);
        System.out.println("[SPRITE] Created with textureId: " + textureID);
        System.out.println("[SPRITE] UV: " + Arrays.toString(UVCrd));
    }

    public float[] getUvCrd() {
        return UVCrd;
    }
}

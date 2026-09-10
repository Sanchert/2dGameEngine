package io.github.sanchert.namelessgf;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class RenderSystem {
    private final List<Renderable> renderables = new ArrayList<>();
    private final FloatBuffer vertexBuffer;
    private final int maxBatchSize = 1000;

    public RenderSystem() {
        vertexBuffer = BufferUtils.createFloatBuffer(maxBatchSize * 16);
    }

    public void register(GameObj obj) {
        SpriteComponent sprite = obj.getComponent(SpriteComponent.class);
        // TODO: The Layer can be changed at any time
        if (sprite != null && sprite.isVisible()) {
            renderables.add(new Renderable(sprite, obj));
            renderables.sort(Comparator
                    .comparingInt((Renderable r) -> r.sprite.getLayer())
                    .thenComparingInt(r -> r.sprite.getTextureID()));
        }
    }

    public void clear() {
        renderables.clear();
    }

    public void render() {
        if (renderables.isEmpty()) return;

        int currentTextureId = -1;
        int vertexCount = 0;
        vertexBuffer.clear();

        for (Renderable r : renderables) {
            SpriteComponent sprite = r.sprite;
            GameObj transform = r.transform;

            if (sprite.getTextureID() != currentTextureId && vertexCount > 0) {
                flushBatch(currentTextureId, vertexCount);
                vertexCount = 0;
                vertexBuffer.clear();
            }

            currentTextureId = sprite.getTextureID();

            addVertex(transform, sprite.getUvCrd());
            vertexCount += 4;

            if (vertexCount >= maxBatchSize) {
                flushBatch(currentTextureId, vertexCount);
                vertexCount = 0;
                vertexBuffer.clear();
            }
        }

        if (vertexCount > 0) {
            flushBatch(currentTextureId, vertexCount);
        }
    }

    private void addVertex(GameObj transform, float[] uv) {
        float x = transform.pos_x;
        float y = transform.pos_y;
        float w = transform.width * transform.scaleX;
        float h = transform.height * transform.scaleY;

        // Left Down
        vertexBuffer.put(x).put(y).put(uv[0]).put(uv[1]);
        // Right Down
        vertexBuffer.put(x + w).put(y).put(uv[2]).put(uv[1]);
        // Right Up
        vertexBuffer.put(x + w).put(y + h).put(uv[2]).put(uv[3]);
        // Left Up
        vertexBuffer.put(x).put(y + h).put(uv[0]).put(uv[3]);
    }

    private void flushBatch(int textureId, int vertexCount) {
        vertexBuffer.flip();

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);

        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);

        vertexBuffer.position(0);
        GL11.glVertexPointer(2, GL11.GL_FLOAT, 16, vertexBuffer);
        vertexBuffer.position(2);
        GL11.glTexCoordPointer(2, GL11.GL_FLOAT, 16, vertexBuffer);

        GL11.glDrawArrays(GL11.GL_QUADS, 0, vertexCount);

        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }

    private record Renderable(SpriteComponent sprite, GameObj transform) {
    }
}

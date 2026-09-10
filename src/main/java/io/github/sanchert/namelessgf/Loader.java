package io.github.sanchert.namelessgf;

import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.system.MemoryStack.stackPush;

class Loader {
    private static final Map<String, Integer> textureCache = new HashMap<>();

    public static int textureLoad(String path, boolean pixelArt) {
        return textureCache.computeIfAbsent(path, _ -> loadTextureFromFile(path, pixelArt)); // NOTE: path can be not unique
    }

    private static int loadTextureFromFile(String path, boolean pixelArt) {
        int textureId = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);

        try (MemoryStack stack = stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            ByteBuffer buf = STBImage.stbi_load(path, width, height, channels, 4);

            if (buf == null) {
                System.out.println("[ERR] Failed to load image: " + path + "\n" + STBImage.stbi_failure_reason());
                return -1;
            }

            GL11.glTexImage2D(
                    GL11.GL_TEXTURE_2D,
                    0,
                    GL11.GL_RGBA,
                    width.get(0),
                    height.get(0),
                    0,
                    GL11.GL_RGBA,
                    GL11.GL_UNSIGNED_BYTE,
                    buf
            );
            int filter = pixelArt ? GL_NEAREST : GL_LINEAR;
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, filter);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, filter);

            STBImage.stbi_image_free(buf);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return -1;
        }

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        return textureId;
    }
}

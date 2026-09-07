package org.example.keenmarksmanfx;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.*;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Launcher {
    private static final String TITLE = "Game";
    private static final float S_PER_UPDATE = 0.016f;

    private static long window;
    GameObj player; // TODO: not here
    GameObj p1;
    GameObj p2;
    GameObj p3;
    GameState state = GameState.RUN;

    private final RenderSystem renderSystem = new RenderSystem();
    private final List<GameObj> objects = new ArrayList<>(); // TODO: not here

    static void main() {
        new Launcher().run();
    }

    private int fixedUpdateCalls = 0;
    private int renderCalls = 0;
    public void run() {
        windowInit();
        initObjects();

        long prev = System.currentTimeMillis();
        float lag = 0;

        int frameCount = 0;
        long lastFpsCheck = System.currentTimeMillis();

        while (!glfwWindowShouldClose(window)) {
            if (state == GameState.RUN) {
                long current = System.currentTimeMillis();
                float elapsed = (current - prev) / 1000.0f;
                prev = current;
                lag += elapsed;

                while (lag >= S_PER_UPDATE) {
                    fixedUpdate();
                    fixedUpdateCalls++; // NOTE
                    lag -= S_PER_UPDATE;
                }
            } else {
                prev = System.currentTimeMillis();
            }

            render();
            renderCalls++; // NOTE

            // ВЫВОД РАЗ В СЕКУНДУ
            if (System.currentTimeMillis() - lastFpsCheck >= 1000) {
                System.out.println("FPS: " + frameCount + ", FixedUpdate: " + fixedUpdateCalls + ", Render: " + renderCalls);
                // ОБНУЛЕНИЕ СЧЁТЧИКОВ
                frameCount = 0;
                fixedUpdateCalls = 0;
                renderCalls = 0;
                lastFpsCheck = System.currentTimeMillis();
            }
            frameCount++;

            glfwPollEvents();
        }

        GLFW_cleanup();
    }

    private void windowInit() {
        // 1. Настройка обработчика ошибок GLFW
        GLFWErrorCallback.createPrint(System.err).set();

        // 2. Инициализация GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Не удалось инициализировать GLFW");
        }

        // 3. Получаем первичный монитор
        long monitor = glfwGetPrimaryMonitor();
        GLFWVidMode vidMode = glfwGetVideoMode(monitor);

        // 4. Настройка параметров окна для полноэкранного режима
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE); // в полноэкранном режиме изменение размера не нужно

        // ВАЖНО: отключаем режим окна, чтобы GLFW знал, что мы создаём полноэкранное окно
        // (по умолчанию GLFW_DECORATED включен)

        // 5. Создание полноэкранного окна
        // передаём указатель на монитор, а размеры берём из видеорежима монитора
        assert vidMode != null;
        window = glfwCreateWindow(
                vidMode.width(),
                vidMode.height(),
                TITLE,
                monitor,
                NULL
        );

        if (window == NULL) {
            throw new RuntimeException("Не удалось создать полноэкранное окно");
        }

        // 6. Делаем OpenGL-контекст текущим
        glfwMakeContextCurrent(window);

        // 7. Включаем вертикальную синхронизацию (VSync) на тестовой машине 31-32 fps. При отключении ~13000 fps
//        glfwSwapInterval(1);

        // 8. Показываем окно
        glfwShowWindow(window);

        // 9. Инициализируем привязки OpenGL
        GL.createCapabilities();

        // Разрешаем рендер 2д текстур и устанавливаем прозрачность
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        // 10. Устанавливаем цвет очистки экрана
        glClearColor(0.1f, 0.2f, 0.4f, 1.0f);

        setupOrthographicProjection();
    }

    private void setupOrthographicProjection() {
        // Получаем размеры окна
        int[] width = new int[1];
        int[] height = new int[1];
        glfwGetWindowSize(window, width, height);

        // Настраиваем 2D-проекцию (пиксельные координаты)
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, width[0], height[0], 0, -1, 1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
    }

    private void initObjects() {
        // Пример: создаем объект с текстурой
        player = new GameObj(250f,350f);
        p1= new GameObj(350f, 350f);
        p2= new GameObj(450f, 350f);
        p3= new GameObj(550f, 350f);
        player.width = 28f;
        player.height = 44f;
        p1.width = 28f;
        p1.height = 44f;
        p2.width = 28f;
        p2.height = 44f;
        p3.width = 28f;
        p3.height = 44f;

        int textureId = Loader.textureLoad("src/main/resources/Images/Knight.png", true);
        SpriteComponent sprite = new SpriteComponent(player, textureId);
        sprite.setTextureID(textureId);
        sprite.setUVCrd(28,44,0,0,28,44);
        player.addComponent(sprite);
        p1.addComponent(sprite);
        p2.addComponent(sprite);
        p3.addComponent(sprite);
        player.scaleX = 7;
        player.scaleY = 7;
        p1.scaleX = 7;
        p1.scaleY = 7;

        p2.scaleX = 7;
        p2.scaleY = 7;

        p3.scaleX = 7;
        p3.scaleY = 7;

        // Регистрируем в рендерере
        renderSystem.register(player);
        renderSystem.register(p1);
        renderSystem.register(p2);
        renderSystem.register(p3);
        objects.add(player);
        objects.add(p1);
        objects.add(p2);
        objects.add(p3);
    }

    float timeCounter;
    private void fixedUpdate() {
        fixedUpdateCalls++;
        timeCounter += S_PER_UPDATE;

        float amplitude = 20f;
        float waveLength = 400f;
        float speed = 6f;

        float phase1 = (player.pos_x / waveLength) + timeCounter * speed;
        float phase2 = (p1.pos_x / waveLength) + timeCounter * speed;
        float phase3 = (p2.pos_x / waveLength) + timeCounter * speed;
        float phase4 = (p3.pos_x / waveLength) + timeCounter * speed;
        player.pos_y = 350f + (float)Math.sin(phase1) * amplitude;
        p1.pos_y = 350f + (float)Math.sin(phase2) * amplitude;
        p3.pos_y = 350f + (float)Math.sin(phase3) * amplitude;
        p2.pos_y = 350f + (float)Math.sin(phase4) * amplitude;
        System.out.printf("p1: %.2f, p2: %.2f, p3: %.2f, p4: %.2f\n",
                player.pos_y, p1.pos_y, p2.pos_y, p3.pos_y);
    }

    /* each frame check for new visual resources (somewhere)
    if new object was added
        If a new object was added, check if it has a sprite component.
        if (prevSize != objects.size()) {
            if (new object has a sprite component) {
                loadVisualResources(object.getComponent(spriteComponent.Class).sprite);
            }
        }
       loadVisualResources(){
            all objects with sprite (getSpriteComponent or annotation)
            objects.foreach(obj -> resources_VISUAL.add(obj.getSprite));
       }
     */

    private void render() {
        renderCalls++;
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            renderSystem.render();
            glfwSwapBuffers(window);
            glfwPollEvents();
    }

    private void GLFW_cleanup() {
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }
}

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

class RenderSystem {
    private final List<Renderable> renderables = new ArrayList<>();
    private final FloatBuffer vertexBuffer;
    private final int maxBatchSize = 1000; // максимум спрайтов в одном батче

    public RenderSystem() {
        // Выделяем буфер для вершин (4 вершины * 4 компонента (x, y, u, v) * 4 байта)
        vertexBuffer = BufferUtils.createFloatBuffer(maxBatchSize * 4 * 4);
    }

    // Регистрация объекта для рендеринга
    public void register(GameObj obj) {
        SpriteComponent sprite = obj.getComponent(SpriteComponent.class);

        if (sprite != null && sprite.isVisible()) {
            renderables.add(new Renderable(sprite, obj));
            renderables.sort(Comparator.comparingInt(r -> r.sprite.getTextureID()));
        }
    }

    // Очистка перед каждым кадром
    public void clear() {
        renderables.clear();
    }

    // Рендеринг всех объектов
    public void render() {
        if (renderables.isEmpty()) return;

        int currentTextureId = -1;
        int vertexCount = 0;
        vertexBuffer.clear();

        for (Renderable r : renderables) {
            SpriteComponent sprite = r.sprite;
            GameObj transform = r.transform;

            // Если текстура сменилась — отрисовываем текущий батч
            if (sprite.getTextureID() != currentTextureId && vertexCount > 0) {
                flushBatch(currentTextureId, vertexCount);
                vertexCount = 0;
                vertexBuffer.clear();
            }

            currentTextureId = sprite.getTextureID();

            // Добавляем вершины спрайта в буфер
            addVertex(transform, sprite.getUvCrd());
            vertexCount += 4;

            // Если батч заполнен — отрисовываем
            if (vertexCount >= maxBatchSize) {
                flushBatch(currentTextureId, vertexCount);
                vertexCount = 0;
                vertexBuffer.clear();
            }
        }

        // Отрисовываем оставшиеся
        if (vertexCount > 0) {
            flushBatch(currentTextureId, vertexCount);
        }
    }

    private void addVertex(GameObj transform, float[] uv) {
        float x = transform.pos_x;
        float y = transform.pos_y;
        float w = transform.width * transform.scaleX;
        float h = transform.height * transform.scaleY;

        // Левый нижний
        vertexBuffer.put(x).put(y).put(uv[0]).put(uv[1]);
        // Правый нижний
        vertexBuffer.put(x + w).put(y).put(uv[2]).put(uv[1]);
        // Правый верхний
        vertexBuffer.put(x + w).put(y + h).put(uv[2]).put(uv[3]);
        // Левый верхний
        vertexBuffer.put(x).put(y + h).put(uv[0]).put(uv[3]);
    }

    private void flushBatch(int textureId, int vertexCount) {
        vertexBuffer.flip();

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);

        // Настройка атрибутов вершин (позиция и UV)
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);

        vertexBuffer.position(0);
        GL11.glVertexPointer(2, GL11.GL_FLOAT, 4 * 4, vertexBuffer);
        vertexBuffer.position(2);
        GL11.glTexCoordPointer(2, GL11.GL_FLOAT, 4 * 4, vertexBuffer);

        // Рисуем как QUADS (4 вершины на спрайт)
        GL11.glDrawArrays(GL11.GL_QUADS, 0, vertexCount);

        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }

    // Внутренний класс для хранения данных
        private record Renderable(SpriteComponent sprite, GameObj transform) {
    }
}
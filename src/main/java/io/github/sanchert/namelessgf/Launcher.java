package io.github.sanchert.namelessgf;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import java.util.*;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Launcher {
//    private static Launcher instance = null;
    private static final String TITLE = "Game";
    private static final float S_PER_UPDATE = 0.016f;
    private static long window;
    GameObj player; // TODO: not here
    GameObj candle;
    GameState state = GameState.RUN;

    private final RenderSystem renderSystem = new RenderSystem();
    private final List<GameObj> objects = new ArrayList<>(); // TODO: not here

//    public Launcher getInstance() {
//        if (instance == null)
//            instance = new Launcher();
//        return instance;
//    }

//    private Launcher() {}

    static void main() {
        new Launcher().run();
    }
    // NOTE: this is a part of logger
    // TODO: move logging to a separate class
    private int fixedUpdateCalls = 0;
    private int renderCalls = 0;
    private int updateCalls = 0;

    // NOTE: This function must notify the system that
    //       the program has started running, and nothing else.
    public void run() {
        // TODO: other place
        windowInit();
        // ------------------
        // TODO
        initObjects();
        // ------------------
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
                    lag -= S_PER_UPDATE;
                }
                update(elapsed);
            } else {
                prev = System.currentTimeMillis();
            }
            render();

            if (System.currentTimeMillis() - lastFpsCheck >= 1000) {
                System.out.println("FPS: " + frameCount + ", FixedUpdate: " + fixedUpdateCalls + ", Render: " + renderCalls + ", Update: " + updateCalls);

                frameCount = 0;
                fixedUpdateCalls = 0;
                renderCalls = 0;
                updateCalls = 0;
                lastFpsCheck = System.currentTimeMillis();
            }
            frameCount++;

            glfwPollEvents();
        }
        // ------------------
        GLFW_cleanup();
    }
    private void update(float dt) {
        updateCalls++;
        ac.update(dt);
    }
    private void windowInit() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Не удалось инициализировать GLFW");
        }

        long monitor = glfwGetPrimaryMonitor();
        GLFWVidMode vidMode = glfwGetVideoMode(monitor);

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE); // полноэкранный режим

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

        glfwMakeContextCurrent(window);

        // VSync на тестовой машине 31-32 fps. При отключении ~13000 fps
//        glfwSwapInterval(1);

        glfwShowWindow(window);

        GL.createCapabilities();

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

//        glClearColor(0.1f, 0.2f, 0.4f, 1.0f);
        glClearColor(.0f, .0f, .0f, 1.0f);
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
    AnimatorComponent ac;
    private void initObjects() {
        candle = new GameObj(0f, 0f);
        candle.width = 732f;
        candle.height = 1024f;


        player = new GameObj(100f,0f);


        player.width = 28f;
        player.height = 44f;


        int textureId = Loader.textureLoad("src/main/resources/Images/Knight.png", true);
        SpriteComponent sprite = new SpriteComponent(textureId, player);
        sprite.setTextureID(textureId);
        sprite.setUVCrd(28,44,0,0,28,44);
        sprite.setLayer(1);
        player.addComponent(sprite);



        Animation burningLoop = new Animation("src/main/resources/Images/burning_loop_3_spritelist.png", 90, 24, 15, 24, 6);
        burningLoop.setFrameDuration(.1f);
        ac = new AnimatorComponent(player);
        ac.addAnimation("Idle", burningLoop);
        player.addComponent(ac);

        int candleTexId = Loader.textureLoad("src/main/resources/Images/candle.jpg", false);
        SpriteComponent sc = new SpriteComponent(candleTexId, candle);
        sc.setTextureID(candleTexId);
        sc.setUVCrd(732, 1024, 0,0, 732,1024);
        candle.addComponent(sc);

        player.scaleX = 15;
        player.scaleY = 15;

        renderSystem.register(player);
        renderSystem.register(candle);

        objects.add(candle);
        objects.add(player);
    }

    float timeCounter;
    private void fixedUpdate() {
        fixedUpdateCalls++; // NOTE: logging
        timeCounter += S_PER_UPDATE;
        float amplitude = 20f;
        float waveLength = 400f;
        float speed = 6f;

        float phase1 = (player.pos_x / waveLength) + timeCounter * speed;
        player.pos_y = 20+ (float)Math.sin(phase1) * amplitude;

    }

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


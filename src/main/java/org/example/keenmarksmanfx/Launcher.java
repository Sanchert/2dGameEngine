package org.example.keenmarksmanfx;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Launcher {
    private static final String TITLE = "Game";
    private static long window;

    static void main() {
        new Launcher().run();
    }

    public void run() {
        GLFW_init();
        // while (true)
        // update();
        render();
        GLFW_cleanup();
    }

    private void GLFW_init() {
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

        // 7. Включаем вертикальную синхронизацию (VSync)
        glfwSwapInterval(1);

        // 8. Показываем окно
        glfwShowWindow(window);

        // 9. Инициализируем привязки OpenGL
        GL.createCapabilities();

        // 10. Устанавливаем цвет очистки экрана
        glClearColor(0.1f, 0.2f, 0.4f, 1.0f);
    }


    /* each frame check for new visual resources (somewhere)
       loadAllResources(){
            all objects with sprite (getSpriteComponent or annotation)
            objects.foreach(obj -> resources_VISUAL.add(obj.getSprite));
       }
     */

    private void render(/*objects to draw*/) {
        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            // ----------------------DRAW HERE----------------------
            // transformComponent + spriteComponent (where and what)
            /* resources_VISUAL.foreach(obj -> {
                // glDraw(obj.sprite, obj.transform);
            // });
            -------------------------------------------------------
            */

            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    private void GLFW_cleanup() {
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }
}

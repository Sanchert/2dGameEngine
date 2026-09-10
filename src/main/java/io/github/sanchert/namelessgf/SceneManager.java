package io.github.sanchert.namelessgf;

import java.util.HashMap;

public class SceneManager {
    private SceneManager instance = null;

    private static final HashMap<String, Scene> scenes = new HashMap<>();
    private static Scene activeScene;


    private SceneManager() {}

    public SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }

    public static void addScene(Scene sc) { scenes.put(sc.getName(), sc); }
    public static Scene getActiveScene() { return activeScene; }
    public static void loadScene(String sceneName) { activeScene = scenes.get(sceneName); }
}

package io.github.sanchert.namelessgf;

import java.util.ArrayList;
import java.util.List;

@AutoName(prefix = "Scene")
public class Scene {

    private String name;
    public String getName() { return name; }

    private final List<GameObj> sceneObjects = new ArrayList<>();

    public void addObject(GameObj go) { sceneObjects.add(go); }
    public void removeObject(GameObj go) { sceneObjects.remove(go); }
    public List<GameObj> getSceneObjects() { return sceneObjects; }
    public GameObj getByTag(String tag) {
        return sceneObjects.stream()
                .filter(go -> go.getTag().equals(tag))
                .findFirst()
                .orElse(null);
    }
}

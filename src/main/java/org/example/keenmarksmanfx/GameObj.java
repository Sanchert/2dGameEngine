package org.example.keenmarksmanfx;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class GameObj {
    private final HashMap<String, IComponent> byName = new HashMap<>();
    private final HashMap<Class<? extends IComponent>, List<IComponent>> byType = new HashMap<>();

    public void addComponent(IComponent c) {
        byName.put(NameGenerator.generateName(c.getClass()), c);
        List<IComponent> l = byType.get(c.getClass());
        if (l == null) {
            l = new ArrayList<>();
        }
        l.add(c);
        byType.put(c.getClass(), l);
    }

    @SuppressWarnings("unchecked")
    public <T> T getComponent(String name) {
        return (T) byName.get(name);
    }

    @SuppressWarnings("unchecked")
    public <T> T getComponent(Class<T> type) {
        List<IComponent> list = byType.get(type);
        return list != null && !list.isEmpty() ? (T) list.getFirst() : null;
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getComponents(Class<T> type) {
        return (List<T>) byType.getOrDefault(type, Collections.emptyList());
    }

    public void removeComponent(String name) {
        IComponent c = byName.remove(name);
        byType.remove(c.getClass());
    }

//    public void removeComponents(Class<? extends IComponent> type) {}

//    public <T extends IComponent> boolean removeComponentIf(Class<T> class_, Predicate<T> condition) {
//        return false;
//    }

    protected double pos_x, pos_y;
    protected double width, height;
    protected double scaleX = 1.0, ScaleY = 1.0;
    protected double rotation;
    protected boolean destroyed = false;

    public GameObj(double pos_x, double pos_y) {
        this.pos_x = pos_x;
        this.pos_y = pos_y;
    }
}

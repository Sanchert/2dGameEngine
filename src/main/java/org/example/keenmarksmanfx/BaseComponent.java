package org.example.keenmarksmanfx;

public abstract class BaseComponent implements IComponent {
    private final boolean removable;
    private final GameObj owner;
    public BaseComponent(boolean removable, GameObj owner) {
        this.removable = removable;
        this.owner = owner;
    }

    public <T extends GameObj> T getOwnerAs(Class<T> type) {
        return type.cast(owner);
    }
}

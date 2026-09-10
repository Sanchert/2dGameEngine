package io.github.sanchert.namelessgf;

public abstract class BaseComponent implements IComponent {
    protected final boolean removable;
    protected final GameObj owner;
    public BaseComponent(boolean removable, GameObj owner) {
        this.removable = removable;
        this.owner = owner;
    }

    public <T extends GameObj> T getOwnerAs(Class<T> type) {
        return type.cast(owner);
    }
}

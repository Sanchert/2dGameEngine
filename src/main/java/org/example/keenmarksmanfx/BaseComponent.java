package org.example.keenmarksmanfx;

public abstract class BaseComponent implements IComponent {
    private final boolean removable;

    public BaseComponent(boolean removable) {
        this.removable = removable;
    }
}

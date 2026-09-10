package io.github.sanchert.namelessgf;

public abstract class ScriptComponent extends BaseComponent implements IScript{

    public ScriptComponent(boolean removable, GameObj owner) {
        super(removable, owner);
    }

    @Override
    public abstract void update();
}

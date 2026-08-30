package org.example.keenmarksmanfx;

import javafx.scene.input.KeyCode;

import java.util.HashSet;
import java.util.Set;

class InputHandler {
    private final Set<Long> keyPressed = new HashSet<>();
    private final Set<Long> keyJustPressed = new HashSet<>();

    public void handleKeyPressed(long keyCode) {
        keyJustPressed.remove(keyCode);
        if (!keyPressed.contains(keyCode)) {
            keyJustPressed.add(keyCode);
        }
        keyPressed.add(keyCode);
    }

    public void handleKeyReleased(long keyCode) {
        keyPressed.remove(keyCode);
        keyJustPressed.remove(keyCode);
    }

    public boolean isKeyPressed(long keyCode) {
        return keyPressed.contains(keyCode);
    }

    public boolean isKeyJustPressed(long keyCode) {
        return keyJustPressed.contains(keyCode);
    }
}

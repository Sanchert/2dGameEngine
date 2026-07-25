package org.example.keenmarksmanfx;

import javafx.scene.input.KeyCode;

import java.util.HashSet;
import java.util.Set;

class InputHandler {
    private final Set<KeyCode> keyPressed = new HashSet<>();
    private final Set<KeyCode> keyJustPressed = new HashSet<>();

    public void handleKeyPressed(KeyCode keyCode) {
        keyJustPressed.remove(keyCode);
        if (!keyPressed.contains(keyCode)) {
            keyJustPressed.add(keyCode);
        }
        keyPressed.add(keyCode);
//        System.out.println(keyPressed);
    }

    public void handleKeyReleased(KeyCode keyCode) {
        keyPressed.remove(keyCode);
        keyJustPressed.remove(keyCode);
    }

    public boolean isKeyPressed(KeyCode keyCode) {
        return keyPressed.contains(keyCode);
    }

    public boolean isKeyJustPressed(KeyCode keyCode) {
        return keyJustPressed.contains(keyCode);
    }
}

package org.example.keenmarksmanfx;

import javafx.scene.input.KeyCode;

class InputHandler {
    //    private final Set<KeyCode> activeKeys = new HashSet<>();
    private boolean D_Pressed = false;
    private boolean W_Pressed = false;
    private boolean S_Pressed = false;

    public void handleKeyPressed(KeyCode keyCode) {
//        activeKeys.add(keyCode);
        switch (keyCode) {
            case KeyCode.D -> D_Pressed = true;
            case KeyCode.W -> W_Pressed = true;
            case KeyCode.S -> S_Pressed = true;
        }
    }

    public void handleKeyReleased(KeyCode keyCode) {
//        activeKeys.remove(keyCode);
        switch (keyCode) {
            case KeyCode.D -> D_Pressed = false;
            case KeyCode.W -> W_Pressed = false;
            case KeyCode.S -> S_Pressed = false;
        }
    }

    public boolean isDKeyPressed() {
        return D_Pressed;
    }

    public boolean isSKeyPressed() {
        return S_Pressed;
    }

    public boolean isWKeyPressed() {
        return W_Pressed;
    }
}

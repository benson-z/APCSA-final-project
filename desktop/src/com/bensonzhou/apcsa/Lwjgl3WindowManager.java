package com.bensonzhou.apcsa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Window;

public class Lwjgl3WindowManager implements WindowManager {
    // Override the setWindowPosition method from the interface
    @Override
    public void setWindowPosition(int x, int y) {
        // Get the Lwjgl3Graphics window
        Lwjgl3Window window = ((Lwjgl3Graphics) Gdx.graphics).getWindow();
        window.setPosition(x, y);
    }
}

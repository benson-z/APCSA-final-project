package com.bensonzhou.apcsa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class WindowSizeManager {
    private static final double INITIAL_HEIGHT = 1000;
    private static final double INITIAL_WIDTH = 1600;
    private static final double MIN_HEIGHT = 300;
    private static final double MIN_WIDTH = 500;
    private static final double RETRACT_CONSTANT = 1.0;
    private static final double EXPAND_CONSTANT = 180;
    public double leftBound;
    public double rightBound;
    public double topBound;
    public double bottomBound;
    private double leftWallVel;
    private double rightWallVel;
    private double topWallVel;
    private double bottomWallVel;
    private double lastMouseX;
    private double lastMouseY;
    private double mouseX;
    private double mouseY;

    public WindowSizeManager() {
        double centerX = Gdx.graphics.getDisplayMode().width / 2.0;
        double centerY = Gdx.graphics.getDisplayMode().height / 2.0;
        leftBound = centerX - INITIAL_WIDTH / 2;
        rightBound = centerX + INITIAL_WIDTH / 2;
        topBound = centerY - INITIAL_HEIGHT / 2;
        bottomBound = centerY + INITIAL_HEIGHT / 2;
    }

    public int getPosX() {
        return (int) leftBound;
    }

    public int getPosY() {
        return (int) topBound;
    }

    public int getHeight() {
        return (int) (bottomBound - topBound);
    }

    public int getWidth() {
        return (int) (rightBound - leftBound);
    }

    public int getRelativeX(double x) {
        return (int) (x - leftBound);
    }

    public int getRelativeY(double y) {
        return (int) (bottomBound - y);
    }

    public double getMouseX() {
        return mouseX;
    }

    public double getMouseY() {
        return mouseY;
    }

    public void resize(OrthographicCamera camera) {
        Gdx.graphics.setWindowedMode(getWidth(), getHeight());
        Main.windowManager.setWindowPosition(getPosX(), getPosY());
        camera.viewportWidth = getWidth();
        camera.viewportHeight = getHeight();
        camera.position.x = camera.viewportWidth / 2;
        camera.position.y = camera.viewportHeight / 2;
    }

    public void applyImpulse(String side, double value) {
        switch (side) {
            case "left":
                leftWallVel = Math.max(leftWallVel, value);
                break;
            case "right":
                rightWallVel = Math.max(rightWallVel, value);
                break;
            case "top":
                topWallVel = Math.max(topWallVel, value);
                break;
            case "bottom":
                bottomWallVel = Math.max(bottomWallVel, value);
                break;
        }
    }

    public void update(double deltaTime) {
        updatePhysics(deltaTime);
        updateBounds(deltaTime);
        if (lastMouseY != Gdx.input.getY()) {
            mouseX = lastMouseX = leftBound + Gdx.input.getX();
            mouseY = lastMouseY = topBound + Gdx.input.getY();
        }
    }

    public void updatePhysics(double deltaTime) {
        leftWallVel = Math.max(-RETRACT_CONSTANT * Math.pow(getWidth() - MIN_WIDTH, 0.6), leftWallVel - deltaTime * EXPAND_CONSTANT);
        rightWallVel = Math.max(-RETRACT_CONSTANT * Math.pow(getWidth() - MIN_WIDTH, 0.6), rightWallVel - deltaTime * EXPAND_CONSTANT);
        topWallVel = Math.max(-RETRACT_CONSTANT * Math.pow(getHeight() - MIN_HEIGHT, 0.6), topWallVel - deltaTime * EXPAND_CONSTANT);
        bottomWallVel = Math.max(-RETRACT_CONSTANT * Math.pow(getHeight() - MIN_HEIGHT, 0.6), bottomWallVel - deltaTime * EXPAND_CONSTANT);
    }

    public void updateBounds(double deltaTime) {
        leftBound = Math.min(leftBound - deltaTime * RETRACT_CONSTANT * leftWallVel, rightBound - MIN_WIDTH);
        topBound = Math.min(topBound - deltaTime * RETRACT_CONSTANT * topWallVel, bottomBound - MIN_HEIGHT);
        rightBound = Math.max(rightBound + deltaTime * RETRACT_CONSTANT * rightWallVel, leftBound + MIN_WIDTH);
        bottomBound = Math.max(bottomBound + deltaTime * RETRACT_CONSTANT * bottomWallVel, topBound + MIN_HEIGHT);
        leftBound = Math.max(leftBound, 0);
        rightBound = Math.min(rightBound, Gdx.graphics.getDisplayMode().width);
        topBound = Math.max(topBound, 0);
        bottomBound = Math.min(bottomBound, Gdx.graphics.getDisplayMode().height);
    }
}

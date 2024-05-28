package com.bensonzhou.apcsa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;

public class Player {
    private static final double MAX_SPEED = 100;
    private static final double ACCEL_STEP = 0.05;
    private static final double PREVIEW_THICKNESS = 20;
    private double xPos;
    private double yPos;
    private double xVel;
    private double yVel;
    private ArrayList<Shot> shots;

    public Player() {
        xPos = Gdx.graphics.getDisplayMode().width / 2.0;
        yPos = Gdx.graphics.getDisplayMode().height / 2.0;
        xVel = 0;
        yVel = 0;
        shots = new ArrayList<>();
    }

    public void handleInputs() {
        boolean xInputGiven = false;
        boolean yInputGiven = false;
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            yVel -= ACCEL_STEP;
            yInputGiven = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            yVel += ACCEL_STEP;
            yInputGiven = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            xVel -= ACCEL_STEP;
            xInputGiven = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            xVel += ACCEL_STEP;
            xInputGiven = true;
        }
        if (!yInputGiven) {
            yVel = Math.signum(yVel) * (Math.abs(yVel) - ACCEL_STEP);
        }
        if (!xInputGiven) {
            xVel = Math.signum(xVel) * (Math.abs(xVel) - ACCEL_STEP);
        }

        xVel = Math.min(Math.max(-1, xVel), 1);
        yVel = Math.min(Math.max(-1, yVel), 1);

        double normal = Math.sqrt(xVel * xVel + yVel * yVel);
        if (normal > 1.0) {
            xVel /= normal;
            yVel /= normal;
        }
    }

    public void updatePos(WindowSizeManager manager, double deltaTime) {
        xPos += deltaTime * xVel * MAX_SPEED;
        yPos += deltaTime * yVel * MAX_SPEED;
        xPos = Math.max(xPos, manager.leftBound);
        xPos = Math.min(xPos, manager.rightBound);
        yPos = Math.max(yPos, manager.topBound);
        yPos = Math.min(yPos, manager.bottomBound);
    }

    public static void draw(ShapeRenderer shapeRenderer, double xPos, double yPos) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.ROYAL); // Set the color of the circle
        shapeRenderer.circle((float) xPos, (float) yPos, 20); // Draw a circle at (100, 100) with a radius of 50
        shapeRenderer.end();
    }

    public static void drawTargetingLine(ShapeRenderer shapeRenderer, float x1, float y1, float angle, float length) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.SKY);
        angle -= Math.PI / 2;
        // Calculate the end point
        float dx = (float) Math.cos(angle) * length;
        float dy = (float) Math.sin(angle) * length;
        float x2 = x1 + dx;
        float y2 = y1 + dy;

        // Calculate the perpendicular offset for thickness
        float halfThickness = (float) PREVIEW_THICKNESS / 2;
        float offsetX = (float) Math.sin(angle) * halfThickness;
        float offsetY = (float) -Math.cos(angle) * halfThickness;

        // Draw the two triangles forming the thick line as a quad
        shapeRenderer.triangle(x1 - offsetX, y1 - offsetY, x1 + offsetX, y1 + offsetY, x2 + offsetX, y2 + offsetY);
        shapeRenderer.triangle(x1 - offsetX, y1 - offsetY, x2 + offsetX, y2 + offsetY, x2 - offsetX, y2 - offsetY);
        shapeRenderer.end();
    }

    public void shoot(WindowSizeManager sizeManager) {
        double angle = Math.atan2(sizeManager.getMouseX() - getXPos(), sizeManager.getMouseY() - getYPos());
        shots.add(new Shot(getXPos(), getYPos(), angle));
    }

    public double getXPos() {
        return xPos;
    }
    public double getYPos() {
        return yPos;
    }

    public ArrayList<Shot> getShots() {
        return shots;
    }
}

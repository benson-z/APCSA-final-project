package com.bensonzhou.apcsa;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Shot {
    private static final double SPEED = 600;
    private double xPos;
    private double yPos;
    private double xVel;
    private double yVel;
    private boolean alive = true;

    public Shot(double xPos, double yPos, double angle) {
        this.xPos = xPos;
        this.yPos = yPos;
        xVel = Math.sin(angle);
        yVel = Math.cos(angle);
    }

    public static void draw(ShapeRenderer shapeRenderer, double xPos, double yPos) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE); // Set the color of the circle
        shapeRenderer.circle((float) xPos, (float) yPos, 7); // Draw a circle at (100, 100) with a radius of 50
        shapeRenderer.end();
    }

    public void updatePos(WindowSizeManager manager, double deltaTime) {
        xPos += deltaTime * xVel * SPEED;
        yPos += deltaTime * yVel * SPEED;
    }

    public String outOfBounds(WindowSizeManager manager) {
        if (!alive) {
            return "";
        }
        if (yPos > manager.bottomBound) {
            return "bottom";
        }

        if (yPos < manager.topBound) {
            return "top";
        }

        if (xPos > manager.rightBound) {
            return "right";
        }

        if (xPos < manager.leftBound) {
            return "left";
        }
        return "none";
    }

    public void unalive() {
        alive = false;
    }

    public double getXPos() {
        return xPos;
    }

    public double getYPos() {
        return yPos;
    }
}

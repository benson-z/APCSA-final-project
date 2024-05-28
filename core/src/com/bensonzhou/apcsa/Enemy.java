package com.bensonzhou.apcsa;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Enemy {
    private static final double spawnRadius = 0;
    private static final double SPEED = 1;
    private double xPos, yPos;
    private double xVel, yVel;
    private boolean alive;

    public Enemy(WindowSizeManager sizeManager) {
        alive = true;
        double angle = Math.random() * 2 * Math.PI;
        double dist = Math.sqrt(Math.pow(sizeManager.getWidth(), 2) + Math.pow(sizeManager.getHeight(), 2)) + Math.random() * spawnRadius;

        xPos = (sizeManager.leftBound + sizeManager.rightBound) / 2 + Math.cos(angle) * dist;
        yPos = (sizeManager.topBound + sizeManager.bottomBound) / 2 + Math.sin(angle) * dist;

        xVel = -Math.cos(angle) * SPEED;
        yVel = -Math.sin(angle) * SPEED;
    }

    public static void draw(ShapeRenderer shapeRenderer, double xPos, double yPos, boolean alive) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(alive ? Color.PURPLE : Color.WHITE); // Set the color of the circle
        shapeRenderer.circle((float) xPos, (float) yPos, 15); // Draw a circle at (100, 100) with a radius of 50
        shapeRenderer.end();
    }

    public boolean hit(double x, double y) {
        if (Math.sqrt(Math.pow(x - xPos, 2) + Math.pow(y - yPos, 2)) < 25) {
            alive = false;
        }
        return !alive;
    }

    public void draw(ShapeRenderer shapeRenderer) {
        if (alive) ;
    }

    public void update(WindowSizeManager sizeManager) {
        xPos += xVel;
        yPos += yVel;
    }

    public boolean isAlive() {
        return alive;
    }

    public double getxPos() {
        return xPos;
    }

    public double getyPos() {
        return yPos;
    }
}

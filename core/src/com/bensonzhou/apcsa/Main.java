package com.bensonzhou.apcsa;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;

/**
 * Main class that extends ApplicationAdapter to create a game application using libGDX.
 */
public class Main extends ApplicationAdapter {
    public static WindowManager windowManager;
    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;
    private BitmapFont font;
    private WindowSizeManager sizeManager;
    private ArrayList<Enemy> enemies;
    private Player p;
    private double health = 10;
    private int score = 0;
    private int counter = 0;

    /**
     * Constructor for the Main class.
     *
     * @param windowManager an instance of WindowManager.
     */
    public Main(WindowManager windowManager) {
        this.windowManager = windowManager;
    }

    /**
     * Initializes the game components.
     */
    @Override
    public void create() {
        sizeManager = new WindowSizeManager();

        camera = new OrthographicCamera(sizeManager.getWidth(), sizeManager.getHeight());
        camera.setToOrtho(false); // Set camera to orthographic (needed for 2d)

        shapeRenderer = new ShapeRenderer();

        sizeManager.resize(camera); // set initial window size

        p = new Player();
        batch = new SpriteBatch();
        font = new BitmapFont();
        enemies = new ArrayList<>();

        // Shoot every 0.5 seconds
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                p.shoot(sizeManager);
            }
        }, 1, 0.5f);

        // spawn an enemy every 0.9 seconds
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                enemies.add(new Enemy(sizeManager));
            }
        }, 0, 0.9f);
    }

    /**
     * Main render loop for the game.
     */
    @Override
    public void render() {
        // Handle player inputs and movement
        handlePlayerActions();

        // Handle shots fired by the player
        handleShots();

        // Handle enemy actions
        handleEnemies();

        // Update camera and size manager
        updateCameraAndSizeManager();

        // Clear screen and draw UI
        clearScreenAndDrawUI();

        // Draw all game objects
        drawObjects();
    }

    /**
     * Handles player actions such as inputs and position updates.
     */
    private void handlePlayerActions() {
        p.handleInputs();
        p.updatePos(sizeManager, Gdx.graphics.getDeltaTime()); // based on delta time between frames
    }

    /**
     * Handles the shots fired by the player.
     */
    private void handleShots() {
        for (int i = 0; i < p.getShots().size(); i++) {
            Shot shot = p.getShots().get(i);

            shot.updatePos(sizeManager, Gdx.graphics.getDeltaTime());

            // Check if shot is out of bounds
            String hit = shot.outOfBounds(sizeManager);
            if (!hit.equals("none")) {
                // Apply impulse if shot is out of bounds
                sizeManager.applyImpulse(hit, 150);

                p.getShots().remove(i);
                i--;
            }
        }
    }

    /**
     * Handles the enemies in the game.
     */
    private void handleEnemies() {
        // Remove dead enemies from the list
        for (int i = 0; i < enemies.size(); i++) {
            if (!enemies.get(i).isAlive()) {
                enemies.remove(i);
                i--;
            }
        }

        for (Enemy enemy : enemies) {
            // Decrement health if enemy hits the player
            if (enemy.hit(p.getXPos(), p.getYPos())) {
                health--;
            }
            // Check for collisions with shots
            for (Shot shot : p.getShots()) {
                if (enemy.hit(shot.getXPos(), shot.getYPos())) {
                    shot.unalive();
                    score++;
                }
            }
        }
    }

    /**
     * Updates the camera and size manager.
     */
    private void updateCameraAndSizeManager() {
        counter++;
        // Update the camera and size manager every other frame
        // Not doing this will result in the game freezing
        if (counter % 2 == 0) {
            camera.update();
            sizeManager.resize(camera);
        }

        sizeManager.update(Gdx.graphics.getDeltaTime());
    }

    /**
     * Clears the screen and draws the UI.
     */
    private void clearScreenAndDrawUI() {
        Gdx.gl.glClearColor(0, 0, 0, 1); // Set clear color to black
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear the screen

        batch.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); // Set projection matrix for batch

        batch.begin();
        font.draw(batch, "Health: " + health, sizeManager.getRelativeX(p.getXPos()) - 40, sizeManager.getRelativeY(p.getYPos()) + 40); // Draw health
        font.draw(batch, "Score: " + score, 10, sizeManager.getHeight() - 10); // Draw score
        batch.end();
    }

    /**
     * Draws all game objects.
     */
    private void drawObjects() {
        // Set projection matrix for shape renderer
        shapeRenderer.setProjectionMatrix(camera.combined);

        // Draw shots
        for (Shot shot : p.getShots()) {
            Shot.draw(shapeRenderer, sizeManager.getRelativeX(shot.getXPos()), sizeManager.getRelativeY(shot.getYPos()));
        }

        // Draw enemies
        for (Enemy enemy : enemies) {
            enemy.update(sizeManager); // Update enemy
            Enemy.draw(shapeRenderer, sizeManager.getRelativeX(enemy.getxPos()), sizeManager.getRelativeY(enemy.getyPos()), enemy.isAlive());
        }

        // Draw player targeting line and player
        Player.drawTargetingLine(shapeRenderer, sizeManager.getRelativeX(p.getXPos()), sizeManager.getRelativeY(p.getYPos()), (float) Math.atan2(sizeManager.getMouseX() - p.getXPos(), sizeManager.getMouseY() - p.getYPos()), 40);
        Player.draw(shapeRenderer, sizeManager.getRelativeX(p.getXPos()), sizeManager.getRelativeY(p.getYPos()));
    }

    /**
     * Disposes of game resources.
     */
    @Override
    public void dispose() {
    }
}

package com.bensonzhou.apcsa;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Timer;

import javax.print.attribute.standard.Sides;
import java.util.ArrayList;

public class Main extends ApplicationAdapter {
    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;
    private BitmapFont font;
    public static WindowManager windowManager;
    private WindowSizeManager sizeManager;
    private ArrayList<Enemy> enemies;
    Player p;
    private double health = 10;
    private int score = 0;
    int counter = 0;

    public Main(WindowManager windowManager) {
        Main.windowManager = windowManager;
    }

    @Override
    public void create() {
        sizeManager = new WindowSizeManager();

        camera = new OrthographicCamera(sizeManager.getWidth(), sizeManager.getHeight());
        camera.setToOrtho(false);

        // Initialize the ShapeRenderer
        shapeRenderer = new ShapeRenderer();

        sizeManager.resize(camera);

        p = new Player();
        batch = new SpriteBatch();
        font = new BitmapFont();
        enemies = new ArrayList<>();

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                p.shoot(sizeManager);
            }
        }, 1, 0.5f);
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                enemies.add(new Enemy(sizeManager));
            }
        }, 0, 0.9f);
    }

    @Override
    public void render() {
        p.handleInputs();
        p.updatePos(sizeManager, Gdx.graphics.getDeltaTime());
        if (Gdx.input.isButtonJustPressed(0)) {
        }
        for (int i = 0; i < p.getShots().size(); i++) {
            p.getShots().get(i).updatePos(sizeManager, Gdx.graphics.getDeltaTime());
            String hit = p.getShots().get(i).outOfBounds(sizeManager);
            if (!hit.equals("none")) {
                sizeManager.applyImpulse(hit, 150);
                p.getShots().remove(i);
                i--;
            }
        }
        for (int i = 0; i < enemies.size(); i++) {
            if (!enemies.get(i).isAlive()) {
                enemies.remove(i);
                i--;
            }
        }
        for (Enemy e : enemies) {
            if (e.hit(p.getXPos(), p.getYPos())) {
                health--;
                System.out.println(health);
            }
            for (Shot s : p.getShots()) {
                if (e.hit(s.getXPos(), s.getYPos())) {
                    score++;
                }
            }
        }
        counter++;
        if (counter % 2 == 0) {
            camera.update();
            sizeManager.resize(camera);
        }

        sizeManager.update(Gdx.graphics.getDeltaTime());

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.begin();
        font.draw(batch, "Health: " + health, sizeManager.getRelativeX(p.getXPos()) - 40, sizeManager.getRelativeY(p.getYPos()) + 40);
        font.draw(batch, "Score: " + score, 10, sizeManager.getHeight() - 10);
        batch.end();
        shapeRenderer.setProjectionMatrix(camera.combined);

        for (Shot s : p.getShots()) {
            Shot.draw(shapeRenderer, sizeManager.getRelativeX(s.getXPos()), sizeManager.getRelativeY(s.getYPos()));
        }
        for (Enemy e : enemies) {
            e.update(sizeManager);
            Enemy.draw(shapeRenderer, sizeManager.getRelativeX(e.getxPos()), sizeManager.getRelativeY(e.getyPos()), e.isAlive());
        }
        Player.drawTargetingLine(shapeRenderer, sizeManager.getRelativeX(p.getXPos()), sizeManager.getRelativeY(p.getYPos()), (float) Math.atan2(sizeManager.getMouseX() - p.getXPos(), sizeManager.getMouseY() - p.getYPos()), 40);
        Player.draw(shapeRenderer, sizeManager.getRelativeX(p.getXPos()), sizeManager.getRelativeY(p.getYPos()));

    }

    @Override
    public void dispose() {
    }
}

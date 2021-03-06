package com.beariksonstudios.endlessshooter.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.beariksonstudios.endlessshooter.EndlessShooter;
import com.beariksonstudios.endlessshooter.classes.Enemy;
import com.beariksonstudios.endlessshooter.classes.Player;
import com.beariksonstudios.endlessshooter.core.*;
import com.beariksonstudios.endlessshooter.tools.WorldMap;

public class Test implements Screen {
    private final Stage uiStage;
    private final Player player;
    private final Enemy enemy;
    private World world;
    private InputHandler input;
    private WorldMap worldMap;
    private OrthographicCamera camera;
    private Box2DDebugRenderer boxRenderer;
    private ESStage gameStage;


    public Test(EndlessShooter endlessShooter) {
        world = new World(new Vector2(0, -43.0f), true); // real life gravity mul by ten
        world.setContactListener(new PhysicsContactListener());
        float worldScale = Assets.WORLD_TO_BOX;

        uiStage = new Stage();
        boxRenderer = new Box2DDebugRenderer();

        TiledMap map = Assets.mapLoader.load("data/maps/test/test.tmx");
        SpriteBatch batch = new SpriteBatch();
        worldMap = new WorldMap(world, map, batch, worldScale);

        gameStage = new ESStage(1f, batch, worldMap);
        camera = gameStage.getCamera();

        player = new Player(worldMap.getStartPosition(), world, worldScale, camera);
        enemy = new Enemy(worldMap.getEnemyStartPosition(), world, worldScale, camera);
        enemy.setTarget(player);
        input = new InputHandler(endlessShooter);

        HUD hud = new HUD(uiStage, world);

        camera.position.set(player.getPosition(), 0);
        camera.update();

        gameStage.addActor(player);
        gameStage.addActor(enemy);
    }

    public void update() {
        world.step(Gdx.graphics.getDeltaTime(), 6, 2);

        input.handleInput(player);
        gameStage.update();

        //Update camera position w/player
        camera.position.set(player.getPosition(), 0);
        float cameraMin = 0f;
        float cameraHalfHeight = camera.viewportHeight / 2;
        float cameraBottom = camera.position.y - cameraHalfHeight;
        if (cameraBottom < cameraMin)
            camera.position.y = cameraMin + cameraHalfHeight;

        camera.update();

        uiStage.act();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.30f, 0.50f, 0.95f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update();

        gameStage.draw();
        boxRenderer.render(world, camera.combined);
        uiStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        uiStage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        camera.viewportWidth = Assets.BOX_TO_WORLD;
        camera.viewportHeight = Assets.BOX_TO_WORLD * height / width;
        camera.update();
    }

    @Override
    public void show() {
        // TODO Auto-generated method stub

    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub

    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose() {
        world.dispose();
        uiStage.dispose();
        gameStage.dispose();
        boxRenderer.dispose();
    }

}

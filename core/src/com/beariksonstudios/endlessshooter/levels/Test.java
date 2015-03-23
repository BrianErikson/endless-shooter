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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.beariksonstudios.endlessshooter.classes.Character;
import com.beariksonstudios.endlessshooter.classes.Enemy;
import com.beariksonstudios.endlessshooter.core.Assets;
import com.beariksonstudios.endlessshooter.core.InputHandler;
import com.beariksonstudios.endlessshooter.core.PhysicsContactListener;
import com.beariksonstudios.endlessshooter.tools.Debugger;
import com.beariksonstudios.endlessshooter.tools.WorldMap;

public class Test implements Screen {
    private final Stage uiStage;
    private final Character player;
    private final Enemy enemy;
    private World world;
    private InputHandler input;
    private WorldMap worldMap;
    private OrthographicCamera camera;
    private Box2DDebugRenderer boxRenderer;
    private SpriteBatch batch;


    public Test() {
        world = new World(new Vector2(0, -43.0f), true); // real life gravity mul by ten
        world.setContactListener(new PhysicsContactListener());
        float zoom = 1f;
        float worldScale = Assets.WORLD_TO_BOX;
        camera = new OrthographicCamera(Assets.BOX_TO_WORLD,
                Assets.BOX_TO_WORLD * (Gdx.graphics.getHeight() / Gdx.graphics.getWidth()));
        camera.zoom = zoom;

        uiStage = new Stage(new FitViewport(camera.viewportWidth, camera.viewportHeight));
        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);
        boxRenderer = new Box2DDebugRenderer();

        TiledMap map = Assets.mapLoader.load("data/maps/test/test.tmx");
        worldMap = new WorldMap(world, map, batch, worldScale);

        Skin labelSkin = Assets.manager.get("data/uiskin.json", Skin.class);
        Debugger debugger = new Debugger(labelSkin, uiStage, world);
        debugger.setPosition(uiStage.getWidth() / 2, uiStage.getHeight() / 2);
        uiStage.addActor(debugger);

        player = new Character(worldMap.getStartPosition(), world, worldScale, camera);
        enemy = new Enemy(worldMap.getEnemyStartPosition(), world, worldScale, camera);
        input = new InputHandler();

        camera.position.set(player.getPosition(), 0);
        camera.update();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.30f, 0.50f, 0.95f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        worldMap.draw(camera);
        boxRenderer.render(world, camera.combined);

        //Update camera position w/player
        camera.position.set(player.getPosition(), 0);
        float cameraMin = 0f;
        float cameraHalfHeight = camera.viewportHeight / 2;
        float cameraBottom = camera.position.y - cameraHalfHeight;
        if (cameraBottom < cameraMin)
            camera.position.y = cameraMin + cameraHalfHeight;

        camera.update();

        input.handleInput(player);

        uiStage.draw();
        
        batch.begin();
        player.draw(boxRenderer, camera, batch);
        enemy.draw(boxRenderer, camera, batch);
        batch.end();

        world.step(1 / 50f, 6, 2);
        world.clearForces();
        
       

    }

    @Override
    public void resize(int width, int height) {
        // TODO: Fix UI resizing. Currently wipes out the scene rendering when this is enabled
        //uiStage.getViewport().update((int)camera.viewportWidth, (int)camera.viewportHeight);

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
        // TODO Auto-generated method stub

    }

}

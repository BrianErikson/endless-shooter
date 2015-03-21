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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.beariksonstudios.endlessshooter.classes.Character;
import com.beariksonstudios.endlessshooter.core.Assets;
import com.beariksonstudios.endlessshooter.core.InputHandler;
import com.beariksonstudios.endlessshooter.core.PhysicsContactListener;
import com.beariksonstudios.endlessshooter.tools.Debugger;
import com.beariksonstudios.endlessshooter.tools.WorldMap;

public class Test implements Screen {
	private World world;
	private InputHandler input;
	private WorldMap worldMap;
	private OrthographicCamera camera;
	private OrthographicCamera UICamera;
	private Box2DDebugRenderer boxRenderer;
	private SpriteBatch batch;
	private float zoom;
	private float worldScale;
	private Skin labelSkin;
	private Debugger debugger;
	private float UIScale;
	private Character character;

	
	public Test() {
		world = new World(new Vector2(0,-43.0f), true); // real life gravity mul by ten
		world.setContactListener(new PhysicsContactListener());
		zoom = 1f;
		UIScale = 100.0f;
		worldScale = Assets.WORLD_TO_BOX;
		camera = new OrthographicCamera(Assets.BOX_TO_WORLD,
                Assets.BOX_TO_WORLD * (Gdx.graphics.getHeight()/Gdx.graphics.getWidth()));
        camera.zoom = zoom;

		UICamera = new OrthographicCamera(camera.viewportWidth*UIScale, camera.viewportHeight*UIScale);
		batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);
		boxRenderer = new Box2DDebugRenderer();
		
		TiledMap map = Assets.mapLoader.load("data/maps/test/test.tmx");;
		worldMap = new WorldMap(world, map, batch, worldScale);
		
		labelSkin = Assets.manager.get("data/uiskin.json", Skin.class);
		debugger = new Debugger(labelSkin, batch, world);
		character = new Character(worldMap.getStartPosition(), world, worldScale, camera);
		
		
		input = new InputHandler();
		
		UICamera.translate(UICamera.viewportWidth/2, UICamera.viewportHeight/2);
		camera.position.set(character.getPosition(), 0);
		camera.update();
		UICamera.update();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.30f, 0.50f, 0.95f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		
		worldMap.draw(camera);
		boxRenderer.render(world, camera.combined);
		
		//Update camera position w/player
		camera.position.set(character.getPosition(), 0);
		float cameraMin = 0f;
        float cameraBottom = camera.position.y - (camera.viewportHeight/2);
        System.out.println("CameraBottom: " + cameraBottom);
		if(cameraBottom < cameraMin)
            camera.position.y = cameraMin + (camera.viewportHeight/2);
		
		camera.update();
		batch.setProjectionMatrix(UICamera.combined);
		batch.begin();
		debugger.drawFPS(delta, new Vector2(0,UICamera.viewportHeight - debugger.getLabelHeight()));
		debugger.drawPhys(delta, new Vector2(0,UICamera.viewportHeight - (debugger.getLabelHeight()*2)));

        input.handleInput(character);
        character.draw(boxRenderer, camera, batch);
		
		batch.end();
		
		world.step(1/50f, 6, 2);
		world.clearForces();
	}

	@Override
	public void resize(int width, int height) {
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

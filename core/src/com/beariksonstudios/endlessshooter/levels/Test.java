package com.beariksonstudios.endlessshooter.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.beariksonstudios.endlessshooter.classes.StockClass.RangeCharData;
import com.beariksonstudios.endlessshooter.core.Assets;
import com.beariksonstudios.endlessshooter.core.Bullet;
import com.beariksonstudios.endlessshooter.core.Character;
import com.beariksonstudios.endlessshooter.core.InputHandler;
import com.beariksonstudios.endlessshooter.core.PlayerFactory;
import com.beariksonstudios.endlessshooter.core.PlayerFactory.PlayerType;
import com.beariksonstudios.endlessshooter.props.Shruiken;
import com.beariksonstudios.endlessshooter.props.Shruiken.Data;
import com.beariksonstudios.endlessshooter.props.SniperBullet.SBulletData;
import com.beariksonstudios.endlessshooter.tools.Debugger;
import com.beariksonstudios.endlessshooter.tools.WorldMap;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

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
	private Array<Character> characters;

	
	public Test() {
		world = new World(new Vector2(0,-43.0f), true); // real life gravity mul by ten
		world.setContactListener(new ContactListener(){


			@Override
			public void beginContact(Contact contact) {
				boolean readyPickup = false;
				Body collideCharBody = null;
				Body collideBulletBody = null;
				Character characterBulletOwner = null;
				Bullet pickupBullet = null;
				Character collideChar = null;
				for (int i = 0; i < 2; i ++){
					Body body = null;
					if (i == 0){
						body = contact.getFixtureA().getBody();
					}
					else {
						body = contact.getFixtureB().getBody();
					}
					Object obj = body.getUserData();
					if (obj instanceof Data){
						collideBulletBody = body;
						Data data = (Data) obj;
						readyPickup = data.bullet.isReadyPickup();
						characterBulletOwner = data.character;
						pickupBullet = data.bullet;
						if (data.bounces >= 2){
							body.setLinearVelocity(0, 0);
							data.isRotating = false;
							body.setUserData(data);
						}
						else {
							data.bounces ++;
							body.setUserData(data);
						}
					}
					else if (obj instanceof SBulletData){
						collideBulletBody = body;
						SBulletData sbdata = (SBulletData) obj;
						readyPickup = sbdata.bullet.isReadyPickup();
						body.setLinearVelocity(0,0);
						sbdata.isRotating = false;
						body.setUserData(sbdata);
						characterBulletOwner = sbdata.character;
						pickupBullet = sbdata.bullet;
					}
					else if (obj instanceof RangeCharData){
						RangeCharData cData = (RangeCharData) obj;
						collideCharBody = body;
						collideChar = cData.character;
					}
				}
				if (readyPickup && collideCharBody != null){
					//characterBulletOwner.removeBullet(pickupBullet);
					//collideChar.addBullet();
				}
			}
		
			@Override
			public void endContact(Contact contact) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
				// TODO Auto-generated method stub
				
			}
			
		});
		zoom = 26.0f;
		UIScale = 100.0f;
		worldScale = Assets.WORLD_TO_BOX;
		camera = new OrthographicCamera(Assets.TILE_SIZE*zoom*Assets.WORLD_TO_BOX, 
										Assets.TILE_SIZE*zoom*Assets.WORLD_TO_BOX);
		UICamera = new OrthographicCamera(camera.viewportWidth*UIScale, camera.viewportHeight*UIScale);
		batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);
		boxRenderer = new Box2DDebugRenderer();
		
		TiledMap map = Assets.mapLoader.load("data/maps/test/test.tmx");;
		worldMap = new WorldMap(world, map, batch, worldScale);
		
		labelSkin = Assets.manager.get("data/uiskin.json", Skin.class);
		debugger = new Debugger(labelSkin, batch, world);
		characters = new Array<Character>();
		characters.add(PlayerFactory.buildChar(true, PlayerType.SNIPER, worldMap.getStartPosition(), 
											world, worldScale, camera));
		
		
		input = new InputHandler();
		
		UICamera.translate(UICamera.viewportWidth/2, UICamera.viewportHeight/2);
		camera.translate(camera.viewportWidth/2, camera.viewportHeight/2);
		camera.update();
		UICamera.update();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.30f, 0.50f, 0.95f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		
		worldMap.draw(camera);
		boxRenderer.render(world, camera.combined);
		
		
		
		batch.setProjectionMatrix(UICamera.combined);
		batch.begin();
		debugger.drawFPS(delta, new Vector2(0,UICamera.viewportHeight - debugger.getLabelHeight()));
		debugger.drawPhys(delta, new Vector2(0,UICamera.viewportHeight - (debugger.getLabelHeight()*2)));
		
		for (int i = 0; i < PlayerFactory.getCharacterNum(); i++) {
			input.handleInput(characters.get(i));
			characters.get(i).draw(boxRenderer, camera, batch);
		}
		
		batch.end();
		
		world.step(1/50f, 6, 2);
		world.clearForces();
		
	}

	@Override
	public void resize(int width, int height) {
		//camera.viewportHeight = height;
		//camera.viewportWidth = width;
		//camera.update();
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

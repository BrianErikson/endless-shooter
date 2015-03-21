package com.beariksonstudios.endlessshooter.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.beariksonstudios.endlessshooter.classes.Character;
import com.beariksonstudios.endlessshooter.classes.Character.RangeCharData;
import com.beariksonstudios.endlessshooter.classes.Character.STATE;
import com.beariksonstudios.endlessshooter.core.Assets;
import com.beariksonstudios.endlessshooter.core.Bullet;
import com.beariksonstudios.endlessshooter.core.InputHandler;
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
					if (obj instanceof RangeCharData){
						RangeCharData cData = (RangeCharData) obj;
						collideCharBody = body;
						collideChar = cData.character;
						
					}
				}
				Object objA = contact.getFixtureA().getBody().getUserData();
				Object objB = contact.getFixtureB().getBody().getUserData();
				System.out.println("collision yo");
				if(objA instanceof RangeCharData){
					RangeCharData cData = (RangeCharData) objA;
					collideChar = cData.character;
					System.out.println("OBJA char");
					if(objB instanceof String){
						if(objB == "object ground"){
							collideChar.setState(STATE.STANDING);
							System.out.println("objB ground");
						}
						else if(objB == "object walls"){
							if(collideChar.getState() == STATE.JUMPING || collideChar.getState() == STATE.FALLING){
								System.out.println("objB wall");
							}
						}
					}
				}
				else if(objB instanceof RangeCharData){
					RangeCharData cData = (RangeCharData) objB;
					collideChar = cData.character;
					System.out.println("OBJB char");
					if(objA instanceof String){
						System.out.println(objA);
						if(objA.equals("object ground")){
							collideChar.setState(STATE.STANDING);
							System.out.println("objA ground");
						}
						else if(objA.equals("object walls")){
							if(collideChar.getState() == STATE.JUMPING || collideChar.getState() == STATE.FALLING){
								System.out.println("objA wall");
							}
						}
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
		zoom = 1f;
		UIScale = 100.0f;
		worldScale = Assets.WORLD_TO_BOX;
		camera = new OrthographicCamera(Gdx.graphics.getWidth()/Assets.BOX_TO_WORLD,
                Gdx.graphics.getWidth()/Assets.BOX_TO_WORLD * (Gdx.graphics.getHeight()/Gdx.graphics.getWidth()));
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
		float cameraMin = 13.2f;
		if(camera.position.y < cameraMin)
			camera.position.y = cameraMin;
		
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
        System.out.println("Resize! " +width + " " + height);
        camera.viewportWidth = width/Assets.BOX_TO_WORLD;
		camera.viewportHeight = camera.viewportWidth * height / width;
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

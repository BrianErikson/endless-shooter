package com.beariksonstudios.endlessshooter.classes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.beariksonstudios.endlessshooter.core.Assets;
import com.beariksonstudios.endlessshooter.core.Bullet;
import com.beariksonstudios.endlessshooter.core.Character;
import com.beariksonstudios.endlessshooter.core.PlayerFactory.Player;
import com.beariksonstudios.endlessshooter.props.Shruiken;
import com.beariksonstudios.endlessshooter.props.Shruiken.Data;
import com.beariksonstudios.endlessshooter.props.SniperBullet.SBulletData;
import com.beariksonstudios.endlessshooter.tools.WorldMap;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.MathUtils;

public abstract class StockClass implements Character {
	protected Vector2 pos;
	protected int moveSpeed;
	protected int jumpSpeed;
	protected World world;
	protected Body body;
	protected float scale;
	protected enum STATE {
		STANDING,
		CROUCHING,
		JUMPING,
		FALLING
	};
	
	protected STATE state;
	protected float width;
	protected float height;
	protected Player player;
	protected boolean isLocal;
	protected Camera camera;
	protected Array<Bullet> bArray;
	protected long bounceTimer;
	protected int bulletCount;
	
	public StockClass(boolean local, Player player, Vector2 startPos, World physicsWorld, 
			float scale, Camera camera) {
		bounceTimer = 100;
		this.camera = camera;
		this.pos = startPos;
		this.scale = scale;
		this.world = physicsWorld;
		this.player = player;
		this.isLocal = local;
		moveSpeed = 10;
		jumpSpeed = 10;
		bArray = new Array <Bullet> ();
		BodyDef bd = new BodyDef();
		bd.allowSleep = false; // object does not need to sleep due to player control
		bd.position.set(pos);
		bd.type = BodyDef.BodyType.DynamicBody;
		
		bulletCount = 2;
		
		this.body = world.createBody(bd);
		body.setFixedRotation(true);
		body.setUserData(new RangeCharData(this));
		
		FixtureDef fd = new FixtureDef();
		fd.density = 80.7f; // weight of average human
		fd.friction = 2f;
		PolygonShape shape = new PolygonShape();
		width = 1.6f * 0.3048f * 0.5f * Assets.TILE_SIZE * scale;
		height = 5.9f * 0.3048f * 0.5f * Assets.TILE_SIZE * scale;
		shape.setAsBox(width, height); // 33% average height and width in feet (converted into meters 0.3048)
		fd.shape = shape;
		
		this.body.createFixture(fd);
	}
	public class RangeCharData {
		public Character character;
		
		public RangeCharData (Character character){
			this.character = character;
		}
	}

	@Override
	public void jump() {
		if (state == STATE.STANDING) {
			state = STATE.JUMPING;
			float force = body.getMass()*jumpSpeed;
			body.applyLinearImpulse(new Vector2(0, force), new Vector2(0,1), true);
		}
	}

	@Override
	public void crouch() {
		if (state == STATE.STANDING) {
			// duck? drop through thin platforms?
			state = STATE.CROUCHING;
		}
	}

	@Override
	public void moveLeft() {
		float force = -moveSpeed * body.getMass();
		body.applyForceToCenter(new Vector2(force, 0), true);
		state = STATE.STANDING;
	}

	@Override
	public void moveRight() {
		float force = moveSpeed * body.getMass();
		body.applyForceToCenter(new Vector2(force, 0), true);
		state = STATE.STANDING;
	}

	@Override
	public void fire() {
	}

	@Override
	public void draw(Box2DDebugRenderer renderer, Camera camera, SpriteBatch batch) {
		
		if (body.getLinearVelocity().y > 0.0f) state = STATE.JUMPING;
		if (body.getLinearVelocity().y == 0.0f && state != STATE.CROUCHING) state = STATE.STANDING;
		if (body.getLinearVelocity().y < 0.0f && state != STATE.JUMPING) state = STATE.FALLING;
		
		for (int i = 0; i < bArray.size; i ++){
			bArray.get(i).draw(camera, batch);
		}
		bounceTimer -= Gdx.graphics.getDeltaTime();
		if (bounceTimer <= 0){
			for (Bullet bullet: bArray){
				if(bullet.getBody().getLinearVelocity().equals(new Vector2 (0,0))){
					if(bullet.getBody().getUserData() instanceof Data){
						Data data = (Data) bullet.getBody().getUserData();
						data.bullet.setReadyPickup(true);
					}
					else if (bullet.getBody().getUserData() instanceof SBulletData){
						SBulletData data = (SBulletData) bullet.getBody().getUserData();
						data.bullet.setReadyPickup(true);
					}
					float force = bullet.getBody().getMass()*2f;
					bullet.getBody().applyLinearImpulse(new Vector2 (0, force), new Vector2 (0,1), true);
				}
			}
			bounceTimer = 100;
		}
	}

	@Override
	public Vector2 getPosition() {return body.getPosition();}

	@Override
	public Vector2 getWorldCenterPosition() {return body.getWorldCenter();}

	@Override
	public Vector2 getSize() {
		return new Vector2(width*2, height*2);
	}
	
	@Override
	public boolean isLocal() {
		return isLocal;
	}
	
	@Override
	public Player getPlayer() {
		return player;
	}
	@Override
	public void removeBullet (Bullet bullet){
		bArray.removeValue(bullet, true);
		bullet.destroyBullet();
	}
	@Override
	public void addBullet() {
		bulletCount +=1;
		
	}
}

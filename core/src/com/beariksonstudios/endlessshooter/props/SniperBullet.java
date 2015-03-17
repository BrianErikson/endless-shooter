package com.beariksonstudios.endlessshooter.props;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.beariksonstudios.endlessshooter.classes.Sniper;
import com.beariksonstudios.endlessshooter.core.Assets;
import com.beariksonstudios.endlessshooter.core.Bullet;
import com.beariksonstudios.endlessshooter.props.Shruiken.Data;

public class SniperBullet implements Bullet {
	private float bulletSpeed;
	private Body body;
	private Texture texture;
	private Sprite sprite;
	private SBulletData sbData;
	private boolean pickupReady;
	private World world;
	
	public SniperBullet(Vector2 dir, Vector2 pos, World world, float scale, float degAngle, Sniper character) {
		bulletSpeed = 8f;
		BodyDef bd = new BodyDef();
		bd.active = true;
		bd.allowSleep = true;
		bd.awake = true;
		bd.bullet = true;
		bd.fixedRotation = true;
		bd.linearVelocity.setAngle(degAngle);
		bd.type = BodyDef.BodyType.DynamicBody;
		
		pickupReady = false;
		
		this.world = world;
		
		body = world.createBody(bd);
		
		FixtureDef fd = new FixtureDef();
		fd.density = 4;
		fd.restitution = 1;
		CircleShape shape = new CircleShape();
		float height = 5.9f * 0.3048f * 0.5f * Assets.TILE_SIZE * scale;
		height /= 20;
		shape.setRadius(height);
		body.setTransform(pos.add(new Vector2((height*2)*dir.x, (height*2)*dir.y)), degAngle);
		fd.shape = shape;
		
		texture = new Texture (Gdx.files.internal("data/bulletART.png"));
		sprite = new Sprite (texture);
		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
		
		sprite.setScale(0.001f);
		
		body.createFixture(fd);
		
		sbData = new SBulletData(this);
		sbData.character = character;
		body.setUserData (sbData);
		
		shape.dispose();
		Vector2 impulse = new Vector2 (bulletSpeed, bulletSpeed).scl(dir);
		body.applyLinearImpulse(impulse, body.getPosition(), true);
		
	}
	public class SBulletData {
		public String name = "SniperBullet";
		public int bounces = 0;
		public boolean isRotating = true;
		public Sniper character = null;
		public SniperBullet bullet = null;
		
		public SBulletData (SniperBullet bullet){
			this.bullet = bullet;
		}
	}
	public void draw(Camera camera, SpriteBatch batch){
		sprite.setPosition(body.getPosition().x - sprite.getWidth()/2, body.getPosition().y - sprite.getHeight()/2);
		if (sbData.isRotating){
			sprite.rotate(45f);
		}
		batch.setProjectionMatrix(camera.combined);
		sprite.draw(batch);
	}
	@Override
	public Body getBody() {
		return body;
	}
	@Override
	public boolean isReadyPickup() {
		// TODO Auto-generated method stub
		return pickupReady;
	}
	@Override
	public void setReadyPickup(boolean state) {
		pickupReady = state;
		
	}
	@Override
	public void destroyBullet() {
		world.destroyBody(body);
		
	}
}

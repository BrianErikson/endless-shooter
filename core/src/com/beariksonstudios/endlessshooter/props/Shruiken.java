package com.beariksonstudios.endlessshooter.props;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.beariksonstudios.endlessshooter.classes.Assassin;
import com.beariksonstudios.endlessshooter.classes.StockClass;
import com.beariksonstudios.endlessshooter.core.Assets;
import com.beariksonstudios.endlessshooter.core.Bullet;

public class Shruiken implements Bullet {
	private float bulletSpeed;
	private Body body;
	private Texture texture;
	private Sprite sprite;
	private Data data;
	private boolean pickupReady;
	private World world;
	
	public Shruiken(Vector2 dir, Vector2 pos, World world, float scale, float degAngle, Assassin character){
		bulletSpeed = 1f;
		BodyDef bd = new BodyDef();
		bd.active = true;
		bd.allowSleep = true;
		bd.awake =true;
		bd.bullet = true;
		bd.fixedRotation = true;
		bd.position.set(pos);
		bd.linearVelocity.setAngle(degAngle);
		bd.type = BodyDef.BodyType.DynamicBody;
		
		pickupReady = false;
		this.world = world;
		
		body = world.createBody(bd);
		
		FixtureDef fd = new FixtureDef();
		fd.density = 3;
		fd.restitution = 1;
		CircleShape shape = new CircleShape();
		float height = 5.9f * 0.3048f * 0.5f * Assets.TILE_SIZE * scale;
		height /= 10;
		shape.setRadius(height);
		fd.shape = shape;
		
		texture = new Texture (Gdx.files.internal("data/ShurikenART.png"));
		sprite = new Sprite (texture);
		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
		
		sprite.setScale(0.004f);
		
		body.createFixture(fd);
		
		data = new Data(this);
		data.character = character;
		body.setUserData (data);
		
		shape.dispose();
		Vector2 impulse = new Vector2 (bulletSpeed, bulletSpeed).scl(dir);
		body.applyLinearImpulse(impulse, body.getPosition(), true);
		
		
	}
	public class Data {
		public String name = "shruiken";
		public int bounces = 0;
		public boolean isRotating = true;
		public Assassin character = null;
		public Shruiken bullet = null;
		
		public Data (Shruiken bullet){
			this.bullet = bullet;
		}
	}
	public void draw(Camera camera, SpriteBatch batch){
		sprite.setPosition(body.getPosition().x - sprite.getWidth()/2, body.getPosition().y - sprite.getHeight()/2);
		if (data.isRotating){
			sprite.rotate(15f);
		}
		batch.setProjectionMatrix(camera.combined);
		sprite.draw(batch);
		
	}
	@Override
	public Body getBody() {
		// TODO Auto-generated method stub
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
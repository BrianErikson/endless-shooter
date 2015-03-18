package com.beariksonstudios.endlessshooter.core;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.beariksonstudios.endlessshooter.core.PlayerFactory.Player;

public interface Character {
	public void jump();
	public void crouch();
	public void moveLeft();
	public void moveRight();
	public void fire();
	public void stop();
	public void setState(STATE newState);
	public STATE getState();
	public enum STATE {
		STANDING,
		CROUCHING,
		JUMPING,
		FALLING
	};
	public void draw(Box2DDebugRenderer renderer, Camera camera, SpriteBatch batch);
	
	public Vector2 getPosition();
	public Vector2 getWorldCenterPosition();
	public Vector2 getSize();
	public Player getPlayer();
	public boolean isLocal();
	
	public void removeBullet (Bullet bullet);
	public void addBullet ();
}

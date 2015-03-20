package com.beariksonstudios.endlessshooter.core;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;

public interface Bullet {

	void draw(Camera camera, SpriteBatch batch);
	
	Body getBody ();
}

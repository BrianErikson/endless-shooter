package com.beariksonstudios.endlessshooter.classes;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class Enemy extends Character{

	public Enemy(Vector2 startPos, World physicsWorld, float scale,
			Camera camera) {
		super(startPos, physicsWorld, scale, camera);
	}

}

package com.beariksonstudios.endlessshooter.classes;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.beariksonstudios.endlessshooter.core.PlayerFactory.Player;

public class Scout extends StockClass {

	public Scout(boolean local, Player player, Vector2 startPos, World physicsWorld, float scale, Camera camera) {
		super(local, player, startPos, physicsWorld, scale, camera);
	}
}

package com.beariksonstudios.endlessshooter.classes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Enemy extends Character{
	Player target;
	float shootTimer;

	public Enemy(Vector2 startPos, World physicsWorld, float scale,
			Camera camera, Stage uiStage) {
		super(startPos, physicsWorld, scale, camera, uiStage);
		shootTimer = 0;
		
	}
	@Override
	public void draw(Box2DDebugRenderer renderer, Camera camera, SpriteBatch batch){
		super.draw(renderer, camera, batch);
		
		shootTimer += Gdx.graphics.getDeltaTime();
		if(shootTimer > 0.5){
			if(target != null){
				Vector2 playerPos = target.getPosition();
				Vector2 offset = playerPos.add(new Vector2(target.getSize().x/2, target.getSize().y/2));
				fire(offset);
			}
			shootTimer = 0;
		}
		
	}
	public void setTarget(Player p){
		this.target = p;
	}

}

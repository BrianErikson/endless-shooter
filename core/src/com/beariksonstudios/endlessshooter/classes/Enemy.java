package com.beariksonstudios.endlessshooter.classes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.beariksonstudios.endlessshooter.classes.Character.CharData;
import com.beariksonstudios.endlessshooter.props.SniperBullet;

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
	 public class RayCallback implements RayCastCallback{

    	public boolean shouldFire = false;
    	@Override
		public float reportRayFixture(Fixture fixture, Vector2 point,
				Vector2 normal, float fraction) {
			Object objectData = fixture.getBody().getUserData();
			System.out.println(objectData);
			if(objectData instanceof CharData){
				shouldFire = true;
				System.out.println("character hit");
			}
			//System.out.println("character not hit");
			return 0;
		}
    	
    }
	 @Override
	 public void fire(Vector2 pos) {
    	if(body != null){
    		Vector2 dist = new Vector2(pos.x - body.getPosition().x,
	                pos.y - body.getPosition().y);
	        Vector2 yVector = new Vector2(body.getPosition().x,
	                body.getPosition().y + dist.y);
	
	        float degAngle = (float) (MathUtils.atan2(dist.x, dist.y) * MathUtils.radiansToDegrees);
	
	        float newDist = this.getSize().y;
	        dist = dist.nor();
	        Vector2 dir = dist.cpy();
	        Vector2 gunPos = dist.scl(newDist);
	        Vector2 playerPos = target.getPosition();
			Vector2 offset = playerPos.add(new Vector2(target.getSize().x/2, target.getSize().y/2));
	        gunPos = body.getPosition().cpy().add(gunPos);
			RayCallback callback = new RayCallback();
	        world.rayCast(callback ,gunPos, offset);
	        if(callback.shouldFire){
		        SniperBullet sBullet = new SniperBullet(dir, gunPos, world, scale, degAngle, this);
		        bullets.add(sBullet);
		        //TODO fix ray call back braskis
	        }

    	}
    }
}

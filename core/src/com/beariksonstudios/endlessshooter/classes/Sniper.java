package com.beariksonstudios.endlessshooter.classes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.beariksonstudios.endlessshooter.classes.StockClass.STATE;
import com.beariksonstudios.endlessshooter.core.PlayerFactory.Player;
import com.beariksonstudios.endlessshooter.props.Shruiken;
import com.beariksonstudios.endlessshooter.props.SniperBullet;

public class Sniper extends StockClass {

	public Sniper(boolean local, Player player, Vector2 startPos, World physicsWorld, float scale, Camera camera) {
		super(local, player, startPos, physicsWorld, scale, camera);
	}
	@Override
	public void fire() {
		if ((state == STATE.STANDING || state == STATE.JUMPING) && Gdx.input.justTouched()&& bulletCount > 0) {
			float mouseX = Gdx.input.getX();
			float mouseY = Gdx.input.getY();
			Vector3 mousePos = new Vector3 (mouseX, mouseY, 0);
			camera.unproject(mousePos);
			Vector2 dist = new Vector2 (mousePos.x - body.getPosition().x,mousePos.y - body.getPosition().y); 
			Vector2 yVector = new Vector2 (body.getPosition().x, body.getPosition().y + dist.y);
			
			float degAngle = (float) (MathUtils.atan2(dist.x, dist.y) * MathUtils.radiansToDegrees);
			System.out.println(degAngle);
			
			float newDist = height;
			dist = dist.nor();
			Vector2 dir = dist.cpy();
			Vector2 gunPos = dist.scl(newDist);
			gunPos = body.getPosition().cpy().add(gunPos);
			SniperBullet sBullet = new SniperBullet(dir, gunPos, world, scale, degAngle, this);
			bArray.add(sBullet);
			bulletCount -= 1;
		}
	}
	@Override
	public void addBullet() {
		bulletCount +=1;
		
	}
}

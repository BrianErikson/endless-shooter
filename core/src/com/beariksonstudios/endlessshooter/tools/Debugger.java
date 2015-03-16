package com.beariksonstudios.endlessshooter.tools;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Debugger {
	
	private float delay;
	private SpriteBatch batch;
	private Label fpsLabel;
	private World world;
	private Label jointLabel;
	private Label bodyLabel;
	private float updateRate;

	/** For standard debug use **/
	public Debugger(Skin skin, SpriteBatch batch) {
		this.batch = batch;
		delay = 0f;
		updateRate = 0.5f; // Update rate in seconds
		fpsLabel = new Label("FPS: init...", skin);
	}
	
	/** For physics and standard debug use **/
	public Debugger(Skin skin, SpriteBatch batch, World world) {
		this.batch = batch;
		this.world = world;
		delay = 0f;
		updateRate = 0.5f; // Update rate in seconds
		fpsLabel = new Label("FPS: init...", skin);
		jointLabel = new Label("Joint Count: init..." + world.getJointCount(), skin);
		bodyLabel = new Label("Body Count: init..." + world.getBodyCount(), skin);
	}
	
	/** Updates the FPS counter twice a second.
	 * Begin SpriteBatch before calling. **/
	public void drawFPS(float delta, Vector2 pos) {
		if (updateCycle(delta)) {
			int fps = (int) Math.floor(1/delta);
			fpsLabel.setText("FPS: " + fps);
		}
		fpsLabel.setPosition(pos.x, pos.y);
		fpsLabel.draw(batch, 1f);
	}
	
	/** Displays World body and joint info.
	 * Begin SpriteBatch before calling. **/
	public void drawPhys(float delta, Vector2 pos) {
		if (updateCycle(delta)) {
			bodyLabel.setText("Body Count: " + world.getBodyCount());
			jointLabel.setText("Joint Count: " + world.getJointCount());
		}
		bodyLabel.setPosition(pos.x, pos.y);
		Vector2 offset = new Vector2(pos.x, pos.y - getLabelHeight());
		jointLabel.setPosition(offset.x, offset.y);
		
		bodyLabel.draw(batch, 1f);
		jointLabel.draw(batch, 1f);
	}
	
	public float getLabelHeight() {
		return fpsLabel.getHeight();
	}
	
	public boolean updateCycle(float deltaTime) {
		delay += deltaTime;
		if (delay >= updateRate) {
			delay = 0f;
			return true;
		}
		else return false;
	}
}

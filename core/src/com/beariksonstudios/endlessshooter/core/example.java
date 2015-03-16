package com.beariksonstudios.endlessshooter.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class example implements Screen {
	private OrthographicCamera camera;
	private ShapeRenderer renderer;
	private Vector2 origin;
	private Vector2 mousePos;

	public example() {
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		renderer = new ShapeRenderer();
		
		origin = new Vector2(camera.viewportWidth/2, camera.viewportHeight/2);
		mousePos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
		
		camera.translate(origin);
		camera.update();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.30f, 0.50f, 0.95f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		mousePos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
		Vector3 mousePos3 = new Vector3(mousePos.x, mousePos.y, 0);
		camera.unproject(mousePos3); // invert Y axis for use in our camera
		Vector2 mouseUnproj =  new Vector2(mousePos3.x, mousePos3.y);
		
		Vector2 xPos = new Vector2(mouseUnproj.x, origin.y); // for displaying length on the x axis
		Vector2 yPos = new Vector2(origin.x, mouseUnproj.y); // for displaying length on the y axis
		
		Vector2 distVec = new Vector2(mouseUnproj.x - origin.x, mouseUnproj.y - origin.y); // x and y distances
		float mag = origin.dst(mouseUnproj); // magnitude

		float degAngle = (float) (MathUtils.atan2(distVec.x, distVec.y) * MathUtils.radiansToDegrees);
		
		
		float desiredDist = 100f;
		System.out.println(distVec);
		distVec = distVec.nor(); // normalized the distance vector (gets magnitude automatically so var mag does not need to be used)
		System.out.println("   " + distVec);
		Vector2 gunPos = distVec.cpy().scl(desiredDist);
		
		renderer.setProjectionMatrix(camera.combined);
		renderer.begin(ShapeType.Filled);
		renderer.setColor(Color.WHITE);
		renderer.circle(origin.x, origin.y, 5f);
		renderer.end();
		renderer.begin(ShapeType.Line);
		renderer.setColor(Color.RED);
		renderer.line(origin, mousePos); // MousePos before Projection
		renderer.setColor(Color.WHITE);
		renderer.line(origin, new Vector2(mouseUnproj.x, mouseUnproj.y)); // MousePos Unprojected
		renderer.setColor(Color.WHITE);
		renderer.line(origin, xPos); // X Coord
		renderer.line(origin, yPos); // Y Coord
		renderer.setColor(Color.BLACK);
		renderer.line(origin, origin.cpy().add(gunPos)); // gun position
		renderer.end();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}

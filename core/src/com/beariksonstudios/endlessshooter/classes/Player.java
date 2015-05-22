package com.beariksonstudios.endlessshooter.classes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;

public class Player extends Character {


    public Player(Vector2 startPos, World physicsWorld, float scale,
                  Camera camera) {
        super(startPos, physicsWorld, scale, camera);
        this.maxHealth = 100;
        this.currentHealth = maxHealth;
    }

    @Override
    public void draw(Camera camera, SpriteBatch batch) {
        super.draw(camera, batch);

    }

    public void fire() {
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.input.getY();
        Vector3 mousePos = new Vector3(mouseX, mouseY, 0);
        camera.unproject(mousePos);
        super.fire(new Vector2(mousePos.x, mousePos.y));
    }

}

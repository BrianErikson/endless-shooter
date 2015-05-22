package com.beariksonstudios.endlessshooter.core;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Brian on 5/22/2015.
 */
public class ESActor {
    private ESStage stage;
    private boolean dead = false;

    public void draw(Camera camera, SpriteBatch batch){}

    public ESStage getStage() {
        return stage;
    }

    public void setStage(ESStage stage) {
        this.stage = stage;
    }

    public void kill() {
        dead = true;
    }

    public boolean isDead() {
        return dead;
    }
}

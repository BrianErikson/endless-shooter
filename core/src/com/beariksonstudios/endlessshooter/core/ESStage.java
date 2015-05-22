package com.beariksonstudios.endlessshooter.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.beariksonstudios.endlessshooter.tools.WorldMap;

import java.util.ArrayList;
import java.util.ListIterator;

/**
 * Created by Brian on 5/22/2015.
 */
public class ESStage {
    private ArrayList<ESActor> actors;
    private ArrayList<ESActor> additions;
    private ArrayList<ESActor> removals;
    private SpriteBatch batch;

    private OrthographicCamera camera;
    private WorldMap map;

    public ESStage(float zoom, SpriteBatch batch, WorldMap map) {
        this.batch = batch;
        this.map = map;

        camera = new OrthographicCamera(Assets.BOX_TO_WORLD,
                Assets.BOX_TO_WORLD * (Gdx.graphics.getHeight() / Gdx.graphics.getWidth()));
        camera.zoom = zoom;
        batch.setProjectionMatrix(camera.combined);
        actors = new ArrayList<ESActor>();
        additions = new ArrayList<ESActor>();
        removals = new ArrayList<ESActor>();
    }

    public void addActor(ESActor actor) {
        actor.setStage(this);
        additions.add(actor);
    }

    public void removeActor(ESActor actor) {
        actor.setStage(null);
        removals.add(actor);
    }

    private void updateActorArray() {
        if (additions.size() > 0) {
            actors.addAll(additions);
            additions.clear();
        }
        if (removals.size() > 0) {
            System.out.println(removals.size());
            for (ESActor actor : removals) {
                actors.remove(actor);
            }
            removals.clear();
        }
    }

    public void update() {
        for (ESActor actor : actors) {
            actor.update();
        }

        updateActorArray();
    }

    public void draw() {
        map.draw(camera);

        batch.begin();
        for (ESActor actor : actors) {
            actor.draw(camera, batch);
        }
        batch.end();

        removeDeadActors();
    }

    private void removeDeadActors() {
        // Remove dead bullets
        ListIterator<ESActor> list = actors.listIterator();
        while (list.hasNext()) {
            ESActor actor = list.next();
            if (actor.isDead()) {
                actor.setStage(null);
                list.remove();
            }
        }
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public ArrayList<ESActor> getActors() {
        return actors;
    }

    public WorldMap getMap() {
        return map;
    }

    public void dispose() {
        batch.dispose();
    }
}

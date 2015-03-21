package com.beariksonstudios.endlessshooter.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class Debugger extends Actor {

    private final Label fpsLabel;
    private Label jointLabel;
    private Label bodyLabel;
    private float delay;
    private World world;
    private Table table;
    private float updateRate;
    private TYPE debugType;

    /**
     * For standard debug use *
     */
    public Debugger(Skin skin, Stage stage) {
        delay = 0f;
        updateRate = 0.5f; // Update rate in seconds
        table = new Table();
        fpsLabel = new Label("FPS: init...", skin);
        table.addActor(fpsLabel);
        table.pack();
        stage.addActor(table);
        debugType = TYPE.STANDARD;
    }

    /**
     * For physics and standard debug use *
     */
    public Debugger(Skin skin, Stage stage, World world) {
        debugType = TYPE.PHYSICS;
        this.world = world;
        delay = 0f;
        updateRate = 0.5f; // Update rate in seconds

        table = new Table();

        fpsLabel = new Label("FPS: init...", skin);
        table.addActor(fpsLabel);
        table.row();

        jointLabel = new Label("Joint Count: init..." + world.getJointCount(), skin);
        table.addActor(jointLabel);
        table.row();

        bodyLabel = new Label("Body Count: init..." + world.getBodyCount(), skin);
        table.addActor(bodyLabel);

        table.pack();
        stage.addActor(table);
        table.setDebug(true);
    }

    public boolean updateCycle(float deltaTime) {
        delay += deltaTime;
        if (delay >= updateRate) {
            delay = 0f;
            return true;
        } else return false;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        float delta = Gdx.graphics.getDeltaTime();
        if (updateCycle(delta)) {
            int fps = (int) Math.floor(1 / delta);
            fpsLabel.setText("FPS: " + fps);
            if (debugType == TYPE.PHYSICS) {
                bodyLabel.setText("Body Count: " + world.getBodyCount());
                jointLabel.setText("Joint Count: " + world.getJointCount());
            }
        }
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        table.setPosition(x, y);
    }

    private enum TYPE {
        STANDARD,
        PHYSICS
    }
}

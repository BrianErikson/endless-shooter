package com.beariksonstudios.endlessshooter.classes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.beariksonstudios.endlessshooter.core.Bullet;
import com.beariksonstudios.endlessshooter.tools.RayCallback;

public class Enemy extends Character {
    private Player target;
    private float shootTimer;
    private ShapeRenderer sRenderer;
    private Vector2 lastP1;
    private Vector2 lastP2;

    public Enemy(Vector2 startPos, World physicsWorld, float scale,
                 Camera camera) {
        super(startPos, physicsWorld, scale, camera);
        shootTimer = 0;
        sRenderer = new ShapeRenderer();
        lastP1 = new Vector2(0, 0);
        lastP2 = new Vector2(0, 0);
    }

    @Override
    public void draw(Camera camera, SpriteBatch batch) {
        super.draw(camera, batch);
        shootTimer += Gdx.graphics.getDeltaTime();
        if (shootTimer > 0.5) {
            if (target != null) {
                Vector2 playerPos = target.getPosition();
                Vector2 offset = playerPos.add(new Vector2(target.getSize().x / 2, target.getSize().y / 2));
                fire(offset);
            }
            shootTimer = 0;
        }

        sRenderer.setProjectionMatrix(camera.combined);
        sRenderer.begin(ShapeType.Filled);
        sRenderer.rectLine(lastP1, lastP2, .04f);
        sRenderer.end();
    }

    public void setTarget(Player p) {
        this.target = p;
    }

    @Override
    public void fire(Vector2 pos) {
        if (body != null && getStage() != null) {
            Vector2 dist = new Vector2(pos.x - body.getPosition().x,
                    pos.y - body.getPosition().y);

            //noinspection SuspiciousNameCombination
            float degAngle = MathUtils.atan2(dist.x, dist.y) * MathUtils.radiansToDegrees;

            float newDist = this.getSize().y;
            dist = dist.nor();
            Vector2 dir = dist.cpy();
            Vector2 gunPos = dist.scl(newDist);
            Vector2 playerPos = target.getPosition();
            Vector2 offset = playerPos.add(new Vector2(target.getSize().x / 2, target.getSize().y / 2));
            gunPos = body.getPosition().cpy().add(gunPos);

            RayCallback callback = new RayCallback();
            world.rayCast(callback, gunPos, offset);

            lastP1 = gunPos;
            if (callback.hits.size() > 0)
                lastP2 = callback.hits.get(0).point;

            if (callback.hits.size() == 1 && getStage() != null) {
                getStage().addActor(new Bullet(dir, gunPos, world, scale, degAngle, this));
            }

        }
    }
}

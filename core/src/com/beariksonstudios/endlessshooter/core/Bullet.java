package com.beariksonstudios.endlessshooter.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.beariksonstudios.endlessshooter.classes.Character;

public class Bullet extends ESActor {
    public final float damage;
    private float bulletSpeed;
    private Body body;
    private Sprite sprite;
    private World world;


    public Bullet(Vector2 dir, Vector2 pos, World world, float scale, float degAngle, Character character) {
        bulletSpeed = 1f;
        BodyDef bd = new BodyDef();
        bd.active = true;
        bd.allowSleep = true;
        bd.awake = true;
        bd.bullet = true;
        bd.fixedRotation = true;
        bd.linearVelocity.setAngle(degAngle);
        bd.type = BodyDef.BodyType.DynamicBody;
        damage = 5;

        this.world = world;

        body = world.createBody(bd);

        FixtureDef fd = new FixtureDef();
        fd.density = 4;
        fd.restitution = 1;
        CircleShape shape = new CircleShape();
        float height = 5.9f * 0.3048f * 0.5f * Assets.TILE_SIZE * scale;
        height /= 20;
        shape.setRadius(height);
        body.setTransform(pos.add(new Vector2((height * 2) * dir.x, (height * 2) * dir.y)), degAngle);
        fd.shape = shape;

        Texture texture = new Texture(Gdx.files.internal("data/bulletART.png"));
        sprite = new Sprite(texture);
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);

        sprite.setScale(0.001f);

        body.createFixture(fd);

        shape.dispose();
        Vector2 impulse = new Vector2(bulletSpeed, bulletSpeed).scl(dir);
        body.applyLinearImpulse(impulse, body.getPosition(), true);
        body.setUserData(this);

    }

    public void draw(Camera camera, SpriteBatch batch) {
        if (body != null) {
            sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2f, body.getPosition().y - sprite.getHeight
                    () / 2f);
            batch.setProjectionMatrix(camera.combined);
            sprite.draw(batch);
            // Work on restructuring removal of ESActors
            if (isDead() && !world.isLocked()) {
                world.destroyBody(body);
                body = null;
            }
        }
    }

    public Body getBody() {
        return body;
    }

    @Override
    public void kill() {
        super.kill();
        body.setLinearVelocity(0, 0);
    }

    public float getDamage() {
        return damage;
    }
}

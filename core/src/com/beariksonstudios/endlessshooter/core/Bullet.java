package com.beariksonstudios.endlessshooter.core;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.beariksonstudios.endlessshooter.classes.Character;

public class Bullet extends ESActor {
    public final float damage;
    private Texture texture;
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

        texture = Assets.manager.get("data/bulletART.png");
        sprite = new Sprite(texture);

        sprite.setSize(30000f, 30000f);
        sprite.setScale(10000f);
        sprite.setOriginCenter();

        body.createFixture(fd);

        shape.dispose();
        Vector2 impulse = new Vector2(bulletSpeed, bulletSpeed).scl(dir);
        body.applyLinearImpulse(impulse, body.getPosition(), true);
        body.setUserData(this);

    }

    @Override
    public void update() {
        if (body != null)
            sprite.setCenter(body.getPosition().x, body.getPosition().y);
    }

    public void draw(Camera camera, SpriteBatch batch) {

        if (body != null) {
            batch.draw(texture, body.getPosition().x, body.getPosition().y, 600f, 600f);
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

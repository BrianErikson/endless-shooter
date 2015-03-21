package com.beariksonstudios.endlessshooter.classes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.beariksonstudios.endlessshooter.core.Assets;
import com.beariksonstudios.endlessshooter.props.SniperBullet;

public class Character {
    protected int moveSpeed;
    protected int jumpSpeed;
    protected World world;
    protected Body body;
    protected float scale;
    protected STATE state;
    protected float width;
    protected float height;
    protected Camera camera;
    protected long bounceTimer;
    protected int jumpCount;

    public Character(Vector2 startPos, World physicsWorld,
                     float scale, Camera camera) {
        bounceTimer = 100;
        this.camera = camera;
        this.scale = scale;
        this.world = physicsWorld;
        moveSpeed = 200;
        jumpSpeed = 20;
        BodyDef bd = new BodyDef();
        bd.allowSleep = false; // object does not need to sleep due to player control
        bd.position.set(startPos);
        bd.type = BodyDef.BodyType.DynamicBody;


        this.body = world.createBody(bd);
        body.setFixedRotation(true);
        body.setUserData(new RangeCharData(this));


        FixtureDef fd = new FixtureDef();
        fd.friction = 0f;
        fd.density = 1.0f; // weight of average human
        PolygonShape shape = new PolygonShape();
        width = 1.6f * 0.3048f * 0.5f * Assets.TILE_SIZE * scale;
        height = 5.9f * 0.3048f * 0.5f * Assets.TILE_SIZE * scale;
        shape.setAsBox(width, height); // 33% average height and width in feet (converted into meters 0.3048)
        fd.shape = shape;

        this.body.createFixture(fd);
    }

    public void jump() {
        if ((state == STATE.STANDING || state == STATE.FALLING || state == STATE.JUMPING) && jumpCount < 2) {
            state = STATE.JUMPING;
            float force = jumpSpeed;
            body.applyLinearImpulse(new Vector2(0, force), new Vector2(0, 1), true);
            jumpCount++;
            System.out.println(jumpCount);
        }
    }

    public void crouch() {
        if (state == STATE.STANDING) {
            // duck? drop through thin platforms?
            state = STATE.CROUCHING;
        }
    }

    public void moveLeft() {
        body.setLinearVelocity(new Vector2(-moveSpeed * Gdx.graphics.getDeltaTime(), body.getLinearVelocity().y));
    }

    public void moveRight() {
        body.setLinearVelocity(new Vector2(moveSpeed * Gdx.graphics.getDeltaTime(), body.getLinearVelocity().y));
    }

    public void stop() {
        body.setLinearVelocity(new Vector2(0, body.getLinearVelocity().y));
    }

    public void draw(Box2DDebugRenderer renderer, Camera camera, SpriteBatch batch) {
        if (body.getLinearVelocity().y > 0.0f) state = STATE.JUMPING;
        if (body.getLinearVelocity().y < 0.0f) state = STATE.FALLING;
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }

    public Vector2 getWorldCenterPosition() {
        return body.getWorldCenter();
    }

    public Vector2 getSize() {
        return new Vector2(width * 2, height * 2);
    }

    public STATE getState() {
        return state;
    }

    public void setState(STATE newState) {
        this.state = newState;
        if (state == STATE.STANDING) {
            jumpCount = 0;
            System.out.println("Standing");
        }
    }

    public void fire() {
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.input.getY();
        Vector3 mousePos = new Vector3(mouseX, mouseY, 0);
        camera.unproject(mousePos);
        Vector2 dist = new Vector2(mousePos.x - body.getPosition().x,
                mousePos.y - body.getPosition().y);
        Vector2 yVector = new Vector2(body.getPosition().x,
                body.getPosition().y + dist.y);

        float degAngle = (float) (MathUtils.atan2(dist.x, dist.y) * MathUtils.radiansToDegrees);

        float newDist = height;
        dist = dist.nor();
        Vector2 dir = dist.cpy();
        Vector2 gunPos = dist.scl(newDist);
        gunPos = body.getPosition().cpy().add(gunPos);
        SniperBullet sBullet = new SniperBullet(dir, gunPos, world, scale, degAngle, this);
    }

    public enum STATE {
        STANDING,
        CROUCHING,
        JUMPING,
        FALLING,
        DYING
    }

    public class RangeCharData {
        public Character character;

        public RangeCharData(Character character) {
            this.character = character;
        }
    }
}

package com.beariksonstudios.endlessshooter.classes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.beariksonstudios.endlessshooter.core.Assets;
import com.beariksonstudios.endlessshooter.core.Bullet;
import com.beariksonstudios.endlessshooter.props.HealthBar;

import java.util.ArrayList;
import java.util.ListIterator;

public class Character {
    protected int moveSpeed;
    protected int jumpSpeed;
    protected World world;
    protected Body body;
    protected float scale;
    protected STATE state;
    protected float width;
    protected float height;
    protected float bodyHeight;
    protected float headHeight;
    protected Camera camera;
    protected long bounceTimer;
    protected int jumpCount;
    protected ArrayList<Bullet> bullets;
    protected float maxHealth;
    protected float currentHealth;
    protected HealthBar healthBar;
    protected Stage uiStage;

    public Character(Vector2 startPos, World physicsWorld,
                     float scale, Camera camera, Stage uiStage) {
        this.uiStage = uiStage;
        bounceTimer = 100;
        this.camera = camera;
        this.scale = scale;
        this.world = physicsWorld;
        moveSpeed = 200;
        jumpSpeed = 20;
        maxHealth = 100;
        currentHealth = 50;
        BodyDef bd = new BodyDef();
        bd.allowSleep = false; // object does not need to sleep due to player control
        bd.position.set(startPos);
        bd.type = BodyDef.BodyType.DynamicBody;

        this.body = world.createBody(bd);
        body.setFixedRotation(true);
        body.setUserData(new CharData(this));

        FixtureDef fd = new FixtureDef();
        fd.friction = 0f;
        fd.density = 1.0f; // weight of average human
        PolygonShape shape = new PolygonShape();
        width = 1.6f * 0.3048f * 0.5f * Assets.TILE_SIZE * scale;
        height = 5.9f * 0.3048f * 0.5f * Assets.TILE_SIZE * scale;
        bodyHeight = height * .75f;
        shape.setAsBox(width, bodyHeight); // 33% average height and width in feet (converted into meters 0.3048)
        fd.shape = shape;

        Fixture bodyFixture = this.body.createFixture(fd);

        FixtureDef fdHead = new FixtureDef();
        fdHead.friction = 0f;
        fdHead.density = 1.0f; // weight of average human
        PolygonShape headShape = new PolygonShape();
        width = 1.6f * 0.3048f * 0.5f * Assets.TILE_SIZE * scale;
        height = 5.9f * 0.3048f * 0.5f * Assets.TILE_SIZE * scale;
        headHeight = height * .25f;
        headShape.setAsBox(width, headHeight, new Vector2(0, bodyHeight + (headHeight)), 0); // 33% average height
        // and width in feet (converted into meters 0.3048)
        fdHead.shape = headShape;

        //implement Health bars on characters
        healthBar = new HealthBar(100, 0, 100, new Vector2(0.1f, 0.05f));
        Vector3 healthBarPos = uiStage.getCamera().project(new Vector3(this.getPosition().x, this.getPosition().y, 0));
        healthBar.setPosition(healthBarPos.x, healthBarPos.y);
        healthBar.setHealth((int)this.currentHealth);
        healthBar.setSize(1f, 0.2f);
        //uiStage.addActor(healthBar);

        Fixture headFixture = this.body.createFixture(fdHead);

        bodyFixture.setUserData("body");
        headFixture.setUserData("head");

        bullets = new ArrayList<Bullet>();
    }

    public void jump() {
        if ((state == STATE.STANDING || state == STATE.FALLING || state == STATE.JUMPING) && jumpCount < 2 && body !=
                null) {
            state = STATE.JUMPING;
            float force = jumpSpeed;
            body.applyLinearImpulse(new Vector2(0, force), new Vector2(0, 1), true);
            jumpCount++;
            System.out.println(jumpCount);
        }
    }

    public void crouch() {
        if (state == STATE.STANDING) {
            // TODO: duck? drop through thin platforms?
            state = STATE.CROUCHING;
        }
    }

    public void moveLeft() {
        if (body != null)
            body.setLinearVelocity(new Vector2(-moveSpeed * Gdx.graphics.getDeltaTime(), body.getLinearVelocity().y));
    }

    public void moveRight() {
        if (body != null)
            body.setLinearVelocity(new Vector2(moveSpeed * Gdx.graphics.getDeltaTime(), body.getLinearVelocity().y));
    }

    public void stop() {
        if (body != null)
            body.setLinearVelocity(new Vector2(0, body.getLinearVelocity().y));
    }

    public void update(float dt) {
        if (body != null) {
            // State machine
            if (body.getLinearVelocity().y > 0.0f) state = STATE.JUMPING;
            if (body.getLinearVelocity().y < 0.0f) state = STATE.FALLING;

            if (currentHealth < 0 && !world.isLocked()) {
                this.onDeath();
            }

            // Health bar
            this.healthBar.setHealth((int)this.currentHealth);
            Vector2 healthBarPos = this.getPosition();
            this.healthBar.setPosition(healthBarPos.x - this.getSize().x, healthBarPos.y + (this.getSize().y / 1.8f));
        }

        // Remove dead bullets
        ListIterator<Bullet> list = bullets.listIterator();
        while (list.hasNext()) {
            Bullet bullet = list.next();
            if (bullet.isDead())
                list.remove();
        }
    }

    public void draw(Box2DDebugRenderer renderer, Camera camera, SpriteBatch batch) {
        // TODO: Move bullet responsibility to a custom stage/render class
        for (Bullet bullet : bullets) {
            bullet.draw(camera, batch);
        }

        healthBar.draw(batch);
    }

    private void onDeath() {
        world.destroyBody(body);
        body = null;
    }

    public Vector2 getPosition() {
        if (body != null)
            return body.getPosition();
        return new Vector2(0, 0);

    }

    public Vector2 getWorldCenterPosition() {
        if (body != null)
            return body.getWorldCenter();
        return new Vector2(0, 0);
    }

    public Vector2 getSize() {
        return new Vector2(width * 2, (height + headHeight) * 2);
    }

    public STATE getState() {
        return state;
    }

    public void setState(STATE newState) {
        this.state = newState;
        if (state == STATE.STANDING) {
            jumpCount = 0;
        }
    }

    public void fire(Vector2 pos) {
        if (body != null) {
            Vector2 dist = new Vector2(pos.x - body.getPosition().x,
                    pos.y - body.getPosition().y);

            //noinspection SuspiciousNameCombination
            float degAngle = MathUtils.atan2(dist.x, dist.y) * MathUtils.radiansToDegrees;

            float newDist = this.getSize().y;
            dist = dist.nor();
            Vector2 dir = dist.cpy();
            Vector2 gunPos = dist.scl(newDist);
            gunPos = body.getPosition().cpy().add(gunPos);

            // TODO: Potentially change bullet classes to only have one with variable damage
            Bullet sBullet = new Bullet(dir, gunPos, world, scale, degAngle, this);
            bullets.add(sBullet);
        }
    }

    public void damageCharacter(float damage) {
        currentHealth -= damage;
    }

    public enum STATE {
        STANDING,
        CROUCHING,
        JUMPING,
        FALLING,
        DYING
    }

    public class CharData {
        public Character character;

        public CharData(Character character) {
            this.character = character;
        }
    }
}

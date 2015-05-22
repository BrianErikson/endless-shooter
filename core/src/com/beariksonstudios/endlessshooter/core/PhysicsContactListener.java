package com.beariksonstudios.endlessshooter.core;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.beariksonstudios.endlessshooter.classes.Character;

/**
 * Created by Brian on 3/21/2015.
 */
public class PhysicsContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Object objA = contact.getFixtureA().getBody().getUserData();
        Object objB = contact.getFixtureB().getBody().getUserData();
        Object fixA = contact.getFixtureA().getUserData();
        Object fixB = contact.getFixtureB().getUserData();

        if (objA instanceof Character.CharData) {
            handleCharacterContact(objA, objB);
        } else if (objB instanceof Character.CharData) {
            handleCharacterContact(objB, objA);
        }
        if (objA instanceof Bullet) {
            handleBulletContact(objA, objB, fixA, fixB);
        } else if (objB instanceof Bullet) {
            handleBulletContact(objB, objA, fixB, fixA);
        }
    }

    @Override
    public void endContact(Contact contact) {
        // TODO Auto-generated method stub

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        // TODO Auto-generated method stub

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        // TODO Auto-generated method stub

    }

    private void handleCharacterContact(Object source, Object other) {
        Character.CharData cData = (Character.CharData) source;
        Character character = cData.character;
        if (other instanceof String) {
            if (other.equals("object ground")) {
                character.setState(Character.STATE.STANDING);
            } else if (other.equals("object walls")) {
                if (character.getState() == Character.STATE.JUMPING || character.getState() == Character.STATE
                        .FALLING) {
                    //TODO Placeholder
                }
            }
        }
    }

    private void handleBulletContact(Object source, Object other, Object sourceFix, Object otherFix) {
        Bullet bullet = (Bullet) source;
        if (other instanceof String) {
            if (other.equals("object ground")) {
                bullet.kill();
            } else if (other.equals("object walls")) {
                bullet.kill();
            }
        } else if (other instanceof Character.CharData) {
            Character.CharData cData = (Character.CharData) other;
            if (cData.character != null) {
                Character character = cData.character;
                if (otherFix instanceof String) {
                    if (otherFix.equals("head")) {
                        character.damageCharacter(bullet.getDamage() * 2);
                        bullet.kill();
                    } else {
                        character.damageCharacter(bullet.getDamage());
                        bullet.kill();
                    }

                }
            }
        }

    }
}

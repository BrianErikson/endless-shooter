package com.beariksonstudios.endlessshooter.core;

import com.badlogic.gdx.physics.box2d.*;
import com.beariksonstudios.endlessshooter.classes.Character;

/**
 * Created by Brian on 3/21/2015.
 */
public class PhysicsContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Object objA = contact.getFixtureA().getBody().getUserData();
        Object objB = contact.getFixtureB().getBody().getUserData();
        
        if(objA instanceof Character.RangeCharData){
            handleCharacterContact(objA, objB);
        }
        else if(objB instanceof Character.RangeCharData){
            handleCharacterContact(objB, objA);
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
        Character.RangeCharData cData = (Character.RangeCharData) source;
        Character character = cData.character;
        if(other instanceof String){
            System.out.println(other);
            if(other.equals("object ground")){
                character.setState(Character.STATE.STANDING);
            }
            else if(other.equals("object walls")){
                if(character.getState() == Character.STATE.JUMPING || character.getState() == Character.STATE.FALLING){
                    //TODO Placeholder
                }
            }
        }
    }
}

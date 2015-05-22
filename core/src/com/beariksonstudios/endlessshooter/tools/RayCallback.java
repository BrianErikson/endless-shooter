package com.beariksonstudios.endlessshooter.tools;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;

import java.util.ArrayList;

/**
 * Created by Brian on 5/22/2015.
 */
public class RayCallback implements RayCastCallback {
    public class RayHit {
        public Fixture fixture;
        public Vector2 point;
        public Vector2 normal;
        public float fraction;

        public RayHit(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
            this.fixture = fixture;
            this.point = point;
            this.normal = normal;
            this.fraction = fraction;
        }
    }
    public ArrayList<RayHit> hits = new ArrayList<RayHit>();

    @Override
    public float reportRayFixture(Fixture fixture, Vector2 point,
                                  Vector2 normal, float fraction) {
        hits.add(new RayHit(fixture, point, normal, fraction));
        return fraction;
    }
}

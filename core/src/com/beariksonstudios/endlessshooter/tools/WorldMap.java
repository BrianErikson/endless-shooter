package com.beariksonstudios.endlessshooter.tools;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.beariksonstudios.endlessshooter.core.Assets;

import java.util.Stack;

public class WorldMap {
    private TiledMap map;
    private MapLayers layers;
    private int[] drawLayers;
    private int[] collisionLayers;
    private OrthogonalTiledMapRenderer renderer;
    private float scale;
    private World world;

    public WorldMap(World world, TiledMap map, SpriteBatch batch, float scale) {
        this.world = world;
        this.map = map;
        this.renderer = new OrthogonalTiledMapRenderer(map, scale, batch);
        this.scale = scale;

        layers = map.getLayers();

        // Add layers to each stack according
        Stack<Integer> drawableLayers = new Stack<Integer>();
        Stack<Integer> noDrawLayers = new Stack<Integer>();
        for (int i = 0; i < layers.getCount(); i++) {
            if (layers.get(i).getName().contains("Tile")) {
                drawableLayers.add(i);
            } else {
                noDrawLayers.add(i);
            }
        }
        //unpack stacks into an Integer array
        drawLayers = new int[drawableLayers.size()];
        for (int i = 0; i < drawLayers.length; i++) {
            if (!drawableLayers.empty()) drawLayers[i] = drawableLayers.pop();
        }

        collisionLayers = new int[noDrawLayers.size()];
        for (int i = 0; i < collisionLayers.length; i++) {
            collisionLayers[i] = noDrawLayers.pop();
        }

        // dispose of unneeded stacks
        drawableLayers = null;
        noDrawLayers = null;

        buildColliders();
    }

    public void draw(OrthographicCamera camera) {
        for (int i = 0; i < layers.getCount(); i++) {
            if (!layers.get(i).getName().contains("start")) {
                renderer.setView(camera);
                renderer.render(drawLayers);
            }
        }
    }

    public void buildColliders() {
        for (MapLayer layer : getColliders()) {
            MapObjects objects = layer.getObjects();
            for (int i = 0; i < objects.getCount(); i++) {
                MapObject object = objects.get(i);
                BodyDef bd = new BodyDef();
                bd.type = BodyDef.BodyType.StaticBody;

                FixtureDef fd = new FixtureDef();
                PolygonShape shape = new PolygonShape();

                if (object instanceof RectangleMapObject) {
                    Rectangle rect = ((RectangleMapObject) object).getRectangle();
                    float scaleWidth = rect.width * scale;
                    float scaleHeight = rect.height * scale;
                    Vector2 pos = new Vector2(0, 0);

                    pos.x = (((rect.getX() / Assets.TILE_SIZE) + ((rect.getWidth() * scale) / 2)));
                    pos.y = (((rect.getY() / Assets.TILE_SIZE) + ((rect.getHeight() * scale) / 2)));

                    bd.position.x = pos.x;
                    bd.position.y = pos.y;
                    shape.setAsBox(scaleWidth / 2, scaleHeight / 2);
                } else if (object instanceof PolygonMapObject) {
                    Polygon polygon = ((PolygonMapObject) object).getPolygon();
                    bd.position.x = polygon.getX();
                    bd.position.y = polygon.getY();
                    shape.set(polygon.getTransformedVertices());
                }
                /*else if (object instanceof PolylineMapObject) {
			        Polyline chain = ((PolylineMapObject) object).getPolyline();
			        // do something with chain...
			    }*/
                else if (object instanceof CircleMapObject) {
                    Circle circle = ((CircleMapObject) object).getCircle();
                    bd.position.x = circle.x;
                    bd.position.y = circle.y;
                    shape.setRadius(circle.radius);
                }

                fd.shape = shape;
                Body body = world.createBody(bd);
                body.setUserData(layer.getName());
                body.createFixture(fd);

                shape.dispose();
            }
        }
    }

    /**
     * Returns true if grounded on a collider *
     */
    public boolean isGrounded(Vector2 point) {
        for (int objIndex : collisionLayers) {
            if (!layers.get(objIndex).getName().contains("death")) {
                MapObjects objects = layers.get(objIndex).getObjects();
                for (int i = 0; i < objects.getCount(); i++) {
                    MapObject object = objects.get(i);

                    if (object instanceof RectangleMapObject) {
                        Rectangle rect = ((RectangleMapObject) object).getRectangle();
                        if (rect.contains(point)) return true;
                    } else if (object instanceof PolygonMapObject) {
                        Polygon polygon = ((PolygonMapObject) object).getPolygon();
                        if (polygon.contains(point.x, point.y)) return true;
                    }
				    /*else if (object instanceof PolylineMapObject) {
				        Polyline chain = ((PolylineMapObject) object).getPolyline();
				        // do something with chain...
				    }*/
                    else if (object instanceof CircleMapObject) {
                        Circle circle = ((CircleMapObject) object).getCircle();
                        if (circle.contains(point)) return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isDead(Vector2 point) {
        for (int objIndex : collisionLayers) {
            String name = layers.get(objIndex).getName();
            if (name.contains("death")) {
                MapObjects objects = layers.get(objIndex).getObjects();
                for (int i = 0; i < objects.getCount(); i++) {
                    MapObject object = objects.get(i);

                    if (object instanceof RectangleMapObject) {
                        Rectangle rect = ((RectangleMapObject) object).getRectangle();
                        if (rect.contains(point)) return true;
                    } else if (object instanceof PolygonMapObject) {
                        Polygon polygon = ((PolygonMapObject) object).getPolygon();
                        if (polygon.contains(point.x, point.y)) return true;
                    }
				    /*else if (object instanceof PolylineMapObject) {
				        Polyline chain = ((PolylineMapObject) object).getPolyline();
				        // do something with chain...
				    }*/
                    else if (object instanceof CircleMapObject) {
                        Circle circle = ((CircleMapObject) object).getCircle();
                        if (circle.contains(point)) return true;
                    }
                }
            }
        }
        return false;
    }

    public Vector2 getStartPosition() {
        MapLayer layer = layers.get("object start");
        Rectangle rect = ((RectangleMapObject) layer.getObjects().get(0)).getRectangle();
        Vector2 pos = new Vector2(0, 0);
        pos.x = (((rect.getX() / Assets.TILE_SIZE) + ((rect.getWidth() * scale) / 2)));
        pos.y = (((rect.getY() / Assets.TILE_SIZE) + ((rect.getHeight() * scale) / 2)));

        return new Vector2(pos.x, pos.y);
    }

    public MapLayers getColliders() {
        MapLayers colliders = new MapLayers();
        for (int objIndex : collisionLayers) {
            String name = layers.get(objIndex).getName().toLowerCase();
            if (name.contains("ground") || name.contains("walls")) {
                colliders.add(layers.get(objIndex));
            }
        }
        return colliders;
    }
}
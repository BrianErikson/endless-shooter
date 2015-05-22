package com.beariksonstudios.endlessshooter.props;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

/**
 * Created by Brian on 5/22/2015.
 */
public class HealthBar {

    private SpriteDrawable background;
    private SpriteDrawable foreground;
    private Vector2 position;
    private Vector2 size;

    private int curHealth;
    private int minHealth;
    private int maxHealth;

    public HealthBar(int curHealth, int minHealth, int maxHealth, Vector2 size) {
        this.curHealth = curHealth;
        this.minHealth = minHealth;
        this.maxHealth = maxHealth;
        this.size = size;

        Pixmap pixMap = new Pixmap(1, 1, Pixmap.Format.RGB888);
        pixMap.setColor(Color.WHITE);
        pixMap.fill();
        Texture texture = new Texture(pixMap);
        Sprite redSprite = new Sprite(texture);
        redSprite.setColor(Color.RED);
        redSprite.setSize(0.1f, 0.05f);
        Sprite greenSprite = new Sprite(texture);
        greenSprite.setColor(Color.GREEN);
        greenSprite.setSize(0.1f, 0.05f);
        background = new SpriteDrawable(redSprite);
        foreground = new SpriteDrawable(greenSprite);
        position = new Vector2(0,0);
    }


    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 pos) {
        position.x = pos.x;
        position.y = pos.y;
    }

    public void draw(SpriteBatch batch) {
        background.draw(batch, position.x, position.y, size.x, size.y);
        foreground.draw(batch, position.x, position.y, size.x * ((float)curHealth / (float)maxHealth), size.y);
    }

    public Vector2 getSize() {
        return size;
    }

    public void setSize(float w, float h) {
        size.x = w;
        size.y = h;
    }

    public int getHealth() {
        return curHealth;
    }

    public void setHealth(int curHealth) {
        this.curHealth = curHealth;
    }

    public void setPosition(float x, float y) {
        position.x = x;
        position.y = y;
    }
}

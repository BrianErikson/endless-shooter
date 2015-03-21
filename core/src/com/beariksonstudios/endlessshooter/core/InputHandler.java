package com.beariksonstudios.endlessshooter.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputProcessor;
import com.beariksonstudios.endlessshooter.classes.Character;


public class InputHandler implements InputProcessor {
    private Action jump;
    private Action crouch;
    private Action moveLeft;
    private Action moveRight;
    private Action stop;
    private Action fire;
    private Action secondary;


    public InputHandler() {
        Gdx.input.setInputProcessor(this);
        jump = new Action.JumpAction();
        crouch = new Action.CrouchAction();
        moveLeft = new Action.MoveLeftAction();
        moveRight = new Action.MoveRightAction();
        fire = new Action.FireAction();
        stop = new Action.StopAction();
        secondary = new Action.FireAction(); //temp
    }

    public void handleInput(Character character) {
        if (Gdx.input.isKeyPressed(Input.Keys.W) && Gdx.input.isKeyJustPressed(Input.Keys.W))
            jump.execute(character);
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
            jump.execute(character);
        if (Gdx.input.isKeyPressed(Input.Keys.S))
            crouch.execute(character);

        if (Gdx.input.isKeyPressed(Input.Keys.A))
            moveLeft.execute(character);
        else if (Gdx.input.isKeyPressed(Input.Keys.D))
            moveRight.execute(character);
        else
            stop.execute(character);

        if (Gdx.input.isButtonPressed(Buttons.LEFT) && Gdx.input.justTouched())
            fire.execute(character);
        if (Gdx.input.isButtonPressed(Buttons.RIGHT) && Gdx.input.justTouched())
            secondary.execute(character);
    }

    @Override
    public boolean keyDown(int keycode) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        // TODO Auto-generated method stub
        return false;
    }
}

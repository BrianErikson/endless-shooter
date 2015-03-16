package com.beariksonstudios.endlessshooter.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Buttons;


public class InputHandler implements InputProcessor{
	private Action jump;
	private Action crouch;
	private Action moveLeft;
	private Action moveRight;
	private Action fire;
	private Action secondary;
	private boolean mouseClicked;
	private int button;
	
	public InputHandler() {
		Gdx.input.setInputProcessor(this);
		jump = new Action.JumpAction();
		crouch = new Action.CrouchAction();
		moveLeft = new Action.MoveLeftAction();
		moveRight = new Action.MoveRightAction();
		fire = new Action.FireAction();
		secondary = new Action.FireAction(); //temp
	}
	
	public void handleInput(Character character) {
		if (character.isLocal()) {
			switch(character.getPlayer()) {
			case ONE:
				if (Gdx.input.isKeyPressed(Input.Keys.W)) jump.execute(character);
				if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) jump.execute(character);
				if (Gdx.input.isKeyPressed(Input.Keys.S)) crouch.execute(character);
				if (Gdx.input.isKeyPressed(Input.Keys.A)) moveLeft.execute(character);
				if (Gdx.input.isKeyPressed(Input.Keys.D)) moveRight.execute(character);
				if (mouseClicked && button == Buttons.LEFT) fire.execute(character);
				if (mouseClicked && button == Buttons.RIGHT) secondary.execute(character);
				break;
			case TWO:
				if (Gdx.input.isKeyPressed(Input.Keys.I)) jump.execute(character);
				if (Gdx.input.isKeyPressed(Input.Keys.K)) crouch.execute(character);
				if (Gdx.input.isKeyPressed(Input.Keys.J)) moveLeft.execute(character);
				if (Gdx.input.isKeyPressed(Input.Keys.L)) moveRight.execute(character);
				if (Gdx.input.isKeyPressed(Input.Keys.U)) fire.execute(character);
				if (Gdx.input.isKeyPressed(Input.Keys.O)) secondary.execute(character);
				if (mouseClicked && button == Buttons.LEFT) fire.execute(character);
				if (mouseClicked && button == Buttons.RIGHT) secondary.execute(character);
				break;
			case THREE:
				if (Gdx.input.isKeyPressed(Input.Keys.W)) jump.execute(character);
				if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) jump.execute(character);
				if (Gdx.input.isKeyPressed(Input.Keys.S)) crouch.execute(character);
				if (Gdx.input.isKeyPressed(Input.Keys.A)) moveLeft.execute(character);
				if (Gdx.input.isKeyPressed(Input.Keys.D)) moveRight.execute(character);
				if (mouseClicked && button == Buttons.LEFT) fire.execute(character);
				if (mouseClicked && button == Buttons.RIGHT) secondary.execute(character);
				break;
			case FOUR:
				if (Gdx.input.isKeyPressed(Input.Keys.W)) jump.execute(character);
				if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) jump.execute(character);
				if (Gdx.input.isKeyPressed(Input.Keys.S)) crouch.execute(character);
				if (Gdx.input.isKeyPressed(Input.Keys.A)) moveLeft.execute(character);
				if (Gdx.input.isKeyPressed(Input.Keys.D)) moveRight.execute(character);
				if (mouseClicked && button == Buttons.LEFT) fire.execute(character);
				if (mouseClicked && button == Buttons.RIGHT) secondary.execute(character);
				break;
			}
		}
		else {
			// PLACEHOLDER FOR NETWORK PLAY
		}
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
		if (button == Input.Buttons.LEFT) {
	          mouseClicked = true;
	          this.button = button;
	          return true;
	      }
	      return false;
    }
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		mouseClicked = false;
		this.button = 0;
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

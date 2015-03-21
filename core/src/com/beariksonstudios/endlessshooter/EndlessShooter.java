package com.beariksonstudios.endlessshooter;

import com.badlogic.gdx.Game;
import com.beariksonstudios.endlessshooter.core.Assets;
import com.beariksonstudios.endlessshooter.levels.Test;

public class EndlessShooter extends Game {
	
	@Override
	public void create() {
		Assets.init();
		this.setScreen(new Test());
	}

	@Override
	public void dispose() {

	}

	@Override
	public void render() {		
		super.render();
	}

	@Override
	public void resize(int width, int height) {
        this.getScreen().resize(width, height);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}

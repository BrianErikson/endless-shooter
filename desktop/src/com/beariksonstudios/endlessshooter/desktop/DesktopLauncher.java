package com.beariksonstudios.endlessshooter.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.beariksonstudios.endlessshooter.EndlessShooter;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1600;
    	config.height = 1216;
		new LwjglApplication(new EndlessShooter(), config);
	}
}

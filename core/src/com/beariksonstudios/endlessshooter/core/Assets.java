package com.beariksonstudios.endlessshooter.core;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public final class Assets {
	public static AssetManager manager = new AssetManager();
	public static TmxMapLoader mapLoader = new TmxMapLoader(new InternalFileHandleResolver());
	public static final float WORLD_TO_BOX = 0.03125f;
	public static final float BOX_TO_WORLD = 32f;
	public static final float TILE_SIZE = 32f;
	
	public Assets() {
		
	}
	
	public static void init() {
		//manager.load("data/maps/test/test.tmx", TiledMap.class);
		manager.load("data/uiskin.json", Skin.class);
		manager.finishLoading();
	}

}

package com.beariksonstudios.endlessshooter.core;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.beariksonstudios.endlessshooter.tools.Debugger;

public class HUD {

	private Stage uiStage;
	public HUD(Stage uiStage, World world){
		this.uiStage = uiStage;
		Skin labelSkin = Assets.manager.get("data/uiskin.json", Skin.class);
        Debugger debugger = new Debugger(labelSkin, uiStage, world);
        debugger.setPosition(uiStage.getWidth()*(0.05f), uiStage.getHeight()*(0.90f));
        uiStage.addActor(debugger);
        
	}

}

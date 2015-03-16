package com.beariksonstudios.endlessshooter.core;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.beariksonstudios.endlessshooter.classes.Assassin;
import com.beariksonstudios.endlessshooter.classes.Scout;
import com.beariksonstudios.endlessshooter.classes.Sniper;
import com.beariksonstudios.endlessshooter.classes.Soldier;

public class PlayerFactory {
	private static int playerCount = 0;
	
	public static enum Player {
		ONE,
		TWO,
		THREE,
		FOUR
	};
	public static enum PlayerType {
		ASSASSIN,
		SCOUT,
		SNIPER,
		SOLDIER
	};
	
	/** creates a new Character. If four Characters have already been created, the builder will return null. **/
	public static Character buildChar(boolean local, PlayerType type, Vector2 startPos, World physicsWorld, float scale, Camera camera) {
		playerCount++;
		
		if (playerCount > 4) {
			return null;
		}
		else {
			Player playerNum = null;
			switch(playerCount) {
			case 1: 
				playerNum = Player.ONE;
				break;
			case 2: 
				playerNum = Player.TWO;
				break;
			case 3: 
				playerNum = Player.THREE;
				break;
			case 4: 
				playerNum = Player.FOUR;
				break;
			}
			
			
			Character player = null;
			switch(type) {
			case ASSASSIN:
				player = new Assassin(local, playerNum, startPos, physicsWorld, scale, camera);
				break;
			case SCOUT:
				player = new Scout(local, playerNum, startPos, physicsWorld, scale, camera);
				break;
			case SNIPER:
				player = new Sniper(local, playerNum, startPos, physicsWorld, scale, camera);
				break;
			case SOLDIER:
				player = new Soldier(local, playerNum, startPos, physicsWorld, scale, camera);
				break;
			}
			
			return player;
		}
	}
	
	public static int getCharacterNum() {
		return playerCount;
	}
}

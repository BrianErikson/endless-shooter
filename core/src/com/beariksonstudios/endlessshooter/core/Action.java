package com.beariksonstudios.endlessshooter.core;

public interface Action {

	void execute(Character player);
	
	class JumpAction implements Action {
		public void execute(Character player) {
			player.jump();
		}
	}
	
	class FireAction implements Action {

		@Override
		public void execute(Character player) {
			player.fire();
		}
	}
	
	class CrouchAction implements Action {

		@Override
		public void execute(Character player) {
			player.crouch();
		}
	}
	
	class MoveLeftAction implements Action {

		@Override
		public void execute(Character player) {
			player.moveLeft();
		}
	}
	
	class MoveRightAction implements Action {

		@Override
		public void execute(Character player) {
			player.moveRight();
		}
	}
	class StopAction implements Action {

		@Override
		public void execute(Character player) {
			player.stop();
			
		}
	}
}


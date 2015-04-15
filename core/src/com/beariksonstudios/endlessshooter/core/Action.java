package com.beariksonstudios.endlessshooter.core;

import com.beariksonstudios.endlessshooter.classes.Player;

public interface Action {

    void execute(Player player);

    class JumpAction implements Action {
        public void execute(Player player) {
            player.jump();
        }
    }

    class FireAction implements Action {

        @Override
        public void execute(Player player) {
            player.fire();
        }
    }

    class CrouchAction implements Action {

        @Override
        public void execute(Player player) {
            player.crouch();
        }
    }

    class MoveLeftAction implements Action {

        @Override
        public void execute(Player player) {
            player.moveLeft();
        }
    }

    class MoveRightAction implements Action {

        @Override
        public void execute(Player player) {
            player.moveRight();
        }
    }

    class StopAction implements Action {

        @Override
        public void execute(Player player) {
            player.stop();

        }
    }
}


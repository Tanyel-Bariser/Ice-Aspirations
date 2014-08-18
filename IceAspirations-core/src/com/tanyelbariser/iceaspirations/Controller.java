package com.tanyelbariser.iceaspirations;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.tanyelbariser.iceaspirations.entities.Player;

public class Controller implements InputProcessor {
	
	private Player player;

	public Controller (Player player) {
		this.player = player;
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (player.canJump() && !player.isDazed()) {
			AudioManager.playJumpSound();
			player.setDown(0);
			player.setAngle(0);
			player.jump();
			player.setCanJump(false);
			player.setStanding(false);
		}
		return false;
	}

	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
		case Keys.LEFT:
			player.leftForce();
			break;
		case Keys.RIGHT:
			player.rightForce();
			break;
		case Keys.SPACE:
			if (player.canJump() && !player.isDazed()) {
				AudioManager.playJumpSound();
				player.setDown(0);
				player.setAngle(0);
				player.jump();
				player.setCanJump(false);
				player.setStanding(false);
			}
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Keys.LEFT || keycode == Keys.RIGHT) {
			player.setForce(0);
		}
		return false;
	}

	// Unused methods from interface
	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
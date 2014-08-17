package com.tanyelbariser.iceaspirations;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.tanyelbariser.iceaspirations.screens.MainScreen;

public class IceAspirations extends Game {
	private static BitmapFont blue;
	private Screen nextScreen;

	@Override
	public void create() {
		Assets.load();
		Assets.MANAGER.finishLoading();
		AudioManager.playMainMusic();
		blue = new BitmapFont(Gdx.files.internal("blue.fnt"), false);
		setScreen(new MainScreen(this));
	}

	@Override
	public void dispose() {
		super.dispose();
		Assets.dispose();
	}

	// nextScreen field & Override in render() & setNextScreen() methods are a
	// fix to overcome a bug that prevents switching screens in render() method
	// of GameScreen after allotted time runs out.
	public void setNextScreen(Screen nextScreen) {
		this.nextScreen = nextScreen;
	}

	@Override
	public void render() {
		super.render();
		if (nextScreen != null) {
			setScreen(nextScreen);
			nextScreen = null;
		}
	}

	public static BitmapFont getBlue() {
		return blue;
	}
}
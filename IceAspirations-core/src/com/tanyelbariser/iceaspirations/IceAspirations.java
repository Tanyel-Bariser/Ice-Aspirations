package com.tanyelbariser.iceaspirations;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.tanyelbariser.iceaspirations.screens.MainScreen;

public class IceAspirations extends Game {
	private static BitmapFont blue;
	private Screen nextScreen;
	private static Assets assets;
	private static AudioManager audio;

	@Override
	public void create() {
		assets = new Assets();
		assets.load();
		assets.getManager().finishLoading();
		audio = new AudioManager();
		blue  = assets.getManager().get(Assets.BLUE_FONT, BitmapFont.class);
		setScreen(new MainScreen(this));
	}
	
	public static Assets getAssets() {
		return assets;
	}
	
	public static AudioManager getAudio() {
		return audio;
	}

	@Override
	public void dispose() {
		super.dispose();
		assets.dispose();
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
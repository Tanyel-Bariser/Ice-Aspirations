package com.tanyelbariser.iceaspirations;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.tanyelbariser.iceaspirations.screens.MainScreen;

public class IceAspirations extends Game {
	private static Preferences prefs;
	private static Music music;
	private static Music carrotMusic;
	private static Music timeOutMusic;
	private static BitmapFont blue;
	private static ImageButton soundButton;
	private Screen nextScreen;

	@Override
	public void create() {
		Assets.load();
		Assets.MANAGER.finishLoading();
		prefs = Gdx.app.getPreferences("IceAspirations");
		blue = new BitmapFont(Gdx.files.internal("blue.fnt"), false);
		setScreen(new MainScreen(this));
		musicSetup();
	}

	private void musicSetup() {
		music = Gdx.audio.newMusic(Gdx.files.internal("Rise of spirit.mp3"));
		music.setLooping(true);
		if (!prefs.getBoolean("Mute")) {
			music.play();
		}
		music.setVolume(0.2f);
		
		carrotMusic = Gdx.audio.newMusic(Gdx.files
				.internal("Carrot Mode.mp3"));
		carrotMusic.setLooping(false);
		carrotMusic.setVolume(0.2f);
		
		timeOutMusic = Gdx.audio.newMusic(Gdx.files
				.internal("Time Running Out.mp3"));
		timeOutMusic.setLooping(false);
		timeOutMusic.setVolume(0.2f);
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

	public static ImageButton getSoundButton() {
		return soundButton;
	}

	public static Music getMusic() {
		return music;
	}

	public static Music getTimeOutMusic() {
		return timeOutMusic;
	}

	public static Music getCarrotMusic() {
		return carrotMusic;
	}

	public static Preferences getPrefs() {
		return prefs;
	}
}
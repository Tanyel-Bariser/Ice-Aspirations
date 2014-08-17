package com.tanyelbariser.iceaspirations;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.tanyelbariser.iceaspirations.factories.SpriteFactory;
import com.tanyelbariser.iceaspirations.screens.GameScreen;

public class IceAspirations extends Game {
	private static Music music;
	private static Music carrotMusic;
	private static Music timeOutMusic;
	private static Skin skin;
	private static BitmapFont blue;
	private static ImageButton soundButton;
	private static Sprite background;
	private Screen nextScreen;

	@Override
	public void create() {
		Assets.load();
		Assets.MANAGER.finishLoading();
		TextureAtlas atlas = new TextureAtlas("Atlas.atlas");
		skin = new Skin(atlas);
		blue = new BitmapFont(Gdx.files.internal("blue.fnt"), false);
		background = SpriteFactory.createBackground();
		muteButtonSetup();
		setScreen(new GameScreen(this));
		musicSetup();
	}

	private void musicSetup() {
		music = Gdx.audio.newMusic(Gdx.files.internal("Rise of spirit.mp3"));
		music.setLooping(true);
		music.play();
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

	private void muteButtonSetup() {
		TextureAtlas atlas = new TextureAtlas("Atlas.atlas");
		Skin skin = new Skin(atlas);

		ImageButtonStyle imageStyle = new ImageButtonStyle();
		imageStyle.up = skin.getDrawable("Unmute");
		imageStyle.down = skin.getDrawable("Mute");
		imageStyle.checked = imageStyle.down;

		soundButton = new ImageButton(imageStyle);

		float soundButtonSize = Gdx.graphics.getWidth() / 6;
		soundButton.setSize(soundButtonSize, soundButtonSize);

		soundButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				boolean checked = soundButton.isChecked();
				if (checked) {
					soundButton.setChecked(true);
					IceAspirations.music.pause();
				} else {
					soundButton.setChecked(false);
					IceAspirations.music.play();
				}
			}
		});
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

	public static Sprite getBackground() {
		return background;
	}

	public static BitmapFont getBlue() {
		return blue;
	}

	public static Skin getSkin() {
		return skin;
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
}
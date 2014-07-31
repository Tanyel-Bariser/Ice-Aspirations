package com.tanyelbariser.iceaspirations;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.tanyelbariser.iceaspirations.screens.GameScreen;
import com.tanyelbariser.iceaspirations.screens.MainScreen;

public class IceAspirations extends Game {
	public static Music music;
	public static Skin skin;
	public static BitmapFont blue;
	public static ImageButton soundButton;
	public static Sprite background;
	
	@Override
	public void create() {
		TextureAtlas atlas = new TextureAtlas("Atlas.atlas");
		skin = new Skin(atlas);
		blue = new BitmapFont(Gdx.files.internal("blue.fnt"), false);
		background = new Sprite(new Texture("Background.png"));
		muteButtonSetup();
		setScreen(new GameScreen(this)); //temporary to make debugging easier
		musicSetup();
	}

	private void musicSetup() {
		music = Gdx.audio.newMusic(Gdx.files.internal("Rise of spirit.mp3"));
		music.setLooping(true);
//		music.play();
		music.setVolume(0.2f);
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
		music.dispose();
		skin.dispose();
		blue.dispose();
	}
}
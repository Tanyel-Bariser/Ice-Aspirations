package com.tanyelbariser.iceaspirations.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton.ImageTextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.tanyelbariser.iceaspirations.IceAspirations;

public class MainScreen implements Screen {
	IceAspirations iceA;
	SpriteBatch batch;
	Sprite background, title;
	static BitmapFont blue;
	Stage stage;
	ImageTextButtonStyle style;
	ImageTextButton play, highscores, quit;
	ImageButtonStyle imageStyle;
	ImageButton sound;
	TextureAtlas atlas;
	Skin skin;
	float width = Gdx.graphics.getWidth();
	float height = Gdx.graphics.getHeight();
	Music music;

	public MainScreen(IceAspirations iceA) {
		this.iceA = iceA;
	}

	@Override
	public void show() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		
		batch = new SpriteBatch();

		background = new Sprite(new Texture("Background.png"));

		title = new Sprite(new Texture("Title.png"));
		float titleX = width / 2 - title.getWidth() / 2;
		float titleY = Math.round(height / 1.4);
		title.setPosition(titleX, titleY);
		title.setScale(width / 768);

		blue = new BitmapFont(Gdx.files.internal("blue.fnt"), false);
		blue.setScale(width / 300);
		style = new ImageTextButtonStyle();
		style.font = blue;

		atlas = new TextureAtlas("Buttons.atlas");
		skin = new Skin(atlas);

		musicSetUp();
		playButtonSetUp();
		highscoreButtonSetUp();
		quitButtonSetup();
	}

	// Enables game music and mute/unmute button
	private void musicSetUp() {
		music = Gdx.audio.newMusic(Gdx.files.internal("Rise of spirit.mp3"));
		music.setLooping(true);
		//music.play();
		music.setVolume(0.2f);

		imageStyle = new ImageButtonStyle();
		imageStyle.up = skin.getDrawable("unmute");
		imageStyle.down = skin.getDrawable("mute");
		imageStyle.checked = imageStyle.down;

		sound = new ImageButton(imageStyle);

		float soundSize = width / 6;
		sound.setSize(soundSize, soundSize);

		sound.setPosition(0, 0);

		sound.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				boolean checked = sound.isChecked();
				if (checked) {
					sound.setChecked(true);
					music.pause();
				} else {
					sound.setChecked(false);
					music.play();
				}
			}
		});
		stage.addActor(sound);
	}

	// Button to transition to game screen
	private void playButtonSetUp() {
		play = new ImageTextButton("Play", style);

		float x = width / 2 - play.getWidth() / 2;
		float y = height / 2;
		play.setPosition(x, y);

		play.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				iceA.setScreen(new GameScreen(iceA));
			}
		});
		stage.addActor(play);
	}

	// Button to transition to high scores screen
	private void highscoreButtonSetUp() {
		highscores = new ImageTextButton("High Scores", style);

		float x = width / 2 - highscores.getWidth() / 2;
		float y = height / 2 - highscores.getHeight();
		highscores.setPosition(x, y);

		highscores.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				iceA.setScreen(new HighScoresScreen(iceA));
			}
		});
		stage.addActor(highscores);
	}

	// Button to exit game
	private void quitButtonSetup() {
		quit = new ImageTextButton("Quit", style);
		
		float x = width / 2 - quit.getWidth() / 2;
		float y = height / 2 - quit.getHeight() * 2;
		quit.setPosition(x, y);
		
		quit.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
			}
		});
		stage.addActor(quit);
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		background.draw(batch);
		title.draw(batch);
		batch.end();

		stage.act(delta);
		stage.draw();
	}

	@Override
	public void pause() {
	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void dispose() {
		batch.dispose();
		background.getTexture().dispose();
		title.getTexture().dispose();
		stage.dispose();
		atlas.dispose();
	}

	@Override
	public void resume() {
	}
}
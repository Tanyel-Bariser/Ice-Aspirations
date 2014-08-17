package com.tanyelbariser.iceaspirations.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton.ImageTextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.tanyelbariser.iceaspirations.IceAspirations;
import com.tanyelbariser.iceaspirations.factories.ButtonFactory;
import com.tanyelbariser.iceaspirations.factories.SpriteFactory;

public class MainScreen implements Screen {
	private IceAspirations iceA;
	private SpriteBatch batch;
	private Sprite title;

	private Stage stage;
	private ImageTextButtonStyle style;
	private ImageTextButton play, highscores, exit;
	private float width = Gdx.graphics.getWidth();
	private float height = Gdx.graphics.getHeight();
	private Sprite background;

	public MainScreen(IceAspirations iceA) {
		this.iceA = iceA;
	}

	@Override
	public void show() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		batch = new SpriteBatch();
		title = SpriteFactory.createTitle();
		background = SpriteFactory.createBackground();

		BitmapFont blue = IceAspirations.getBlue();
		blue.setScale(width / 300);
		style = new ImageTextButtonStyle();
		style.font = blue;
		
		soundButtonSetup();
		playButtonSetUp();
		highscoreButtonSetUp();
		exitButtonSetup();
	}
	

	private void soundButtonSetup() {
		final Preferences prefs = IceAspirations.getPrefs();
		if (!prefs.contains("Mute")) {
			prefs.putBoolean("Mute", false);
			prefs.flush();
		}
		final ImageButton soundButton = ButtonFactory
				.createImageButton("Unmute", "Mute", 0, 0, prefs.getBoolean("Mute"));
		Gdx.app.log("TAG", String.valueOf(prefs.getBoolean("Mute")));
		soundButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				boolean changeMute = prefs.getBoolean("Mute"); 
				prefs.putBoolean("Mute", !changeMute);
				if (!changeMute) {
					IceAspirations.getMusic().pause();
					soundButton.setChecked(true);
				} else {
					IceAspirations.getMusic().play();
					soundButton.setChecked(false);
				}
				prefs.flush();
				Gdx.app.log("TAG", String.valueOf(prefs.getBoolean("Mute")));
			}
		});
		soundButton.setPosition(0, 0);
		stage.addActor(soundButton);
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
				iceA.setScreen(new HighScoresScreen(iceA, 0));
			}
		});
		stage.addActor(highscores);
	}

	// Button to exit game
	private void exitButtonSetup() {
		exit = new ImageTextButton("Exit", style);

		float x = width / 2 - exit.getWidth() / 2;
		float y = height / 2 - exit.getHeight() * 2;
		exit.setPosition(x, y);

		exit.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
			}
		});
		stage.addActor(exit);
	}

	@Override
	public void resize(int width, int height) {
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
		stage.dispose();
	}

	@Override
	public void resume() {
	}
}
package com.tanyelbariser.iceaspirations.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton.ImageTextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.tanyelbariser.iceaspirations.IceAspirations;

public class MainScreen implements Screen {
	IceAspirations iceA;
	SpriteBatch batch;
	Sprite background, title;
	
	Stage stage;
	ImageTextButtonStyle style;
	ImageTextButton play, highscores, quit;
	float width = Gdx.graphics.getWidth();
	float height = Gdx.graphics.getHeight();

	public MainScreen(IceAspirations iceA) {
		this.iceA = iceA;
	}

	@Override
	public void show() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		
		batch = new SpriteBatch();

		title = new Sprite(new Texture("Title.png"));
		float titleX = width / 2 - title.getWidth() / 2;
		float titleY = Math.round(height / 1.4);
		title.setPosition(titleX, titleY);
		title.setScale(width / 768);

		IceAspirations.blue.setScale(width / 300);
		style = new ImageTextButtonStyle();
		style.font = IceAspirations.blue;

		IceAspirations.soundButton.setPosition(0, 0);
		stage.addActor(IceAspirations.soundButton);
		
		playButtonSetUp();
		highscoreButtonSetUp();
		quitButtonSetup();
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
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		IceAspirations.background.draw(batch);
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
		title.getTexture().dispose();
		stage.dispose();
	}

	@Override
	public void resume() {
	}
}
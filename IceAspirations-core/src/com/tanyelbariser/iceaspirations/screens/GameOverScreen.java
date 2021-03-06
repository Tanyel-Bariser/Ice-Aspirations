package com.tanyelbariser.iceaspirations.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.tanyelbariser.iceaspirations.AudioManager;
import com.tanyelbariser.iceaspirations.IceAspirations;
import com.tanyelbariser.iceaspirations.factories.ButtonFactory;
import com.tanyelbariser.iceaspirations.factories.SpriteFactory;

public class GameOverScreen implements Screen {
	private IceAspirations iceA;
	private AudioManager audio = IceAspirations.getAudio();
	private int maxHeight = 0;
	private Stage stage;
	private final float WIDTH = Gdx.graphics.getWidth();
	private final float HEIGHT = Gdx.graphics.getHeight();
	private Sprite background;

	public GameOverScreen(IceAspirations iceA, int maxHeight) {
		this.iceA = iceA;
		this.maxHeight = maxHeight;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Music doesn't always work first time so is in render method to be
		// invoked multiple times to make sure
		audio.stopLowTimeMusic();
		audio.playMainMusic();

		SpriteBatch batch = new SpriteBatch();
		batch.begin();
		background.draw(batch);
		batch.end();

		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		background = SpriteFactory.createBackground();
		background.setSize(WIDTH, HEIGHT);
		// Create Label to show remaining game time
		BitmapFont blue = IceAspirations.getBlue();
		LabelStyle style = new LabelStyle(blue, Color.BLUE);
		Label gameOver = new Label("GAME OVER\nScore: "
				+ String.valueOf(maxHeight), style);
		gameOver.setPosition(WIDTH / 2 - gameOver.getWidth() / 2, HEIGHT / 2
				- gameOver.getHeight() / 2);
		stage.addActor(gameOver);

		backButtonSetUp();

	}

	private void backButtonSetUp() {
		ImageButton back = new ButtonFactory().createImageButton("Back", "Back", 0,
				0, false);
		back.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				iceA.setScreen(new HighScoresScreen(iceA, maxHeight));
			}
		});
		stage.addActor(back);
	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		stage.dispose();
	}
}
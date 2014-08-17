package com.tanyelbariser.iceaspirations.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.tanyelbariser.iceaspirations.IceAspirations;
import com.tanyelbariser.iceaspirations.factories.ButtonFactory;

public class GameOverScreen implements Screen {
	private IceAspirations iceA;
	private int maxHeight = 0;
	private Stage stage;
	private final float WIDTH = Gdx.graphics.getWidth();
	private final float HEIGHT = Gdx.graphics.getHeight();

	public GameOverScreen(IceAspirations iceA, int maxHeight) {
		this.iceA = iceA;
		this.maxHeight = maxHeight;
		if (IceAspirations.getTimeOutMusic().isPlaying()) {
			IceAspirations.getTimeOutMusic().stop();
			IceAspirations.getMusic().play();
		}
		if (IceAspirations.getCarrotMusic().isPlaying()) {
			IceAspirations.getCarrotMusic().stop();
			IceAspirations.getMusic().play();
		}
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		SpriteBatch batch = new SpriteBatch();
		batch.begin();
		IceAspirations.getBackground().draw(batch);
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
		ImageButton back = ButtonFactory
				.createImageButton("Back", "Back", 0, 0);
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
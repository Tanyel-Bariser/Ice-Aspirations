package com.tanyelbariser.iceaspirations.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.tanyelbariser.iceaspirations.IceAspirations;

public class GameOverScreen implements Screen {
	private IceAspirations iceA;
	private Stage stage;
	private final float WIDTH = Gdx.graphics.getWidth();
	private final float HEIGHT = Gdx.graphics.getHeight();

	public GameOverScreen(IceAspirations iceA) {
		this.iceA = iceA;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		SpriteBatch batch = new SpriteBatch();
		batch.begin();
		IceAspirations.background.draw(batch);
		batch.end();

		stage.act(delta);
		stage.draw();
		
		if (Gdx.input.isTouched()) {
			iceA.setScreen(new MainScreen(iceA));
		}
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		stage = new Stage();

		// Create Label to show remaining game time
		LabelStyle style = new LabelStyle(IceAspirations.blue, Color.BLUE);
		Label gameOver = new Label("GAME OVER", style);
		gameOver.setPosition(WIDTH /2 - gameOver.getWidth() / 2,
				HEIGHT / 2- gameOver.getHeight() / 2);
		stage.addActor(gameOver);

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}

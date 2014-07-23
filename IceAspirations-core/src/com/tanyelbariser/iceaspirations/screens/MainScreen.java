package com.tanyelbariser.iceaspirations.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton.ImageTextButtonStyle;
import com.tanyelbariser.iceaspirations.IceAspirations;

public class MainScreen implements Screen {
	IceAspirations iceA;
	SpriteBatch batch;
	Sprite sprite;
	BitmapFont blue;
	Stage stage;
	ImageTextButtonStyle style;
	ImageTextButton play, highscores, quit;

	public MainScreen(IceAspirations iceA) {
		this.iceA = iceA;
	}

	@Override
	public void show() {
		stage = new Stage();
		batch = new SpriteBatch();
		sprite = new Sprite(new Texture("Main Screen.png"));
		sprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		blue = new BitmapFont(Gdx.files.internal("blue.fnt"), false);
		style = new ImageTextButtonStyle();
		style.font = blue;

		//Create play button in main screen to transition to game screen
		play = new ImageTextButton("Play", style);
		float playX = Gdx.graphics.getWidth() / 2 - play.getWidth() / 2;
		float playY = Gdx.graphics.getHeight() / 2 - play.getHeight() / 2;
		play.setPosition(playX, playY);
		stage.addActor(play);
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		sprite.draw(batch);
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
		sprite.getTexture().dispose();
		blue.dispose();
		stage.dispose();
		
		
	}

	@Override
	public void resume() {
	}
}
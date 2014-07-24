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
	Sprite background, title;
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
		background = new Sprite(new Texture("Background.png"));
		title = new Sprite(new Texture("Title.png"));
		float titleX = Gdx.graphics.getWidth() / 2 - title.getWidth() /  2;
		float titleY = Math.round(Gdx.graphics.getHeight() / 1.3);
		title.setPosition(titleX, titleY);
		blue = new BitmapFont(Gdx.files.internal("blue.fnt"), false);
		blue.setScale(Gdx.graphics.getWidth() / 300);
		style = new ImageTextButtonStyle();
		style.font = blue;
		
		//Create play button in main screen to transition to game screen
		play = new ImageTextButton("Play", style);
		float playX = Gdx.graphics.getWidth() / 2 - play.getWidth() / 2;
		float playY = Gdx.graphics.getHeight() / 2;
		play.setPosition(playX, playY);
		stage.addActor(play);

		//Create play button in main screen to transition to high scores screen
		highscores = new ImageTextButton("High Scores", style);
		float highscoresX = Gdx.graphics.getWidth() / 2 - highscores.getWidth() / 2;
		float highscoresY = Gdx.graphics.getHeight() / 2 - highscores.getHeight();
		highscores.setPosition(highscoresX, highscoresY);
		stage.addActor(highscores);
		
		//Create quit button in main screen to exit game
		quit = new ImageTextButton("Quit", style);
		float quitX = Gdx.graphics.getWidth() / 2 - quit.getWidth() / 2;
		float quitY = Gdx.graphics.getHeight() / 2 - quit.getHeight() * 2;
		quit.setPosition(quitX, quitY);
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
		blue.dispose();
		stage.dispose();
	}

	@Override
	public void resume() {
	}
}
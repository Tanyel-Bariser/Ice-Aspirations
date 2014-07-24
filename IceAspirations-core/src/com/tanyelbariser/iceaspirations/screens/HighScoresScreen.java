package com.tanyelbariser.iceaspirations.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.tanyelbariser.iceaspirations.IceAspirations;

public class HighScoresScreen implements Screen {
	IceAspirations iceA;
	SpriteBatch batch;
	Sprite background;
	Stage stage;
	Table table;
	LabelStyle style;
	Label heading;
	float width = Gdx.graphics.getWidth();
	float height = Gdx.graphics.getHeight();

	public HighScoresScreen(IceAspirations iceA) {
		this.iceA = iceA;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		background.draw(batch);
		batch.end();
		
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height);
		table.invalidateHierarchy();
	}

	@Override
	public void show() {
		batch = new SpriteBatch();
		background = new Sprite(new Texture("Background.png"));
		
		style = new LabelStyle(MainScreen.blue, Color.BLUE);
		heading = new Label("High Scores", style);
		
		table = new Table();
		table.setBounds(0, 0, width, height);
		stage = new Stage();
		table.add(heading);
		stage.addActor(table);
	}

	@Override
	public void hide() {
		dispose();
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
		batch.dispose();
		background.getTexture().dispose();
	}
}
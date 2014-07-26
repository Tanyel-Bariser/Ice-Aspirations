package com.tanyelbariser.iceaspirations.screens;

import java.util.Random;

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
		stage.getViewport().update(width, height, true);
		table.invalidateHierarchy();
		table.setSize(width, height);
	}

	@Override
	public void show() {
		batch = new SpriteBatch();
		background = new Sprite(new Texture("Background.png"));
		
		Random random = new Random();
		String score = "1) " + String.valueOf(random.nextInt(10000));
		String score1 = "2) " + String.valueOf(random.nextInt(10000));
		String score2 = "3) " + String.valueOf(random.nextInt(10000));
		
		
		style = new LabelStyle(MainScreen.blue, Color.BLUE);
		heading = new Label("High Scores", style);
		Label points = new Label(score, style);
		Label points1 = new Label(score1, style);
		Label points2 = new Label(score2, style);
		
		table = new Table();
		table.setBounds(0, 0, width, height);
		table.add(heading).row();
		table.add(points).row();
		table.add(points1).row();
		table.add(points2);
		
		stage = new Stage();
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
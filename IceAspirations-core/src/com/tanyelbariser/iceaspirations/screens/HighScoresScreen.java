package com.tanyelbariser.iceaspirations.screens;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
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
		String score1 = "1) " + String.valueOf(random.nextInt(100) + 400);
		String score2 = "2) " + String.valueOf(random.nextInt(100) + 300);
		String score3 = "3) " + String.valueOf(random.nextInt(100) + 200);
		String score4 = "4) " + String.valueOf(random.nextInt(100) + 100);
		String score5 = "5) " + String.valueOf(random.nextInt(100));
		
		
		style = new LabelStyle(MainScreen.blue, Color.BLUE);
		heading = new Label("High Scores", style);
		
		Label points1 = new Label(score1, style);
		Label points2 = new Label(score2, style);
		Label points3 = new Label(score3, style);
		Label points4 = new Label(score4, style);
		Label points5 = new Label(score5, style);
		
		table = new Table();
		table.setBounds(0, 0, width, height);
		table.add(heading).center().row();
		table.add(points1).row();
		table.add(points2).row();
		table.add(points3).row();
		table.add(points4).row();
		table.add(points5);
		
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		
		stage.addActor(table);
		
		backButtonSetUp();
	}

	private void backButtonSetUp() {
		
		TextureAtlas atlas = new TextureAtlas("Atlas.atlas");
		Skin skin = new Skin(atlas);
		
		ImageButtonStyle imageStyle = new ImageButtonStyle();
		imageStyle.up = skin.getDrawable("Back");
		imageStyle.down = skin.getDrawable("Back");

		ImageButton back = new ImageButton(imageStyle);

		float backSize = width / 6;
		back.setSize(backSize, backSize);

		back.setPosition(0, 0);
		
		back.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				iceA.setScreen(new MainScreen(iceA));
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
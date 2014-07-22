package com.tanyelbariser.iceaspirations.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tanyelbariser.iceaspirations.IceAspirations;

public class MainScreen implements Screen {
	IceAspirations iceA;
	SpriteBatch batch;
	Sprite sprite;

	public MainScreen(IceAspirations iceA) {
		this.iceA = iceA;
	}
	
	@Override
	public void show() {
		batch = new SpriteBatch();
		sprite = new Sprite(new Texture("Mock Screen.jpg"));
		sprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());		
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
	}
	
	@Override
	public void resume() {
	}
}
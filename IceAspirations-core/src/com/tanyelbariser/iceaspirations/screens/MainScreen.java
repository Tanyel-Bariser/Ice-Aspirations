package com.tanyelbariser.iceaspirations.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MainScreen implements Screen {
	SpriteBatch batch;
	Sprite sprite;
	final String TAG = "IN MAINSCREEN"; 

	@Override
	public void show() {
		Gdx.app.log(TAG, "show()");
		batch = new SpriteBatch();
		sprite = new Sprite(new Texture("Cartoon Sky.jpg"));
		sprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());		
	}
	
	@Override
	public void resize(int width, int height) {
		Gdx.app.log(TAG, "resize(int width, int height)");	
	}
	
	@Override
	public void render(float delta) {
		Gdx.app.log(TAG, "render(float delta)");
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		sprite.draw(batch);
		batch.end();
	}

	@Override
	public void pause() {
		Gdx.app.log(TAG, "pause()");
	}

	@Override
	public void hide() {
		Gdx.app.log(TAG, "hide()");
		dispose();	
	}

	@Override
	public void dispose() {
		Gdx.app.log(TAG, "dipose()");
		batch.dispose();
		sprite.getTexture().dispose();
	}
	
	@Override
	public void resume() {
		Gdx.app.log(TAG, "resume()");
	}
}
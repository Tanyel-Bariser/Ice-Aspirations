package com.tanyelbariser.iceaspirations;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.tanyelbariser.iceaspirations.screens.MainScreen;

public class IceAspirations extends Game {
	//Will get rid of logging information later,
	// just here to get me used to app lifecycle
	final String TAG = "IN ICEASPIRATIONS";
	
	@Override
	public void create() {
		Gdx.app.log(TAG, "create()");
		setScreen(new MainScreen());
	}

	@Override
	public void setScreen(Screen screen) {
		Gdx.app.log(TAG, "setScreen(Screen screen)");
		super.setScreen(screen);
	}

	@Override
	public void resize(int width, int height) {
		Gdx.app.log(TAG, "resize(int width, int height)");
		super.resize(width, height);
	}

	@Override
	public void render() {
		Gdx.app.log(TAG, "render()");
		super.render();
	}

	@Override
	public void pause() {
		Gdx.app.log(TAG, "pause()");
		super.pause();
	}
	
	@Override
	public void dispose() {
		Gdx.app.log(TAG, "dipose()");
		super.dispose();
	}

	@Override
	public void resume() {
		Gdx.app.log(TAG, "resume()");
		super.resume();
	}

	@Override
	public Screen getScreen() {
		Gdx.app.log(TAG, "getScreen()");
		return super.getScreen();
	}
}
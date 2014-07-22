package com.tanyelbariser.iceaspirations;

import com.badlogic.gdx.Game;
import com.tanyelbariser.iceaspirations.screens.MainScreen;

public class IceAspirations extends Game {
	@Override
	public void create() {
		setScreen(new MainScreen(this));
	}
}
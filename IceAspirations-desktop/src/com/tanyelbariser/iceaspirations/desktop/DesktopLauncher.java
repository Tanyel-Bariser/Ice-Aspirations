package com.tanyelbariser.iceaspirations.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.tanyelbariser.iceaspirations.IceAspirations;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		//Changed width & height to exactly half the pixels of my phone to fit onto my laptop screen
		config.width = 384;
		config.height = 640;
		config.resizable = false;
		new LwjglApplication(new IceAspirations(), config);
	}
}

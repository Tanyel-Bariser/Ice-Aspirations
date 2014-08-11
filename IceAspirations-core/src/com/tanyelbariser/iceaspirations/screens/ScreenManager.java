package com.tanyelbariser.iceaspirations.screens;

import com.tanyelbariser.iceaspirations.IceAspirations;

public class ScreenManager {
	private static MainScreen mainScreen;
	private static GameScreen gameScreen;
	private static HighScoresScreen highScoresScreen;
	private static GameOverScreen gameOverScreen;
	
	
	public ScreenManager(IceAspirations iceA) {
		mainScreen = new MainScreen(iceA);
		gameScreen = new GameScreen(iceA);
		highScoresScreen = new HighScoresScreen(iceA, 0);
		gameOverScreen = new GameOverScreen(iceA, 0);
	}


	public static MainScreen getMainScreen() {
		return mainScreen;
	}


	public static GameScreen getGameScreen() {
		return gameScreen;
	}


	public static HighScoresScreen getHighScoresScreen() {
		return highScoresScreen;
	}


	public static GameOverScreen getGameOverScreen() {
		return gameOverScreen;
	}
}
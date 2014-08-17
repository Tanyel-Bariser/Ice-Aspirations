package com.tanyelbariser.iceaspirations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;

public class AudioManager {

	private static final Preferences prefs = Gdx.app.getPreferences("IceAspirations");
	private static final Music MAIN_MUSIC = Assets.MANAGER.get(
			Assets.MAIN_MUSIC, Music.class);
	private static final Music SUPER_MUSIC = Assets.MANAGER.get(
			Assets.SUPER_MUSIC, Music.class); 
	private static final Music LOW_TIME_MUSIC = Assets.MANAGER.get(
			Assets.LOW_TIME_MUSIC, Music.class); 

	public static void playMainMusic() {
		stopSuperMusic();
		if (!mainMusicIsPlaying() && !prefs.getBoolean("Mute")) {
			MAIN_MUSIC.setLooping(true);
			MAIN_MUSIC.setVolume(0.2f);
			MAIN_MUSIC.play();
		}
	}

	public static void pauseMainMusic() {
		if (mainMusicIsPlaying()) {
			MAIN_MUSIC.pause();
		}
	}

	private static boolean mainMusicIsPlaying() {
		return MAIN_MUSIC.isPlaying();
	}
	
	public static void playSuperMusic() {
		pauseMainMusic();
		if (!superMusicIsPlaying() && !prefs.getBoolean("Mute")) {
			SUPER_MUSIC.setLooping(false);
			SUPER_MUSIC.setVolume(0.2f);
			SUPER_MUSIC.play();
		}
	}

	private static void stopSuperMusic() {
		if (superMusicIsPlaying()) {
			SUPER_MUSIC.stop();
			Gdx.app.log("TAG", "STOP SUPER MUSIC");
		}
	}

	private static boolean superMusicIsPlaying() {
		return SUPER_MUSIC.isPlaying();
	}
	
	public static void playLowTimeMusic() {
		if(!lowTimeMusicIsPlaying() && !prefs.getBoolean("Mute")) {
			LOW_TIME_MUSIC.setLooping(false);
			LOW_TIME_MUSIC.setVolume(0.2f);
			LOW_TIME_MUSIC.play();
		}
	}
	
	public static void stopLowTimeMusic() {
		if(lowTimeMusicIsPlaying()) {
			LOW_TIME_MUSIC.stop();
		}
	}

	private static boolean lowTimeMusicIsPlaying() {
		return LOW_TIME_MUSIC.isPlaying();
	}
}
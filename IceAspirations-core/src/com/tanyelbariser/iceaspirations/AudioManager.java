package com.tanyelbariser.iceaspirations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AudioManager {

	private static final Preferences prefs = Gdx.app
			.getPreferences("IceAspirations");
	private Assets assets;
	private Music mainMusic;
	private Music superMusic;
	private Music lowTimeMusic;
	private Sound jumpSound;
	private Sound getItemSound;
	private Sound hitSound;
	
	
	public AudioManager() {
		assets = IceAspirations.getAssets();
		mainMusic = assets.getManager().get(
				Assets.MAIN_MUSIC, Music.class);
		mainMusic.setLooping(true);
		mainMusic.setVolume(0.2f);
		superMusic = assets.getManager().get(
				Assets.SUPER_MUSIC, Music.class);
		superMusic.setLooping(false);
		superMusic.setVolume(0.2f);
		lowTimeMusic = assets.getManager().get(
				Assets.LOW_TIME_MUSIC, Music.class);
		lowTimeMusic.setLooping(false);
		lowTimeMusic.setVolume(0.2f);
		jumpSound = assets.getManager().get(
				Assets.JUMP_SOUND, Sound.class);
		getItemSound = assets.getManager().get(
				Assets.PICK_UP_SOUND, Sound.class);
		hitSound = assets.getManager().get(Assets.HIT_SOUND,
				Sound.class);
	}

	public void playMainMusic() {
		stopSuperMusic();
		if (!mainMusicIsPlaying() && !prefs.getBoolean("Mute")) {
			mainMusic.play();
		}
	}

	public void pauseMainMusic() {
		if (mainMusicIsPlaying()) {
			mainMusic.pause();
		}
	}

	private boolean mainMusicIsPlaying() {
		return mainMusic.isPlaying();
	}

	public void playSuperMusic() {
		pauseMainMusic();
		if (!superMusicIsPlaying() && !prefs.getBoolean("Mute")) {
			superMusic.play();
		}
	}

	private void stopSuperMusic() {
		if (superMusicIsPlaying()) {
			superMusic.stop();
		}
	}

	private boolean superMusicIsPlaying() {
		return superMusic.isPlaying();
	}

	public void playLowTimeMusic() {
		if (!lowTimeMusicIsPlaying() && !prefs.getBoolean("Mute")) {
			lowTimeMusic.play();
		}
	}

	public void stopLowTimeMusic() {
		if (lowTimeMusicIsPlaying()) {
			lowTimeMusic.stop();
		}
	}

	private boolean lowTimeMusicIsPlaying() {
		return lowTimeMusic.isPlaying();
	}
	
	public void playJumpSound() {
		if (!prefs.getBoolean("Mute")) {
			jumpSound.play(0.1f, 0.8f, 0);
		}
	}

	public void playPickUpSound() {
		getItemSound.play(0.1f, 0.8f, 0);
	}

	public void playHitSound() {
		hitSound.play(1, 0.8f, 0);
	}
}
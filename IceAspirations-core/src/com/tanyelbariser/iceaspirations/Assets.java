package com.tanyelbariser.iceaspirations;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Assets {
	private AssetManager manager;
	
	public static final String ATLAS = "Atlas.atlas";
	public static final String BACKGROUND = "Background.png";
	public static final String MOUNTAINS = "Mountains.png";
	public static final String N_LIGHTS = "NorthernLights.png";
	public static final String GALAXY = "Galaxy.jpg";
	public static final String BLUE_FONT = "blue.fnt";
	public static final String YELLOW_FONT = "yellow.fnt";
	public static final String RED_FONT = "red.fnt";
	public static final String MAIN_MUSIC = "Rise of spirit.mp3";
	public static final String LOW_TIME_MUSIC = "Time Running Out.mp3";
	public static final String SUPER_MUSIC = "Carrot Mode.mp3";
	public static final String HIT_SOUND = "Hit Sound.wav";
	public static final String JUMP_SOUND = "Jumping.wav";
	public static final String PICK_UP_SOUND = "Pick Up.ogg";
	
	public Assets() {
		manager = new AssetManager();
	}
	
	public AssetManager getManager() {
		return manager;
	}
	
	public void load() {
		manager.load(ATLAS, TextureAtlas.class);
		manager.load(BACKGROUND, Texture.class);
		manager.load(MOUNTAINS, Texture.class);
		manager.load(N_LIGHTS, Texture.class);
		manager.load(GALAXY, Texture.class);
		manager.load(BLUE_FONT, BitmapFont.class);
		manager.load(YELLOW_FONT, BitmapFont.class);
		manager.load(RED_FONT, BitmapFont.class);
		manager.load(MAIN_MUSIC, Music.class);
		manager.load(LOW_TIME_MUSIC, Music.class);
		manager.load(SUPER_MUSIC, Music.class);
		manager.load(HIT_SOUND, Sound.class);
		manager.load(JUMP_SOUND, Sound.class);
		manager.load(PICK_UP_SOUND, Sound.class);
	}
	
	public void dispose() {
		manager.dispose();
	}
}

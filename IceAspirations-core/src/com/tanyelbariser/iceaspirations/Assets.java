package com.tanyelbariser.iceaspirations;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Assets {
	public static final AssetManager MANAGER = new AssetManager();
	
	public static final String ATLAS = "Atlas.atlas";
	public static final String BACKGROUND = "Background.png";
	public static final String BLUE_FONT = "blue.fnt";
	public static final String YELLOW_FONT = "yellow.fnt";
	public static final String RED_FONT = "red.fnt";
	public static final String MAIN_MUSIC = "Rise of spirit.mp3";
	public static final String LOW_TIME_MUSIC = "Time Running Out.mp3";
	public static final String SUPER_MUSIC = "Carrot Mode.mp3";
	public static final String HIT_SOUND = "Hit Sound.wav";
	public static final String JUMP_SOUND = "Jumping.wav";
	public static final String PICK_UP_SOUND = "Pick Up.ogg";
	
	public static void load() {
		MANAGER.load(ATLAS, TextureAtlas.class);
		MANAGER.load(BACKGROUND, Texture.class);
		MANAGER.load(BLUE_FONT, BitmapFont.class);
		MANAGER.load(YELLOW_FONT, BitmapFont.class);
		MANAGER.load(RED_FONT, BitmapFont.class);
		MANAGER.load(MAIN_MUSIC, Music.class);
		MANAGER.load(LOW_TIME_MUSIC, Music.class);
		MANAGER.load(SUPER_MUSIC, Music.class);
		MANAGER.load(HIT_SOUND, Sound.class);
		MANAGER.load(JUMP_SOUND, Sound.class);
		MANAGER.load(PICK_UP_SOUND, Sound.class);
	}
	
	public static void dispose() {
		MANAGER.dispose();
	}
}

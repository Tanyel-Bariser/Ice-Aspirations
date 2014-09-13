package com.tanyelbariser.iceaspirations.factories;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.tanyelbariser.iceaspirations.Assets;
import com.tanyelbariser.iceaspirations.IceAspirations;

public class SpriteFactory {
	private Skin skin;
	private final static float WIDTH = Gdx.graphics.getWidth();
	private final static float HEIGHT = Gdx.graphics.getHeight();

	public SpriteFactory() {
		skin = new Skin(IceAspirations.getAssets().getManager()
				.get(Assets.ATLAS, TextureAtlas.class));
	}

	public static Sprite createBackground() {
		return new Sprite(IceAspirations.getAssets().getManager()
				.get(Assets.BACKGROUND, Texture.class));
	}

	public static Sprite createMountains() {
		return new Sprite(IceAspirations.getAssets().getManager()
				.get(Assets.MOUNTAINS, Texture.class));
	}
	
	public static Sprite createNLights() {
		return new Sprite(IceAspirations.getAssets().getManager()
				.get(Assets.N_LIGHTS, Texture.class));
	}
	
	public static Sprite createGalaxy() {
		return new Sprite(IceAspirations.getAssets().getManager()
				.get(Assets.GALAXY, Texture.class));
	}

	public Sprite createTitle() {
		Sprite sprite = skin.getSprite("Title");
		float titleX = WIDTH / 2 - sprite.getWidth() / 2;
		float titleY = Math.round(HEIGHT / 1.4);
		sprite.setPosition(titleX, titleY);
		sprite.setScale(WIDTH / 768);
		return sprite;
	}

	public Sprite createBoulder() {
		Sprite boulder = skin.getSprite("Boulder");
		boulder.setSize(5f, 5f);
		boulder.setOrigin(boulder.getWidth() / 2, boulder.getHeight() / 2);
		return boulder;
	}

	public Sprite createClock() {
		Sprite clock = skin.getSprite("Clock");
		clock.setPosition(0, -50);
		clock.setSize(2, 2);
		clock.setOrigin(clock.getWidth() / 2, clock.getHeight() / 2);
		return clock;
	}

	public Sprite createCarrot() {
		Sprite carrot = skin.getSprite("Carrot");
		carrot.setPosition(0, -50);
		carrot.setSize(2, 2);
		carrot.setOrigin(carrot.getWidth() / 2, carrot.getHeight() / 2);
		return carrot;
	}

	public Sprite createPlayerSprite(String name, float width, float height) {
		Sprite sprite = skin.getSprite(name);
		sprite.setSize(width, height);
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
		return sprite;
	}

	public Sprite createPlatformSprite(String name, float width, float height) {
		Sprite sprite = skin.getSprite(name);
		sprite.setSize(width, height);
		return sprite;
	}
}
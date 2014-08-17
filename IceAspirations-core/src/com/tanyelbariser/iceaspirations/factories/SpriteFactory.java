package com.tanyelbariser.iceaspirations.factories;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.tanyelbariser.iceaspirations.Assets;

public class SpriteFactory {
	private static final Skin SKIN = new Skin(Assets.MANAGER.get(Assets.ATLAS,
			TextureAtlas.class));
	private final static float WIDTH = Gdx.graphics.getWidth();
	private final static float HEIGHT = Gdx.graphics.getHeight();

	private SpriteFactory() {
	}

	public static Sprite createBackground() {
		return new Sprite(Assets.MANAGER.get(Assets.BACKGROUND, Texture.class));
	}

	public static Sprite createTitle() {
		Sprite sprite = SKIN.getSprite("Title");
		float titleX = WIDTH / 2 - sprite.getWidth() / 2;
		float titleY = Math.round(HEIGHT / 1.4);
		sprite.setPosition(titleX, titleY);
		sprite.setScale(WIDTH / 768);
		return sprite;
	}
	
	public static Sprite createBoulder() {
		Sprite boulder = SKIN.getSprite("Boulder");
		boulder.setSize(5f, 5f);
		boulder.setOrigin(boulder.getWidth() / 2, boulder.getHeight() / 2);
		return boulder;
	}

	public static Sprite createClock() {
		Sprite clock = SKIN.getSprite("Clock");
		clock.setPosition(0, -50);
		clock.setSize(2, 2);
		clock.setOrigin(clock.getWidth() / 2, clock.getHeight() / 2);
		return clock;
	}

	public static Sprite createCarrot() {
		Sprite carrot = SKIN.getSprite("Carrot");
		carrot.setPosition(0, -50);
		carrot.setSize(2, 2);
		carrot.setOrigin(carrot.getWidth() / 2, carrot.getHeight() / 2);
		return carrot;
	}

	public static Sprite createPlayerSprite(String name, float width, float height) {
		Sprite sprite = SKIN.getSprite(name);
		sprite.setSize(width, height);
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
		return sprite;
	}

	public static Sprite createPlatformSprite(String name, float width, float height) {
		Sprite sprite = SKIN.getSprite(name);
		sprite.setSize(width, height);
		return sprite;
	}
}
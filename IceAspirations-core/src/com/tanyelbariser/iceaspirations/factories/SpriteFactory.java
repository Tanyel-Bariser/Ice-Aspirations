package com.tanyelbariser.iceaspirations.factories;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.tanyelbariser.iceaspirations.Assets;

public class SpriteFactory {
	private static final Skin SKIN = new Skin(Assets.MANAGER.get(Assets.ATLAS,
			TextureAtlas.class));

	private SpriteFactory() {
	}

	public static Sprite createBackground() {
		return new Sprite(Assets.MANAGER.get(Assets.BACKGROUND, Texture.class));
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
		carrot.setOrigin(carrot.getWidth() / 2,
				carrot.getHeight() / 2);
		return carrot;
	}

	public static Sprite createStand() {
		Sprite standSprite = SKIN.getSprite("Rabbit1");
		standSprite.setSize(2.9f, 4.2f);
		standSprite.setOrigin(standSprite.getWidth() / 2,
				standSprite.getHeight() / 2);
		return standSprite;
	}
	
	public static Sprite createJump1() {
		Sprite jump1 = SKIN.getSprite("Rabbit2");
		jump1.setSize(2.9f, 4.2f);
		jump1.setOrigin(jump1.getWidth() / 2, jump1.getHeight() / 2);
		return jump1;
	}
	
	public static Sprite createJump2() {
		Sprite jump2 = SKIN.getSprite("Rabbit3");
		jump2.setSize(2.3f, 4.2f);
		jump2.setOrigin(jump2.getWidth() / 2, jump2.getHeight() / 2);
		return jump2;
	}
	
	public static Sprite createJump3() {
		Sprite jump3 = SKIN.getSprite("Rabbit4");
		jump3.setSize(2.3f, 4.2f);
		jump3.setOrigin(jump3.getWidth() / 2, jump3.getHeight() / 2);
		return jump3;
	}
	
	public static Sprite createJump4() {
		Sprite jump4 = SKIN.getSprite("Rabbit5");
		jump4.setSize(2.3f, 4.2f);
		jump4.setOrigin(jump4.getWidth() / 2, jump4.getHeight() / 2);
		return jump4;
	}
	
	public static Sprite createFalling() {
		Sprite falling = SKIN.getSprite("Rabbit6");
		falling.setSize(3, 4.2f);
		falling.setOrigin(falling.getWidth() / 2,
				falling.getHeight() / 2);
		return falling;
	}
	
	public static Sprite createDazed() {
		Sprite dazed = SKIN.getSprite("Dazed");
		dazed.setSize(3, 4.2f);
		dazed.setOrigin(dazed.getWidth() / 2,
				dazed.getHeight() / 2);
		return dazed;
	}
}

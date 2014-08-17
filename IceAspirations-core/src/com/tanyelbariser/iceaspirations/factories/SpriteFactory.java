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
		carrot.setOrigin(carrot.getWidth() / 2, carrot.getHeight() / 2);
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
		falling.setOrigin(falling.getWidth() / 2, falling.getHeight() / 2);
		return falling;
	}

	public static Sprite createDazed() {
		Sprite dazed = SKIN.getSprite("Dazed");
		dazed.setSize(3, 4.2f);
		dazed.setOrigin(dazed.getWidth() / 2, dazed.getHeight() / 2);
		return dazed;
	}

	public static Sprite createSpecialJump1() {
		Sprite jump1 = SKIN.getSprite("SpecialJumping1");
		jump1.setSize(2.9f, 4.2f);
		jump1.setOrigin(jump1.getWidth() / 2, jump1.getHeight() / 2);
		return jump1;
	}

	public static Sprite createSpecialJump2() {
		Sprite jump2 = SKIN.getSprite("SpecialJumping2");
		jump2.setSize(2.985f, 4.33f);
		jump2.setOrigin(jump2.getWidth() / 2, jump2.getHeight() / 2);
		return jump2;
	}

	public static Sprite createSpecialJump3() {
		Sprite jump3 = SKIN.getSprite("SpecialJumping3");
		jump3.setSize(3.113f, 4.38f);
		jump3.setOrigin(jump3.getWidth() / 2, jump3.getHeight() / 2);
		return jump3;
	}

	public static Sprite createSpecialJump4() {
		Sprite jump4 = SKIN.getSprite("SpecialJumping4");
		jump4.setSize(3.07f, 5f);
		jump4.setOrigin(jump4.getWidth() / 2, jump4.getHeight() / 2);
		return jump4;
	}

	public static Sprite createSpecialFall1() {
		Sprite fall1 = SKIN.getSprite("SpecialFalling1");
		fall1.setSize(2.9f, 4.2f);
		fall1.setOrigin(fall1.getWidth() / 2, fall1.getHeight() / 2);
		return fall1;
	}

	public static Sprite createSpecialFall2() {
		Sprite fall2 = SKIN.getSprite("SpecialFalling2");
		fall2.setSize(2.863f, 4.2f);
		fall2.setOrigin(fall2.getWidth() / 2, fall2.getHeight() / 2);
		return fall2;
	}

	public static Sprite createSpecialFall3() {
		Sprite fall3 = SKIN.getSprite("SpecialFalling3");
		fall3.setSize(2.9f, 4.1328f);
		fall3.setOrigin(fall3.getWidth() / 2, fall3.getHeight() / 2);
		return fall3;
	}

	public static Sprite createSpecialStand1() {
		Sprite stand1 = SKIN.getSprite("SpecialStanding1");
		stand1.setSize(3.5f, 3.15f);
		stand1.setOrigin(stand1.getWidth() / 2, stand1.getHeight() / 2);
		return stand1;
	}

	public static Sprite createSpecialStand2() {
		Sprite stand2 = SKIN.getSprite("SpecialStanding2");
		stand2.setSize(4.375f, 3.15f);
		stand2.setOrigin(stand2.getWidth() / 2, stand2.getHeight() / 2);
		return stand2;
	}

	public static Sprite createSpecialStand3() {
		Sprite stand3 = SKIN.getSprite("SpecialStanding3");
		stand3.setSize(3.395f, 3.15f);
		stand3.setOrigin(stand3.getWidth() / 2, stand3.getHeight() / 2);
		return stand3;
	}

}
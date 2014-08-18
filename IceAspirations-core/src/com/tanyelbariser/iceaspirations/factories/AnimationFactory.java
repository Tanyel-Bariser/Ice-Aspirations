package com.tanyelbariser.iceaspirations.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class AnimationFactory {

	public static Animation createJumpAnimation() {
		Sprite jump1 = SpriteFactory.createPlayerSprite("Rabbit2", 2.9f, 4.2f);
		Sprite jump2 = SpriteFactory.createPlayerSprite("Rabbit3", 2.3f, 4.2f);
		Sprite jump3 = SpriteFactory.createPlayerSprite("Rabbit4", 2.3f, 4.2f);
		Sprite jump4 = SpriteFactory.createPlayerSprite("Rabbit5", 2.3f, 4.2f);
		Sprite[] jumpSprites = new Sprite[] { jump1, jump2, jump3, jump4 };
		return new Animation(0.1f, jumpSprites);
	}

	public static Animation createSpecialJumpAnimation() {
		Sprite jump1 = SpriteFactory.createPlayerSprite("SpecialJumping1",
				2.9f, 4.2f);
		Sprite jump2 = SpriteFactory.createPlayerSprite("SpecialJumping2",
				2.985f, 4.33f);
		Sprite jump3 = SpriteFactory.createPlayerSprite("SpecialJumping3",
				3.113f, 4.38f);
		Sprite jump4 = SpriteFactory.createPlayerSprite("SpecialJumping4",
				3.07f, 5f);
		Sprite[] specialStandSprites = new Sprite[] { jump1, jump2, jump3,
				jump4, jump3, jump2 };
		return new Animation(0.05f, specialStandSprites);
	}

	public static Animation createSpecialFallAnimation() {
		Sprite fall1 = SpriteFactory.createPlayerSprite("SpecialFalling1",
				2.9f, 4.2f);
		Sprite fall2 = SpriteFactory.createPlayerSprite("SpecialFalling2",
				2.863f, 4.2f);
		Sprite fall3 = SpriteFactory.createPlayerSprite("SpecialFalling3",
				2.9f, 4.1328f);
		Sprite[] specialFallSprites = new Sprite[] { fall1, fall2, fall3, fall2 };
		return new Animation(0.1f, specialFallSprites);
	}

	public static Animation createSpecialStandAnimation() {
		Sprite stand1 = SpriteFactory.createPlayerSprite("SpecialStanding1",
				3.5f, 3.15f);
		Sprite stand2 = SpriteFactory.createPlayerSprite("SpecialStanding2",
				4.375f, 3.15f);
		Sprite stand3 = SpriteFactory.createPlayerSprite("SpecialStanding3",
				3.395f, 3.15f);
		Sprite[] specialStandSprites = new Sprite[] { stand1, stand2, stand3 };
		return new Animation(0.1f, specialStandSprites);
	}
}
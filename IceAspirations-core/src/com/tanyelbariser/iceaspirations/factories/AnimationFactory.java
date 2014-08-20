package com.tanyelbariser.iceaspirations.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class AnimationFactory {
	private SpriteFactory spriteFactory;

	public AnimationFactory() {
		spriteFactory = new SpriteFactory();
	}

	public Animation createJumpAnimation() {
		Sprite jump1 = spriteFactory.createPlayerSprite("Rabbit2", 2.9f, 4.2f);
		Sprite jump2 = spriteFactory.createPlayerSprite("Rabbit3", 2.3f, 4.2f);
		Sprite jump3 = spriteFactory.createPlayerSprite("Rabbit4", 2.3f, 4.2f);
		Sprite jump4 = spriteFactory.createPlayerSprite("Rabbit5", 2.3f, 4.2f);
		Sprite[] jumpSprites = new Sprite[] { jump1, jump2, jump3, jump4 };
		return new Animation(0.1f, jumpSprites);
	}

	public Animation createSpecialJumpAnimation() {
		Sprite jump1 = spriteFactory.createPlayerSprite("SpecialJumping1",
				2.9f, 4.2f);
		Sprite jump2 = spriteFactory.createPlayerSprite("SpecialJumping2",
				2.985f, 4.33f);
		Sprite jump3 = spriteFactory.createPlayerSprite("SpecialJumping3",
				3.113f, 4.38f);
		Sprite jump4 = spriteFactory.createPlayerSprite("SpecialJumping4",
				3.07f, 5f);
		Sprite[] specialStandSprites = new Sprite[] { jump1, jump2, jump3,
				jump4, jump3, jump2 };
		return new Animation(0.05f, specialStandSprites);
	}

	public Animation createSpecialFallAnimation() {
		Sprite fall1 = spriteFactory.createPlayerSprite("SpecialFalling1",
				2.9f, 4.2f);
		Sprite fall2 = spriteFactory.createPlayerSprite("SpecialFalling2",
				2.863f, 4.2f);
		Sprite fall3 = spriteFactory.createPlayerSprite("SpecialFalling3",
				2.9f, 4.1328f);
		Sprite[] specialFallSprites = new Sprite[] { fall1, fall2, fall3, fall2 };
		return new Animation(0.1f, specialFallSprites);
	}

	public Animation createSpecialStandAnimation() {
		Sprite stand1 = spriteFactory.createPlayerSprite("SpecialStanding1",
				3.5f, 3.15f);
		Sprite stand2 = spriteFactory.createPlayerSprite("SpecialStanding2",
				4.375f, 3.15f);
		Sprite stand3 = spriteFactory.createPlayerSprite("SpecialStanding3",
				3.395f, 3.15f);
		Sprite[] specialStandSprites = new Sprite[] { stand1, stand2, stand3 };
		return new Animation(0.1f, specialStandSprites);
	}
}
package com.tanyelbariser.iceaspirations.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.tanyelbariser.iceaspirations.AudioManager;
import com.tanyelbariser.iceaspirations.CollisionDetection;
import com.tanyelbariser.iceaspirations.factories.AnimationFactory;
import com.tanyelbariser.iceaspirations.factories.SpriteFactory;
import com.tanyelbariser.iceaspirations.platforms.Platforms;
import com.tanyelbariser.iceaspirations.screens.GameScreen;

public class Player {
	private Body body;
	private BodyDef bodyDef;
	private FixtureDef fixDef;
	private final float HEIGHT = Gdx.graphics.getHeight();
	private float jump = 300;
	private float force;
	private final float forceChange = 5000;
	private float myDelta;
	private boolean facingLeft = false;
	private CollisionDetection contact;
	
	private Sprite standSprite;
	private Sprite fallingSprite;
	private Sprite dazedSprite;
	private Animation specialJumpAnimation;
	private Animation jumpAnimation;
	private Animation fallAnimation;
	private Animation standAnimation;
	private float frameTime;
	private float timeDazed;

	public Player(World world) {
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(0, -HEIGHT / GameScreen.ZOOM / 3);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(0.5f, 1.5f);

		fixDef = new FixtureDef();
		fixDef.shape = shape;
		fixDef.density = 3f;
		fixDef.friction = 0f;
		fixDef.restitution = 0f;

		body = world.createBody(bodyDef);
		body.createFixture(fixDef).setUserData("player");

		animationSpriteSetUp();
		
		shape.dispose();
		Gdx.input.setCatchBackKey(true);
	}

	private void animationSpriteSetUp() {
		standSprite = SpriteFactory.createPlayerSprite("Rabbit1", 2.9f, 4.2f);
		fallingSprite = SpriteFactory.createPlayerSprite("Rabbit6", 3, 4.2f);
		dazedSprite = SpriteFactory.createPlayerSprite("Dazed", 3, 4.2f);
		jumpAnimation = AnimationFactory.createJumpAnimation();
		specialJumpAnimation = AnimationFactory.createSpecialJumpAnimation();
		fallAnimation = AnimationFactory.createSpecialFallAnimation();
		standAnimation = AnimationFactory.createSpecialStandAnimation();
	}
	
	
	public void setCollisionDetection(CollisionDetection contact) {
		this.contact = contact;
	}

	public void update(float delta) {
		if (contact.isCarrotTouched()) {
			jump = 500f;
		}
		myDelta = delta;
		body.setTransform(body.getPosition(), contact.getAngle());
		float accel = -Gdx.input.getAccelerometerX() * 2;
		body.setLinearVelocity((accel - contact.getSlippery()) * delta,
				body.getLinearVelocity().y + contact.getDown() * delta);
		body.applyForceToCenter(force * delta, contact.getDown(), true);
		if (accel < -0.3f || force < 0) {
			facingLeft = true;
		} else if (accel > 0.3f || force > 0) {
			facingLeft = false;
		}
		if (body.getPosition().x > Platforms.RIGHT_SCREEN_EDGE) {
			body.setTransform(Platforms.RIGHT_SCREEN_EDGE - 0.5f,
					body.getPosition().y, 0);
		} else if (body.getPosition().x < Platforms.LEFT_SCREEN_EDGE) {
			body.setTransform(Platforms.LEFT_SCREEN_EDGE + 0.5f,
					body.getPosition().y, 0);
		}
	}
	
	public Sprite updateSprite(float adjustedDelta, float delta) {
		Sprite playerSprite;
		frameTime += delta;
		if (contact.isDazed()) {
			playerSprite = dazedSprite;
			timeDazed += delta;
			if (timeDazed == delta) {
				AudioManager.playHitSound();
			}
			if (timeDazed > 3) {
				contact.setDazed(false);
				timeDazed = 0;
			}
		} else if (body.getLinearVelocity().y > 2) {
			if (contact.isCarrotTouched()) {
				playerSprite = (Sprite) specialJumpAnimation.getKeyFrame(
						frameTime, true);
			} else {
				playerSprite = (Sprite) jumpAnimation.getKeyFrame(frameTime,
						false);
			}
		} else if (body.getLinearVelocity().y < -4
				& !contact.isStanding()) {
			if (contact.isCarrotTouched()) {
				playerSprite = (Sprite) fallAnimation.getKeyFrame(frameTime,
						true);
			} else {
				playerSprite = fallingSprite;
				frameTime = 0;
			}
		} else {
			if (contact.isCarrotTouched()) {
				playerSprite = (Sprite) standAnimation.getKeyFrame(frameTime,
						true);
			} else {
				playerSprite = standSprite;
				frameTime = 0;
			}
		}
		boolean left = facingLeft && playerSprite.isFlipX();
		boolean right = !facingLeft
				&& !playerSprite.isFlipX();
		if (left || right) {
			// do nothing if player is moving left AND the playerSprite is
			// already facing left (same for right)
		} else {
			playerSprite.flip(true, false);
		}
		playerSprite.setPosition(body.getPosition().x
				- playerSprite.getWidth() / 2, body.getPosition().y
				- playerSprite.getHeight() / 2);
		playerSprite.setRotation(body.getAngle()
				* MathUtils.radiansToDegrees);
		return playerSprite;
	}

	public Body getBody() {
		return body;
	}

	public void jump() {
		body.applyLinearImpulse(0, jump * myDelta, body.getWorldCenter().x,
				body.getWorldCenter().y, true);
	}

	public void leftForce() {
		force -= forceChange + contact.getSlippery() * 10;
	}

	public void rightForce() {
		force += forceChange + contact.getSlippery() * 10;
	}

	public void setForce(float force) {
		this.force = force;
	}

	public boolean getFacingLeft() {
		return facingLeft;
	}
}
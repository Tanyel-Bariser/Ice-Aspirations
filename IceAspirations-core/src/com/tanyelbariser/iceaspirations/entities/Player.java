package com.tanyelbariser.iceaspirations.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.tanyelbariser.iceaspirations.IceAspirations;
import com.tanyelbariser.iceaspirations.factories.SpriteFactory;
import com.tanyelbariser.iceaspirations.platforms.Platforms;
import com.tanyelbariser.iceaspirations.screens.GameScreen;

public class Player implements ContactListener, InputProcessor {
	private Body body;
	private BodyDef bodyDef;
	private FixtureDef fixDef;
	private final float HEIGHT = Gdx.graphics.getHeight();
	private float angle;
	private float slippery;
	private float jump = 300;
	private float force;
	private final float forceChange = 5000;
	private float down;
	private float myDelta;
	private Sprite standSprite;
	private Sprite dazedSprite;
	private Sprite fallingSprite;
	private Animation jumpAnimation;
	private Sound jumpSound = Gdx.audio.newSound(Gdx.files
			.internal("Jumping.wav"));
	private Sound getItemSound = Gdx.audio.newSound(Gdx.files
			.internal("Pick Up.ogg"));
	private Sound hitSound = Gdx.audio.newSound(Gdx.files
			.internal("Hit Sound.wav"));;
	private boolean standing;
	private boolean canJump;
	private boolean facingLeft = false;
	private boolean clockTouched = false;
	private boolean dazed = false;
	private boolean carrotTouched = false;
	private Animation specialStandAnimation;
	private Animation specialJumpAnimation;
	private Animation specialFallAnimation;

	public Player(World world) {
		world.setContactListener(this);

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

		shape.dispose();
		createAnimation();
		createSpecialStandAnimation();
		createSpecialJumpAnimation();
		createSpecialFallAnimation();
		Gdx.input.setCatchBackKey(true);
	}

	public Body getBody() {
		return body;
	}

	public Animation getjumpAnimation() {
		return jumpAnimation;
	}

	public Animation getSpecialStandAnimation() {
		return specialStandAnimation;
	}

	public Animation getSpecialJumpAnimation() {
		return specialJumpAnimation;
	}

	public Animation getSpecialFallAnimation() {
		return specialFallAnimation;
	}

	public Sprite getStandSprite() {
		return standSprite;
	}

	public Sprite getFallingSprite() {
		return fallingSprite;
	}

	public Sprite getDazedSprite() {
		return dazedSprite;
	}

	public boolean isStanding() {
		return standing;
	}

	public boolean getFacingLeft() {
		return facingLeft;
	}

	public boolean isClockTouched() {
		return clockTouched;
	}

	public void setClockTouched(boolean clockTouched) {
		this.clockTouched = clockTouched;
	}

	public boolean isDazed() {
		return dazed;
	}

	public void setDazed(boolean dazed) {
		this.dazed = dazed;
	}

	public void playHitSound() {
		if (IceAspirations.getMusic().isPlaying()
				|| IceAspirations.getCarrotMusic().isPlaying()) {
			hitSound.play(1, 0.8f, 0);
		}
	}

	public boolean isCarrotTouched() {
		return carrotTouched;
	}

	public void setCarrotTouched(boolean carrotTouched) {
		this.carrotTouched = carrotTouched;
	}

	private void createAnimation() {
		Sprite jump1 = SpriteFactory.createPlayerSprite("Rabbit2", 2.9f, 4.2f);
		Sprite jump2 = SpriteFactory.createPlayerSprite("Rabbit3", 2.3f, 4.2f);
		Sprite jump3 = SpriteFactory.createPlayerSprite("Rabbit4", 2.3f, 4.2f);
		Sprite jump4 = SpriteFactory.createPlayerSprite("Rabbit5", 2.3f, 4.2f);
		Sprite[] jumpSprites = new Sprite[] { jump1, jump2, jump3, jump4 };
		jumpAnimation = new Animation(0.1f, jumpSprites);

		// non animation sprites
		standSprite = SpriteFactory.createPlayerSprite("Rabbit1", 2.9f, 4.2f);
		fallingSprite = SpriteFactory.createPlayerSprite("Rabbit6", 3, 4.2f);
		dazedSprite = SpriteFactory.createPlayerSprite("Dazed", 3, 4.2f);
	}

	private void createSpecialJumpAnimation() {
		Sprite jump1 = SpriteFactory.createPlayerSprite("SpecialJumping1", 2.9f, 4.2f);
		Sprite jump2 = SpriteFactory.createPlayerSprite("SpecialJumping2", 2.985f, 4.33f);
		Sprite jump3 = SpriteFactory.createPlayerSprite("SpecialJumping3", 3.113f, 4.38f);
		Sprite jump4 = SpriteFactory.createPlayerSprite("SpecialJumping4", 3.07f, 5f);
		Sprite[] specialStandSprites = new Sprite[] { jump1, jump2, jump3,
				jump4, jump3, jump2 };
		specialJumpAnimation = new Animation(0.05f, specialStandSprites);
	}

	private void createSpecialFallAnimation() {
		Sprite fall1 = SpriteFactory.createPlayerSprite("SpecialFalling1", 2.9f, 4.2f);
		Sprite fall2 = SpriteFactory.createPlayerSprite("SpecialFalling2", 2.863f, 4.2f);
		Sprite fall3 = SpriteFactory.createPlayerSprite("SpecialFalling3", 2.9f, 4.1328f);
		Sprite[] specialFallSprites = new Sprite[] { fall1, fall2, fall3, fall2 };
		specialFallAnimation = new Animation(0.1f, specialFallSprites);
	}

	private void createSpecialStandAnimation() {
		Sprite stand1 = SpriteFactory.createPlayerSprite("SpecialStanding1", 3.5f, 3.15f);
		Sprite stand2 = SpriteFactory.createPlayerSprite("SpecialStanding2", 4.375f, 3.15f);
		Sprite stand3 = SpriteFactory.createPlayerSprite("SpecialStanding3", 3.395f, 3.15f);
		Sprite[] specialStandSprites = new Sprite[] { stand1, stand2, stand3 };
		specialStandAnimation = new Animation(0.1f, specialStandSprites);
	}

	public void update(float delta) {
		if (carrotTouched) {
			jump = 400f;
		}
		myDelta = delta;
		body.setTransform(body.getPosition(), angle);
		float accel = -Gdx.input.getAccelerometerX() * 2;
		body.setLinearVelocity((accel - slippery) * delta,
				body.getLinearVelocity().y + down * delta);
		body.applyForceToCenter(force * delta, down, true);
		if (accel < -0.3f || force < 0) {
			facingLeft = true;
		} else if (accel > 0.3f || force > 0) {
			facingLeft = false;
		}
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		boolean touchLeftEdge = Platforms.LEFT_SCREEN_EDGE + 0.6f > body
				.getPosition().x;
		boolean touchRightEdge = Platforms.RIGHT_SCREEN_EDGE - 0.6f < body
				.getPosition().x;
		boolean feetContact = contact.getWorldManifold().getPoints()[0].y < body
				.getPosition().y;
		boolean headContact = contact.getWorldManifold().getPoints()[0].y > body
				.getPosition().y;
		String fixA = (String) contact.getFixtureA().getUserData();
		String fixB = (String) contact.getFixtureB().getUserData();
		boolean playerContact = fixA.equals("player") || fixB.equals("player");
		boolean boulderContact = fixA.equals("boulder")
				|| fixB.equals("boulder");
		boolean platformContact = fixA.equals("platform")
				|| fixB.equals("platform");
		boolean groundContact = fixA.equals("ground") || fixB.equals("ground");
		boolean justBoulder = boulderContact
				&& !(platformContact || groundContact);
		if (playerContact && boulderContact && headContact && !carrotTouched) {
			down = -10;
			dazed = true;
		} else if ((touchLeftEdge || touchRightEdge)
				&& !(playerContact && (platformContact || boulderContact))) {
			canJump = false;
			down = 0;
		} else if (feetContact) {
			if (playerContact && (platformContact || justBoulder)) {
				if (platformContact) {
					// FixtureB is a platform
					angle = contact.getFixtureB().getBody().getAngle();
				} else {
					angle = 0;
				}
				slippery = angle * 15;
				down = -1.5f;
				canJump = standing = true;
			} else if (playerContact && groundContact) {
				down = angle = slippery = 0;
				canJump = standing = true;
			}
		}
	}

	@Override
	public void endContact(Contact contact) {
		down = 0;
		standing = false;
		// Delay in resetting slippery causes player to jump off platform at an
		// angle perpendicular to the platform & allows smoother sliding on
		// platforms & continued movement in the same direction after contact.
		Timer.schedule(new Task() {
			@Override
			public void run() {
				slippery = 0;
				canJump = false;
			}
		}, 0.5f);
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (canJump && !dazed) {
			if (IceAspirations.getMusic().isPlaying()
					|| IceAspirations.getCarrotMusic().isPlaying()) {
				jumpSound.play(0.1f, 0.8f, 0);
			}
			down = angle = 0;
			body.applyLinearImpulse(0, jump * myDelta, body.getWorldCenter().x,
					body.getWorldCenter().y, true);
			canJump = standing = false;
		}
		return false;
	}

	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
		case Keys.LEFT:
			force -= forceChange + slippery * 10;
			break;
		case Keys.RIGHT:
			force += forceChange + slippery * 10;
			break;
		case Keys.SPACE:
			if (canJump && !dazed) {
				if (IceAspirations.getMusic().isPlaying()
						|| IceAspirations.getCarrotMusic().isPlaying()) {
					jumpSound.play(0.1f, 0.8f, 0);
				}
				down = angle = 0;
				body.applyLinearImpulse(0, jump * myDelta,
						body.getWorldCenter().x, body.getWorldCenter().y, true);
				canJump = standing = false;
			}
		case Keys.BACK:
			// Do nothing
			return false;
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Keys.LEFT || keycode == Keys.RIGHT) {
			force = 0;
		}
		return false;
	}

	// UNUSED METHODS FROM INTERFACES
	@Override
	public void beginContact(Contact contact) {
		String fixA = (String) contact.getFixtureA().getUserData();
		String fixB = (String) contact.getFixtureB().getUserData();
		boolean playerContact = fixA.equals("player") || fixB.equals("player");
		boolean clockContact = fixA.equals("clock") || fixB.equals("clock");
		if (playerContact && clockContact) {
			if (IceAspirations.getMusic().isPlaying()
					|| IceAspirations.getCarrotMusic().isPlaying()) {
				getItemSound.play(0.1f, 0.8f, 0);
			}
			clockTouched = true;
		}
		boolean carrotContact = fixA.equals("carrot") || fixB.equals("carrot");
		if (playerContact && carrotContact) {
			if (IceAspirations.getMusic().isPlaying()
					|| IceAspirations.getCarrotMusic().isPlaying()) {
				getItemSound.play(0.1f, 0.8f, 0);
			}
			carrotTouched = true;
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
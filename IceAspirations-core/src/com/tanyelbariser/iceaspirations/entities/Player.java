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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.tanyelbariser.iceaspirations.IceAspirations;
import com.tanyelbariser.iceaspirations.platforms.Platforms;
import com.tanyelbariser.iceaspirations.screens.GameScreen;

public class Player implements ContactListener, InputProcessor {
	final float height = Gdx.graphics.getHeight();
	final float width = Gdx.graphics.getWidth();
	private BodyDef bodyDef;
	private FixtureDef fixDef;
	private Body body;
	private Sprite standSprite;
	private float angle;
	private float slippery;
	private final float jump = 300; // * 2 when eats carrot or * 1.2 if 2*delta
									// in hyper mode
	private float force;
	private final float forceChange = 5000;
	private boolean canJump;
	private float down;
	private float myDelta;
	private Animation jumpAnimation;
	private Sprite fallingSprite;
	private boolean standing;
	private Sound jumpSound = Gdx.audio.newSound(Gdx.files
			.internal("Jumping.wav"));
	private boolean facingLeft = false;

	public Player(World world) {
		world.setContactListener(this);

		bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(0, -height / GameScreen.ZOOM / 3);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(0.5f, 1.5f);

		fixDef = new FixtureDef();
		fixDef.shape = shape;
		fixDef.density = 3f;
		fixDef.friction = 0f;
		fixDef.restitution = 0f;

		body = world.createBody(bodyDef);
		body.createFixture(fixDef);

		shape.dispose();
		createAnimations();
		Gdx.input.setCatchBackKey(true);
	}

	public Body getBody() {
		return body;
	}

	public Animation getjumpAnimation() {
		return jumpAnimation;
	}

	public Sprite getStandSprite() {
		return standSprite;
	}

	public Sprite getFallingSprite() {
		return fallingSprite;
	}

	public boolean isStanding() {
		return standing;
	}

	public boolean getFacingLeft() {
		return facingLeft;
	}

	private void createAnimations() {
		Skin skin = IceAspirations.getSkin();
		standSprite = skin.getSprite("Rabbit1");
		standSprite.setSize(2.9f, 4.2f);
		standSprite.setOrigin(standSprite.getWidth() / 2,
				standSprite.getHeight() / 2);

		Sprite rabbit2 = skin.getSprite("Rabbit2");
		rabbit2.setSize(2.9f, 4.2f);
		rabbit2.setOrigin(rabbit2.getWidth() / 2, rabbit2.getHeight() / 2);

		Sprite rabbit3 = skin.getSprite("Rabbit3");
		rabbit3.setSize(2.3f, 4.2f);
		rabbit3.setOrigin(rabbit3.getWidth() / 2, rabbit3.getHeight() / 2);

		Sprite rabbit4 = skin.getSprite("Rabbit4");
		rabbit4.setSize(2.3f, 4.2f);
		rabbit4.setOrigin(rabbit4.getWidth() / 2, rabbit4.getHeight() / 2);

		Sprite rabbit5 = skin.getSprite("Rabbit5");
		rabbit5.setSize(2.3f, 4.2f);
		rabbit5.setOrigin(rabbit5.getWidth() / 2, rabbit5.getHeight() / 2);

		Sprite[] jumpSprites = new Sprite[] { standSprite, rabbit2, rabbit3,
				rabbit4, rabbit5 };

		jumpAnimation = new Animation(0.10f, jumpSprites);

		fallingSprite = skin.getSprite("Rabbit7");
		fallingSprite.setSize(3, 4.2f);
		fallingSprite.setOrigin(fallingSprite.getWidth() / 2,
				fallingSprite.getHeight() / 2);
	}

	public void update(float delta) {
		/*
		 * Using libgdx's Animation class if (body.getLinearVelocity().y > 0) {
		 * rising = true; // runs animation loop once from Rabbit1 to Rabbit4 }
		 * else { rising = false; // runs animation loop once from Rabbit5 to
		 * Rabbit7 } run animation loop from Rabbit8 to Rabbit1 once feetContact
		 * in postSolve(), which should override animation Rabbit 5 to 7 if true
		 * ALSO boolean facingLeft = Gdx.input.getAccelerometerX() > 0; if
		 * (facingLeft) { set PlayMode.REVERSED enum in Animation class;
		 */
		myDelta = delta;
		body.setTransform(body.getPosition(), angle);
		float accel = -Gdx.input.getAccelerometerX() * 2;
		body.setLinearVelocity((accel - slippery) * delta,
				body.getLinearVelocity().y + down * delta);
		body.applyForceToCenter(force * delta, down, true);
		if (accel < 0 || force < 0) {
			facingLeft = true;
		} else {
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
		if (touchLeftEdge || touchRightEdge) {
			canJump = false;
			down = 0;
		} else if (feetContact) {
			angle = contact.getFixtureB().getBody().getAngle();
			boolean onFloatingPlatform = (angle < 0.8f && angle > 0.07f)
					|| (angle < -0.07f && angle > -0.8f);
			if (onFloatingPlatform) {
				slippery = angle * 15;
				down = -1.5f;
				standing = true;
			} else {
				slippery = angle = 0;
			}
			canJump = true;
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
		if (canJump) {
			if (IceAspirations.getMusic().isPlaying()) {
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
			if (canJump) {
				if (IceAspirations.getMusic().isPlaying()) {
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
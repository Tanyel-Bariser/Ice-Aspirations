package com.tanyelbariser.iceaspirations.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
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
import com.tanyelbariser.iceaspirations.platforms.Platforms;
import com.tanyelbariser.iceaspirations.screens.GameScreen;

public class Player implements ContactListener, InputProcessor {
	final float height = Gdx.graphics.getHeight();
	final float width = Gdx.graphics.getWidth();
	private BodyDef bodyDef;
	private FixtureDef fixDef;
	public Body body;
	public Sprite stand;
	public Sprite jumping;
	private float angle;
	private float slippery;
	public final float jump = 300;
	private float force;
	private final float forceChange = 5000;
	private boolean canJump;
	private float down;
	private float myDelta;

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

		stand = IceAspirations.skin.getSprite("Rabbit1");
		stand.setSize(2.9f, 4.2f);
		stand.setOrigin(stand.getWidth() / 2, stand.getHeight() / 2);

		jumping = IceAspirations.skin.getSprite("Rabbit5");
		jumping.setSize(2.3f, 4.2f);
		jumping.setOrigin(jumping.getWidth() / 2, jumping.getHeight() / 2);
		// body.setUserData(playerSprite);
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
		body.setLinearVelocity((accel - slippery) * delta, body.getLinearVelocity().y + down * delta);
		body.applyForceToCenter(force  * delta, down, true);
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
		} else if (feetContact) {
			angle = contact.getFixtureB().getBody().getAngle();
			boolean onFloatingPlatform = (angle < 0.8f && angle > 0.07f)
					|| (angle < -0.07f && angle > -0.8f);
			if (onFloatingPlatform) {
				slippery = angle * 15;
				down = -1.5f;
			} else {
				slippery = angle = 0;
			}
			canJump = true;
		}
	}

	@Override
	public void endContact(Contact contact) {
		down = 0;
		//Delay in resetting slippery allows smoother sliding on platforms
		// & continue moving in the same direction after contact, i.e. inertia
		Timer.schedule(new Task() {
			@Override
			public void run() {
				slippery = 0;
				canJump = false;
			}
		}, 1);
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (canJump) {
			down = 0;
			body.applyLinearImpulse(0, jump * myDelta, body.getWorldCenter().x,
					body.getWorldCenter().y, true);
			canJump = false;
			angle = 0;
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
				body.applyLinearImpulse(0, jump * myDelta, body.getWorldCenter().x,
						body.getWorldCenter().y, true);
				canJump = false;
				angle = 0;
			}
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
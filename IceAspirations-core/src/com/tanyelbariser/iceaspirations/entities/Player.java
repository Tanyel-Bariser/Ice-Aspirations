package com.tanyelbariser.iceaspirations.entities;

import com.badlogic.gdx.Gdx;
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
import com.tanyelbariser.iceaspirations.screens.GameScreen;

public class Player implements ContactListener {
	final float height = Gdx.graphics.getHeight();
	final float width = Gdx.graphics.getWidth();
	private BodyDef bodyDef;
	private FixtureDef fixDef;
	public Body body;
	public Sprite stand;
	private float angle;
	private float slippery;
	public Sprite jump;
	public boolean standing = true;

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
		stand.setSize(2.1f, 4.2f);
		stand.setOrigin(stand.getWidth() / 2, stand.getHeight() / 2);

		jump = IceAspirations.skin.getSprite("Rabbit5");
		jump.setSize(2.1f, 4.2f);
		jump.setOrigin(stand.getWidth() / 2, stand.getHeight() / 2);
		// body.setUserData(playerSprite);
	}

	public void update(float delta) {
		/* if (body.getLinearVelocity().y > 0) {
		 * 		rising = true; // runs animation loop once from Rabbit1 to Rabbit4
		 * } else {
		 * 		rising = false; // runs animation loop once from Rabbit5 to Rabbit7
		 * }
		 * 		run animation loop from Rabbit8 to Rabbit1 once feetContact,
		 * 		i.e. standing = true; ... which should override any other sprite animation
		 */
		body.setTransform(body.getPosition(), angle);
		float accel = -Gdx.input.getAccelerometerX() * 2;
		body.setLinearVelocity(accel - slippery, body.getLinearVelocity().y);
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		boolean feetContact = contact.getWorldManifold().getPoints()[0].y < body
				.getPosition().y;
		boolean notRising = body.getLinearVelocity().y < 0;
		if (feetContact && notRising) {
			standing = true;
			angle = contact.getFixtureB().getBody().getAngle();
			boolean onFloatingPlatform = (angle < 0.8f && angle > 0.07f)
					|| (angle < -0.07f && angle > -0.8f);
			if (onFloatingPlatform) {
				slippery = angle * 10;
			} else {
				slippery = angle = 0;
			}
			float jumpPower = 200;
			if (Gdx.input.isTouched()) {
				body.applyLinearImpulse(0, jumpPower, body.getWorldCenter().x,
						body.getWorldCenter().y, true);
				standing = false;
			}
		}
	}

	@Override
	public void beginContact(Contact contact) {
	}

	@Override
	public void endContact(Contact contact) {
		Timer.schedule(new Task() {
			@Override
			public void run() {
				angle = slippery = 0;
				standing = false;
			}
		}, 1f);
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
	}
}
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
import com.tanyelbariser.iceaspirations.IceAspirations;
import com.tanyelbariser.iceaspirations.screens.GameScreen;

public class Player implements ContactListener {
	final float height = Gdx.graphics.getHeight();
	final float width = Gdx.graphics.getWidth();
	private BodyDef bodyDef;
	private FixtureDef fixDef;
	public Body body;
	public Sprite playerSprite;
	private float timeSinceJump;
	private float angle;

	public Player(World world) {
		world.setContactListener(this);
		
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(0, -height / GameScreen.ZOOM / 3);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(0.75f, 1.5f);

		fixDef = new FixtureDef();
		fixDef.shape = shape;
		fixDef.density = 3f;
		fixDef.friction = 0f;
		fixDef.restitution = 0f;

		body = world.createBody(bodyDef);
		body.createFixture(fixDef);
		
		shape.dispose();

		playerSprite = IceAspirations.skin.getSprite("Rabbit1");
		playerSprite.setSize(2.1f, 4.2f);
		playerSprite.setOrigin(playerSprite.getWidth() / 2,
				playerSprite.getHeight() / 2);
		body.setUserData(playerSprite);
	}

	public void update(float delta) {
		body.setTransform(body.getPosition(), angle);
		float accel = -Gdx.input.getAccelerometerX() * 2;
		float y = body.getLinearVelocity().y;
		body.setLinearVelocity(accel, y);
		timeSinceJump += delta;
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		if(contact.getWorldManifold().getPoints()[0].y < body.getPosition().y) {
			angle = contact.getFixtureB().getBody().getAngle();
			if (timeSinceJump > 0.5f) {
				float jumpPower = 200;
				if (Gdx.input.isTouched()) {
					body.applyLinearImpulse(0, jumpPower, body.getWorldCenter().x,
							body.getWorldCenter().y, true);
					timeSinceJump = 0;
				}
			}
		}
		Gdx.app.log("ANGLE", String.valueOf(angle));
	}

	@Override
	public void beginContact(Contact contact) {}
	@Override
	public void endContact(Contact contact) {}
	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {}
}
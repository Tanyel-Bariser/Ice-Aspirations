package com.tanyelbariser.iceaspirations.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.tanyelbariser.iceaspirations.CollisionDetection;
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

		shape.dispose();
		Gdx.input.setCatchBackKey(true);
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
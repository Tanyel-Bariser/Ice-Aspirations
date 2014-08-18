package com.tanyelbariser.iceaspirations.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.tanyelbariser.iceaspirations.screens.GameScreen;

public class Player {
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
	private boolean standing;
	private boolean canJump;
	private boolean facingLeft = false;
	private boolean clockTouched = false;
	private boolean dazed = false;
	private boolean carrotTouched = false;

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

	
	
	// Getters and Setters
	public Body getBody() {
		return body;
	}
	public void jump() {
		body.applyLinearImpulse(0, jump * myDelta,
				body.getWorldCenter().x, body.getWorldCenter().y, true);
	}
	public float getJump() {
		return jump;
	}
	public float getForce() {
		return force;
	}
	public void setForce(float force) {
		this.force = force;
	}
	public void leftForce() {
		force -= forceChange + slippery * 10;
	}
	public void rightForce() {
		force += forceChange + slippery * 10;
	}
	public float getPlayerDelta() {
		return myDelta;
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
	public boolean isCarrotTouched() {
		return carrotTouched;
	}
	public void setCarrotTouched(boolean carrotTouched) {
		this.carrotTouched = carrotTouched;
	}
	public void setDown(float down) {
		this.down = down;
	}
	public void setCanJump(boolean canJump) {
		this.canJump = canJump;
	}
	public boolean canJump() {
		return canJump;
	}
	public void setAngle(float angle) {
		this.angle = angle;
	}
	public void setSlippery(float slippery) {
		this.slippery = slippery;
	}
	public void setStanding(boolean standing) {
		this.standing = standing;
	}
}
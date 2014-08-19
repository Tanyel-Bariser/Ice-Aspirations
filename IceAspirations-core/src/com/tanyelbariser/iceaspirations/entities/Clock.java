package com.tanyelbariser.iceaspirations.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.tanyelbariser.iceaspirations.CollisionDetection;
import com.tanyelbariser.iceaspirations.screens.GameScreen;

public class Clock {
	private Body clock;
	private float heightLastClock = 0;
	private float distanceBetweenClocks = 100;

	public Clock(World world) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(0, -50);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(1, 1);

		FixtureDef fixDef = new FixtureDef();
		fixDef.shape = shape;
		fixDef.isSensor = true;

		clock = world.createBody(bodyDef);
		clock.createFixture(fixDef).setUserData("clock");

		shape.dispose();
	}

	// Reposition clock after being touched
	public Sprite repositionClock(Sprite clockSprite,
			OrthographicCamera camera, CollisionDetection contact,
			float topScreenEdge, Array<Body> platformArray,
			GameScreen gameScreen) {
		if (!contact.isClockTouched()
				&& camera.position.y > heightLastClock + distanceBetweenClocks) {
			for (Body platform : platformArray) {
				if (platform.getPosition().y > topScreenEdge) {
					clock.setTransform(platform.getPosition().x,
							platform.getPosition().y + 2.5f,
							platform.getAngle());
					break;
				}
			}
			heightLastClock = camera.position.y;
			distanceBetweenClocks += 20;
		}
		if (contact.isClockTouched()) {
			gameScreen.increaseAllotedTime(10);
			clock.setTransform(-50, 0, 0);
			contact.setClockTouched(false);
		}
		clockSprite.setPosition(clock.getPosition().x - clockSprite.getWidth()
				/ 2, clock.getPosition().y - clockSprite.getHeight() / 2);
		clockSprite.setRotation(clock.getAngle() * MathUtils.radiansToDegrees);

		return clockSprite;
	}
}

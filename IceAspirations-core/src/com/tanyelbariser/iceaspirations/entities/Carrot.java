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

public class Carrot {
	private Body carrot;
	private float heightLastCarrot = 0;
	private float distanceBetweenCarrots = 155;
	private float timeSinceCarrotTouched = 0;

	public Carrot(World world) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(0, -50);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(1, 1);

		FixtureDef fixDef = new FixtureDef();
		fixDef.shape = shape;
		fixDef.isSensor = true;

		carrot = world.createBody(bodyDef);
		carrot.createFixture(fixDef).setUserData("carrot");

		shape.dispose();
	}

	// Reposition carrot after being touched
	public Sprite repositionCarrot(Sprite carrotSprite,
			OrthographicCamera camera, CollisionDetection contact,
			float topScreenEdge, float delta, Array<Body> platformArray) {
		if (contact.isCarrotTouched()) {
			carrot.setTransform(-50, 0, 0);
			timeSinceCarrotTouched += delta;
		}
		if (timeSinceCarrotTouched > 7.5f) {
			timeSinceCarrotTouched = 0;
			contact.setCarrotTouched(false);
		}
		if (!contact.isCarrotTouched()
				&& camera.position.y > heightLastCarrot
						+ distanceBetweenCarrots) {
			for (Body platform : platformArray) {
				if (platform.getPosition().y > topScreenEdge) {
					carrot.setTransform(platform.getPosition().x,
							platform.getPosition().y + 2.5f,
							platform.getAngle());
					break;
				}
			}
			heightLastCarrot = camera.position.y;
			distanceBetweenCarrots += 40;
		}
		carrotSprite.setPosition(
				carrot.getPosition().x - carrotSprite.getWidth() / 2,
				carrot.getPosition().y - carrotSprite.getHeight() / 2);
		carrotSprite
				.setRotation(carrot.getAngle() * MathUtils.radiansToDegrees);
		return carrotSprite;
	}
}
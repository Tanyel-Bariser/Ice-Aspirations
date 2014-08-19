package com.tanyelbariser.iceaspirations.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Boulder {
	private Body boulder;
	private float distanceBetweenBoulders = 200;
	private float heightLastBoulder = 0;

	public Boulder(World world) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(0, -50);

		CircleShape shape = new CircleShape();
		shape.setRadius(2.5f);

		FixtureDef fixDef = new FixtureDef();
		fixDef.shape = shape;
		fixDef.density = 3f;
		fixDef.friction = 1f;
		fixDef.restitution = 0f;

		boulder = world.createBody(bodyDef);
		boulder.createFixture(fixDef).setUserData("boulder");

		shape.dispose();
	}
	
	public Sprite repositionBoulder(Sprite boulderSprite,
			OrthographicCamera camera, float topScreenEdge, float bottomScreenEdge,
			float gravity, float playerX) {
		if (boulder.getPosition().y < bottomScreenEdge - 10
				&& camera.position.y > heightLastBoulder
						+ distanceBetweenBoulders) {
			boulder.setTransform(playerX,
					topScreenEdge + 10, 0);
			heightLastBoulder = camera.position.y;
			if (distanceBetweenBoulders > 20) {
				distanceBetweenBoulders -= 10;
			}
		}
		if (boulder.getLinearVelocity().y > -1) {
			boulder.setLinearVelocity(0, gravity);
		}
		boulderSprite.setPosition(
				boulder.getPosition().x - boulderSprite.getWidth() / 2,
				boulder.getPosition().y - boulderSprite.getHeight() / 2);
		boulderSprite.setRotation(boulder.getAngle()
				* MathUtils.radiansToDegrees);
		return boulderSprite;
	}
}
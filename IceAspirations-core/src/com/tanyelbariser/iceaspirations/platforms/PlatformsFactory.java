package com.tanyelbariser.iceaspirations.platforms;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.tanyelbariser.iceaspirations.IceAspirations;

public class PlatformsFactory {
	
	public static ArrayList<Body> createPlatforms(World world) {
		
		ArrayList<Body> platforms = new ArrayList<Body>();
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(0, Platforms.BOTTOM_SCREEN_EDGE+3);

		float angle = MathUtils.random(-45 * MathUtils.degreesToRadians,
				45 * MathUtils.degreesToRadians);
		bodyDef.angle = angle;
		
		Body platform = world.createBody(bodyDef);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(4 / 2, 2 / 2);
		
		platform.createFixture(shape, 0);

		shape.dispose();
		
		Sprite sprite = IceAspirations.skin.getSprite("Platform4");

		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
		sprite.setPosition(platform.getPosition().x - sprite.getWidth()
				/ 2, platform.getPosition().y - sprite.getHeight() / 2);
		sprite.setRotation(platform.getAngle()
				* MathUtils.radiansToDegrees);

		platforms.add(platform);
		
		return platforms;
	}

}

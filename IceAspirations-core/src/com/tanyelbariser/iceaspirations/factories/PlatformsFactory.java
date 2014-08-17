package com.tanyelbariser.iceaspirations.factories;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.tanyelbariser.iceaspirations.platforms.Platforms;

public class PlatformsFactory {

	public static Array<Body> createPlatforms(World world) {
		Array<Body> platforms = new Array<Body>();
		platforms.add(createPlatform(world, "Platform1", 4, 2, 2, 0.91f));
		platforms.add(createPlatform(world, "Platform2", 4, 3, 1.6f, 1.2f));
		platforms.add(createPlatform(world, "Platform3", 8, 1.8f, 4, 0.72f));
		platforms.add(createPlatform(world, "Platform4", 4, 2, 2, 0.91f));
		platforms.add(createPlatform(world, "Platform5", 6, 3, 2.4f, 1.2f));
		platforms.add(createPlatform(world, "Platform6", 4, 2, 2, 0.91f));
		platforms.add(createPlatform(world, "Platform7", 4, 2, 2, 0.91f));
		platforms.add(createPlatform(world, "Platform8", 3, 3, 1.2f, 1.2f));
		platforms.add(createPlatform(world, "Platform9", 4, 2, 2, 0.91f));
		return platforms;
	}

	private static Body createPlatform(World world, String name, float width,
			float height, float boxWidth, float boxHeight) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(5, Platforms.BOTTOM_SCREEN_EDGE - 30);
		Body platform = world.createBody(bodyDef);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(boxWidth, boxHeight);
		platform.createFixture(shape, 0).setUserData("platform");
		shape.dispose();

		Sprite sprite = SpriteFactory.createPlatformSprite(name, width, height);

		platform.setUserData(sprite);
		return platform;
	}
}
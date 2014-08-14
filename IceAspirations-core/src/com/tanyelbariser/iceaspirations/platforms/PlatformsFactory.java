package com.tanyelbariser.iceaspirations.platforms;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.tanyelbariser.iceaspirations.IceAspirations;

public class PlatformsFactory {
	
	public static Array<Body> createPlatforms(World world) {
		Array<Body> platforms = new Array<Body>();
		platforms.add(createPlatform(world, "Platform3", 4, 2, 2, 0.91f));
		platforms.add(createPlatform(world, "Platform7", 4, 3, 1.6f, 1.2f));
		platforms.add(createPlatform(world, "Platform5", 8, 1.8f, 4, 0.72f));
		platforms.add(createPlatform(world, "Platform6", 4, 2, 2, 0.91f));
		platforms.add(createPlatform(world, "Platform8", 6, 3, 2.4f, 1.2f));
		platforms.add(createPlatform(world, "Platform1", 4, 2, 2, 0.91f));
		platforms.add(createPlatform(world, "Platform4", 4, 2, 2, 0.91f));
		platforms.add(createPlatform(world, "Platform9", 3, 3, 1.2f, 1.2f));
		platforms.add(createPlatform(world, "Platform2", 4, 2, 2, 0.91f));
		return platforms;
	}

	private static Body createPlatform(World world, String platformNum,
			float width, float height, float boxWidth, float boxHeight) {
		Skin skin = IceAspirations.getSkin();
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(5, Platforms.BOTTOM_SCREEN_EDGE - 15);
		Body platform = world.createBody(bodyDef);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(boxWidth, boxHeight);
		platform.createFixture(shape, 0).setUserData("platform");
		shape.dispose();

		Sprite sprite = skin.getSprite(platformNum);
		sprite.setSize(width, height);

		platform.setUserData(sprite);
		return platform;
	}
}
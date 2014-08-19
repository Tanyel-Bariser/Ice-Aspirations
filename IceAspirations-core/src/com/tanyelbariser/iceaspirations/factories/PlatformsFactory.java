package com.tanyelbariser.iceaspirations.factories;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.tanyelbariser.iceaspirations.platforms.PlatformManager;
import com.tanyelbariser.iceaspirations.screens.GameScreen;

public class PlatformsFactory {
	private static Array<Body> platforms = new Array<Body>();
	public static final float LEFT_SCREEN_EDGE = -GameScreen.WIDTH
			/ GameScreen.ZOOM / 2;
	public static final float RIGHT_SCREEN_EDGE = GameScreen.WIDTH
			/ GameScreen.ZOOM / 2;
	public static final float BOTTOM_SCREEN_EDGE = -GameScreen.HEIGHT
			/ GameScreen.ZOOM / 2;

	public static void createGroundWalls(World world) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(0, 0);

		ChainShape worldContainerShape = new ChainShape();

		Vector2 topLeft = new Vector2(LEFT_SCREEN_EDGE, 5000);
		Vector2 bottomLeft = new Vector2(LEFT_SCREEN_EDGE, BOTTOM_SCREEN_EDGE);
		Vector2 bottomRight = new Vector2(RIGHT_SCREEN_EDGE, BOTTOM_SCREEN_EDGE);
		Vector2 topRight = new Vector2(RIGHT_SCREEN_EDGE, 5000);

		worldContainerShape.createChain(new Vector2[] { topLeft, bottomLeft,
				bottomRight, topRight });

		FixtureDef fixDef = new FixtureDef();
		fixDef.shape = worldContainerShape;
		fixDef.friction = 0f;
		fixDef.restitution = 0;

		Body worldContainer = world.createBody(bodyDef);
		worldContainer.createFixture(fixDef).setUserData("ground");

		worldContainerShape.dispose();
	}

	public static Array<Body> createPlatforms(World world) {
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
		bodyDef.position.set(5, PlatformManager.BOTTOM_SCREEN_EDGE - 30);
		Body platform = world.createBody(bodyDef);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(boxWidth, boxHeight);
		platform.createFixture(shape, 0).setUserData("platform");
		shape.dispose();

		Sprite sprite = SpriteFactory.createPlatformSprite(name, width, height);

		platform.setUserData(sprite);
		return platform;
	}

	public static Array<Sprite> initiseSprites() {
		Array<Sprite> platformSprites = new Array<Sprite>();
		for (Body platform : platforms) {
			Sprite sprite = (Sprite) platform.getUserData();
			sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
			sprite.setPosition(
					platform.getPosition().x - sprite.getWidth() / 2,
					platform.getPosition().y - sprite.getHeight() / 2);
			sprite.setRotation(platform.getAngle() * MathUtils.radiansToDegrees);
			platformSprites.add(sprite);
		}
		return platformSprites;
	}
}
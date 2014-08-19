package com.tanyelbariser.iceaspirations.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import com.tanyelbariser.iceaspirations.factories.SpriteFactory;
import com.tanyelbariser.iceaspirations.screens.GameScreen;

public class Platforms {
	private World world;
	private Array<Body> platforms = new Array<Body>();
	public static final float LEFT_SCREEN_EDGE = -GameScreen.WIDTH
			/ GameScreen.ZOOM / 2;
	public static final float RIGHT_SCREEN_EDGE = GameScreen.WIDTH
			/ GameScreen.ZOOM / 2;
	public static final float BOTTOM_SCREEN_EDGE = -GameScreen.HEIGHT
			/ GameScreen.ZOOM / 2;
	private float topPlatformY = BOTTOM_SCREEN_EDGE / 3;
	private boolean placeLeft = true;
	private float bottomPlatformY;
	private final static float DISTANCE_BETWEEN_PLATFORMS = 10;
	
	public Platforms(World world) {
		this.world = world;
	}
	
	public void createGroundWalls() {
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

	public Array<Body> createPlatforms() {
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

	private Body createPlatform(World world, String name, float width,
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

	public Array<Sprite> initiseSprites() {
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

	// Repositions platform if out of camera/screen view
	public void repositionPlatforms(float topScreenEdge, float bottomScreenEdge, Array<Body> platformArray) {
		for (Body platform : platformArray) {
			if (platform.getPosition().y < bottomScreenEdge - 25) {
				repositionAbove(platform, topScreenEdge);
			} else if (platform.getPosition().y > topScreenEdge + 25) {
				repositionBelow(platform, bottomScreenEdge);
			}
		}
	}

	private void repositionAbove(Body platform, float topScreenEdge) {
		if (topPlatformY < topScreenEdge + 25) {
			bottomPlatformY = platform.getPosition().y;
			repositionPlatform(platform, topPlatformY);
			topPlatformY += DISTANCE_BETWEEN_PLATFORMS;
		}
	}

	private void repositionBelow(Body platform, float bottomScreenEdge) {
		if (bottomPlatformY > bottomScreenEdge - 25
				&& bottomPlatformY > BOTTOM_SCREEN_EDGE / 3) {
			topPlatformY = platform.getPosition().y;
			repositionPlatform(platform, bottomPlatformY);
			bottomPlatformY -= DISTANCE_BETWEEN_PLATFORMS;
		}
	}

	private void repositionPlatform(Body platform, float positionY) {
		float platformX;
		Sprite sprite = (Sprite) platform.getUserData();
		float width = sprite.getWidth();
		if (placeLeft) {
			platformX = MathUtils.random(LEFT_SCREEN_EDGE + width / 2,
					0 - width / 4);
		} else {
			platformX = MathUtils.random(0 + width / 4, RIGHT_SCREEN_EDGE
					- width / 2);
		}
		placeLeft = !placeLeft;

		float angle = MathUtils.random(-45 * MathUtils.degreesToRadians,
				45 * MathUtils.degreesToRadians);
		platform.setTransform(platformX, positionY, angle);

		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
		sprite.setPosition(platform.getPosition().x - sprite.getWidth() / 2,
				platform.getPosition().y - sprite.getHeight() / 2);
		sprite.setRotation(platform.getAngle() * MathUtils.radiansToDegrees);
	}
}
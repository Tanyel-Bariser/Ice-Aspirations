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
	private SpriteFactory spriteFactory;
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
	private final static int HEIGHT_ABOVE_OR_BELOW_SCREEN = 25;
	private final float HALF = 0.5f;
	private final float QUARTER = 0.25f;

	public Platforms(World world) {
		this.world = world;
	}

	public void createGroundWalls() {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(0, 0);

		ChainShape worldContainerShape = new ChainShape();

		final int WALL_HEIGHT = 5000;
		Vector2 topLeft = new Vector2(LEFT_SCREEN_EDGE, WALL_HEIGHT);
		Vector2 bottomLeft = new Vector2(LEFT_SCREEN_EDGE, BOTTOM_SCREEN_EDGE);
		Vector2 bottomRight = new Vector2(RIGHT_SCREEN_EDGE, BOTTOM_SCREEN_EDGE);
		Vector2 topRight = new Vector2(RIGHT_SCREEN_EDGE, WALL_HEIGHT);

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
		spriteFactory = new SpriteFactory();
		float p1Width = 4, p2Width = 4, p3Width = 8, p4Width = 4, p5Width = 6, p6Width = 4, p7Width = 4, p8Width = 3, p9Width = 4;
		float p1Height = 2, p2Height = 3, p3Height = 1.8f, p4Height = 2, p5Height = 3, p6Height = 2, p7Height = 2, p8Height = 3, p9Height = 2;
		float p1BoxWidth = 2, p2BoxWidth = 1.6f, p3BoxWidth = 4, p4BoxWidth = 2, p5BoxWidth = 2.4f, p6BoxWidth = 2, p7BoxWidth = 2, p8BoxWidth = 1.2f, p9BoxWidth = 2;
		float p1BoxHeight = 0.91f, p2BoxHeight = 1.2f, p3BoxHeight = 0.72f, p4BoxHeight = 0.91f, p5BoxHeight = 1.2f, p6BoxHeight = 0.91f, p7BoxHeight = 0.91f, p8BoxHeight = 1.2f, p9BoxHeight = 0.91f;

		platforms.add(createPlatform(world, "Platform1", p1Width, p1Height,
				p1BoxWidth, p1BoxHeight));
		platforms.add(createPlatform(world, "Platform2", p2Width, p2Height,
				p2BoxWidth, p2BoxHeight));
		platforms.add(createPlatform(world, "Platform3", p3Width, p3Height,
				p3BoxWidth, p3BoxHeight));
		platforms.add(createPlatform(world, "Platform4", p4Width, p4Height,
				p4BoxWidth, p4BoxHeight));
		platforms.add(createPlatform(world, "Platform5", p5Width, p5Height,
				p5BoxWidth, p5BoxHeight));
		platforms.add(createPlatform(world, "Platform6", p6Width, p6Height,
				p6BoxWidth, p6BoxHeight));
		platforms.add(createPlatform(world, "Platform7", p7Width, p7Height,
				p7BoxWidth, p7BoxHeight));
		platforms.add(createPlatform(world, "Platform8", p8Width, p8Height,
				p8BoxWidth, p8BoxHeight));
		platforms.add(createPlatform(world, "Platform9", p9Width, p9Height,
				p9BoxWidth, p9BoxHeight));
		return platforms;
	}

	private Body createPlatform(World world, String name, float width,
			float height, float boxWidth, float boxHeight) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		float positionX = 5, belowScreen = 30;
		bodyDef.position.set(positionX, Platforms.BOTTOM_SCREEN_EDGE
				- belowScreen);
		Body platform = world.createBody(bodyDef);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(boxWidth, boxHeight);
		platform.createFixture(shape, 0).setUserData("platform");
		shape.dispose();

		Sprite sprite = spriteFactory.createPlatformSprite(name, width, height);

		platform.setUserData(sprite);
		return platform;
	}

	public Array<Sprite> initiseSprites() {
		Array<Sprite> platformSprites = new Array<Sprite>();
		for (Body platform : platforms) {
			Sprite sprite = (Sprite) platform.getUserData();
			sprite.setOrigin(sprite.getWidth() * HALF, sprite.getHeight()
					* HALF);
			sprite.setPosition(platform.getPosition().x - sprite.getWidth()
					* HALF, platform.getPosition().y - sprite.getHeight()
					* HALF);
			sprite.setRotation(platform.getAngle() * MathUtils.radiansToDegrees);
			platformSprites.add(sprite);
		}
		return platformSprites;
	}

	// Repositions platform if out of camera/screen view
	public void repositionPlatforms(float topScreenEdge,
			float bottomScreenEdge, Array<Body> platformArray) {
		for (Body platform : platformArray) {
			if (platform.getPosition().y < bottomScreenEdge
					- HEIGHT_ABOVE_OR_BELOW_SCREEN) {
				repositionAbove(platform, topScreenEdge);
			} else if (platform.getPosition().y > topScreenEdge
					+ HEIGHT_ABOVE_OR_BELOW_SCREEN) {
				repositionBelow(platform, bottomScreenEdge);
			}
		}
	}

	private void repositionAbove(Body platform, float topScreenEdge) {
		if (topPlatformY < topScreenEdge + HEIGHT_ABOVE_OR_BELOW_SCREEN) {
			bottomPlatformY = platform.getPosition().y;
			repositionPlatform(platform, topPlatformY);
			topPlatformY += DISTANCE_BETWEEN_PLATFORMS;
		}
	}

	private void repositionBelow(Body platform, float bottomScreenEdge) {
		final float third = 0.33f;
		if (bottomPlatformY > bottomScreenEdge - HEIGHT_ABOVE_OR_BELOW_SCREEN
				&& bottomPlatformY > BOTTOM_SCREEN_EDGE * third) {
			topPlatformY = platform.getPosition().y;
			repositionPlatform(platform, bottomPlatformY);
			bottomPlatformY -= DISTANCE_BETWEEN_PLATFORMS;
		}
	}

	private void repositionPlatform(Body platform, float positionY) {
		float platformX;
		Sprite sprite = (Sprite) platform.getUserData();
		float width = sprite.getWidth();
		final float middleScreen = 0;
		if (placeLeft) {
			platformX = MathUtils.random(LEFT_SCREEN_EDGE + width * HALF,
					middleScreen - width * QUARTER);
		} else {
			platformX = MathUtils.random(middleScreen + width * QUARTER,
					RIGHT_SCREEN_EDGE - width * HALF);
		}
		placeLeft = !placeLeft;

		float negativeAngle = -45, positiveAngle = 45;
		float angle = MathUtils.random(negativeAngle
				* MathUtils.degreesToRadians, positiveAngle
				* MathUtils.degreesToRadians);
		platform.setTransform(platformX, positionY, angle);

		sprite.setOrigin(sprite.getWidth() * HALF, sprite.getHeight() * HALF);
		sprite.setPosition(platform.getPosition().x - sprite.getWidth() * HALF,
				platform.getPosition().y - sprite.getHeight() * HALF);
		sprite.setRotation(platform.getAngle() * MathUtils.radiansToDegrees);
	}
}
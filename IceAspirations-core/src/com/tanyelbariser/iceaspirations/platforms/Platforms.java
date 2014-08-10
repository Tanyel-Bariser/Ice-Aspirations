package com.tanyelbariser.iceaspirations.platforms;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.tanyelbariser.iceaspirations.screens.GameScreen;

public class Platforms {
	private float topPlatformY;
	public static final float LEFT_SCREEN_EDGE = -GameScreen.WIDTH
			/ GameScreen.ZOOM / 2;
	public static final float RIGHT_SCREEN_EDGE = GameScreen.WIDTH
			/ GameScreen.ZOOM / 2;
	public static final float BOTTOM_SCREEN_EDGE = -GameScreen.HEIGHT
			/ GameScreen.ZOOM / 2;
	private boolean placeLeft = true;
	private float bottomPlatformY;
	private final float DISTANCE_BETWEEN_PLATFORMS = 10;

	public Platforms(World world) {
		topPlatformY = BOTTOM_SCREEN_EDGE / 3;

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
		worldContainer.createFixture(fixDef);

		worldContainerShape.dispose();
	}

	public void repositionAbove(Body platform, float topScreenEdge) {
		if (topPlatformY < topScreenEdge + 14) {
			bottomPlatformY = platform.getPosition().y;
			repositionPlatform(platform, topPlatformY);
			topPlatformY += DISTANCE_BETWEEN_PLATFORMS;
		}
	}

	public void repositionBelow(Body platform, float bottomScreenEdge) {
		if (bottomPlatformY > bottomScreenEdge - 14 && bottomPlatformY > BOTTOM_SCREEN_EDGE / 3) {
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
			platformX = MathUtils.random(LEFT_SCREEN_EDGE + width/2,
					0 - width/4);
		} else {
			platformX = MathUtils.random(0 + width/4,
					RIGHT_SCREEN_EDGE - width/2);
		}
		placeLeft = !placeLeft;

		float angle = MathUtils.random(-45 * MathUtils.degreesToRadians,
				45 * MathUtils.degreesToRadians);
		platform.setTransform(platformX, positionY, angle);

		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
		sprite.setPosition(
				platform.getPosition().x - sprite.getWidth() / 2,
				platform.getPosition().y - sprite.getHeight() / 2);
		sprite.setRotation(platform.getAngle() * MathUtils.radiansToDegrees);
	}
}
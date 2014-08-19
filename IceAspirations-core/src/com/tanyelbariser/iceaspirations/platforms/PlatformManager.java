package com.tanyelbariser.iceaspirations.platforms;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.tanyelbariser.iceaspirations.screens.GameScreen;

public class PlatformManager {
	public static final float LEFT_SCREEN_EDGE = -GameScreen.WIDTH
			/ GameScreen.ZOOM / 2;
	public static final float RIGHT_SCREEN_EDGE = GameScreen.WIDTH
			/ GameScreen.ZOOM / 2;
	public static final float BOTTOM_SCREEN_EDGE = -GameScreen.HEIGHT
			/ GameScreen.ZOOM / 2;
	private static float topPlatformY = BOTTOM_SCREEN_EDGE / 3;
	private static boolean placeLeft = true;
	private static float bottomPlatformY;
	private final static float DISTANCE_BETWEEN_PLATFORMS = 10;

	// Repositions platform if out of camera/screen view
	public static void repositionPlatforms(float topScreenEdge, float bottomScreenEdge, Array<Body> platformArray) {
		for (Body platform : platformArray) {
			if (platform.getPosition().y < bottomScreenEdge - 25) {
				repositionAbove(platform, topScreenEdge);
			} else if (platform.getPosition().y > topScreenEdge + 25) {
				repositionBelow(platform, bottomScreenEdge);
			}
		}
	}

	private static void repositionAbove(Body platform, float topScreenEdge) {
		if (topPlatformY < topScreenEdge + 25) {
			bottomPlatformY = platform.getPosition().y;
			repositionPlatform(platform, topPlatformY);
			topPlatformY += DISTANCE_BETWEEN_PLATFORMS;
		}
	}

	private static void repositionBelow(Body platform, float bottomScreenEdge) {
		if (bottomPlatformY > bottomScreenEdge - 25
				&& bottomPlatformY > BOTTOM_SCREEN_EDGE / 3) {
			topPlatformY = platform.getPosition().y;
			repositionPlatform(platform, bottomPlatformY);
			bottomPlatformY -= DISTANCE_BETWEEN_PLATFORMS;
		}
	}

	private static void repositionPlatform(Body platform, float positionY) {
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
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
	public static final float LEFT_SCREEN_EDGE = -GameScreen.width
			/ GameScreen.ZOOM / 2;
	public static final float RIGHT_SCREEN_EDGE = GameScreen.width
			/ GameScreen.ZOOM / 2;
	public static final float BOTTOM_SCREEN_EDGE = -GameScreen.height
			/ GameScreen.ZOOM / 2;
	private boolean placeLeft = true;
	float widestPlatform = 8;
	private float bottomPlatformY;

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
		if (topPlatformY < topScreenEdge + 2) {
			bottomPlatformY = platform.getPosition().y;
			repositionPlatform(platform, topPlatformY);
			topPlatformY += MathUtils.random(5, 8);
		}
	}

	public void repositionBelow(Body platform, float bottomScreenEdge) {
		if (bottomPlatformY > bottomScreenEdge - 2) {
			topPlatformY = platform.getPosition().y;
			repositionPlatform(platform, bottomPlatformY);
			bottomPlatformY -= MathUtils.random(5, 8);
		}
	}
	
	private void repositionPlatform(Body platform, float positionY) {
		float platformX;
		if (placeLeft) {
			platformX = MathUtils.random(LEFT_SCREEN_EDGE + widestPlatform/2,
					0 - widestPlatform/2);
		} else {
			platformX = MathUtils.random(0 + widestPlatform/2,
					RIGHT_SCREEN_EDGE - widestPlatform/2);
		}
		placeLeft = !placeLeft;

		float angle = MathUtils.random(-45 * MathUtils.degreesToRadians,
				45 * MathUtils.degreesToRadians);
		platform.setTransform(platformX, positionY, angle);

		Sprite sprite = (Sprite) platform.getUserData();
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
		sprite.setPosition(
				platform.getPosition().x - sprite.getWidth() / 2,
				platform.getPosition().y - sprite.getHeight() / 2);
		sprite.setRotation(platform.getAngle() * MathUtils.radiansToDegrees);
	}
}
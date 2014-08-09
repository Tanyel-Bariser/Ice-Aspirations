package com.tanyelbariser.iceaspirations.platforms;

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
import com.tanyelbariser.iceaspirations.screens.GameScreen;

public class Platforms {
	private BodyDef bodyDef;
	private FixtureDef fixDef;
	private float platformY;
	private Body worldContainer;
	public static final float LEFT_SCREEN_EDGE = -GameScreen.width
			/ GameScreen.ZOOM / 2;
	public static final float RIGHT_SCREEN_EDGE = GameScreen.width
			/ GameScreen.ZOOM / 2;
	public static final float BOTTOM_SCREEN_EDGE = -GameScreen.height
			/ GameScreen.ZOOM / 2;
	private boolean placeLeft = true;
	World world;

	public Platforms(World world) {
		this.world = world;
		platformY = BOTTOM_SCREEN_EDGE / 3;

		bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(0, 0);

		ChainShape worldContainerShape = new ChainShape();

		Vector2 topLeft = new Vector2(LEFT_SCREEN_EDGE, 5000);
		Vector2 bottomLeft = new Vector2(LEFT_SCREEN_EDGE, BOTTOM_SCREEN_EDGE);
		Vector2 bottomRight = new Vector2(RIGHT_SCREEN_EDGE, BOTTOM_SCREEN_EDGE);
		Vector2 topRight = new Vector2(RIGHT_SCREEN_EDGE, 5000);

		worldContainerShape.createChain(new Vector2[] { topLeft, bottomLeft,
				bottomRight, topRight });

		fixDef = new FixtureDef();
		fixDef.shape = worldContainerShape;
		fixDef.friction = 0f;
		fixDef.restitution = 0;

		worldContainer = world.createBody(bodyDef);
		worldContainer.createFixture(fixDef);

		worldContainerShape.dispose();
	}

	public void repositionAbove(Body platform, float topScreenEdge) {
		if (platformY < topScreenEdge + 20) {
			float widestPlatform = 8;
			float platformX;
			if (placeLeft) {
				platformX = MathUtils.random(LEFT_SCREEN_EDGE + widestPlatform/2,
						0);
			} else {
				platformX = MathUtils.random(0,
						RIGHT_SCREEN_EDGE - widestPlatform/2);
			}
			placeLeft = !placeLeft;

			float angle = MathUtils.random(-45 * MathUtils.degreesToRadians,
					45 * MathUtils.degreesToRadians);
			platform.setTransform(platformX, platformY, angle);

			Sprite sprite = (Sprite) platform.getUserData();
			sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
			sprite.setPosition(
					platform.getPosition().x - sprite.getWidth() / 2,
					platform.getPosition().y - sprite.getHeight() / 2);
			sprite.setRotation(platform.getAngle() * MathUtils.radiansToDegrees);

			platformY += MathUtils.random(5, 8);
		}
	}

	public void repositionBelow(Body platform, float bottomScreenEdge) {

	}
}
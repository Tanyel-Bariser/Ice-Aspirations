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
	public static final float LEFT_SCREEN_EDGE = -GameScreen.width / GameScreen.ZOOM / 2;
	public static final float RIGHT_SCREEN_EDGE = GameScreen.width / GameScreen.ZOOM / 2;
	public static final float BOTTOM_SCREEN_EDGE = -GameScreen.height / GameScreen.ZOOM / 2;
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

	public void createPlatforms(float topEdge, Sprite platformSprite) {
		if (platformY < topEdge + 2) {
			float width = 3, height = 1;			
			float platformX = 0;
			if (placeLeft) {
				platformX  = MathUtils.random(LEFT_SCREEN_EDGE + width, 0 - width);
			} else {
				platformX = MathUtils.random(0 + width, RIGHT_SCREEN_EDGE - width);
			}
			placeLeft = !placeLeft;
			
			bodyDef = new BodyDef();
			bodyDef.type = BodyType.StaticBody;
			bodyDef.position.set(platformX, platformY);

			float angle = MathUtils.random(-45 * MathUtils.degreesToRadians,
					45 * MathUtils.degreesToRadians);
			bodyDef.angle = angle;
			
			Body platform = world.createBody(bodyDef);
			
			PolygonShape shape = new PolygonShape();
			shape.setAsBox(width / 2, height / 2);
			
			platform.createFixture(shape, 0);

			shape.dispose();

			platformSprite.setSize(3f, 1f);
			platformSprite.setOrigin(platformSprite.getWidth() / 2,
					platformSprite.getHeight() / 2);
			platformSprite.setPosition(platformX - platformSprite.getWidth() / 2, platformY - platformSprite.getHeight() / 2);
    		platformSprite.setRotation(angle * MathUtils.radiansToDegrees);
    		
			platform.setUserData(platformSprite);

			platformY += MathUtils.random(3, 5);
		}
	}

	public void repositionAbove(Body platform, float platformY) {
		
	}

	public void repositionBelow(Body platform, float platformY) {
				
	}
}
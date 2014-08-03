package com.tanyelbariser.iceaspirations.platforms;

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
	float leftScreenEdge = -GameScreen.width / GameScreen.ZOOM / 2;
	float rightScreenEdge = GameScreen.width / GameScreen.ZOOM / 2;
	float bottomScreenEdge = -GameScreen.height / GameScreen.ZOOM / 2;
	private boolean placeLeft = true;

	public Platforms(World world) {
		platformY = bottomScreenEdge;

		bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(0, 0);

		ChainShape worldContainerShape = new ChainShape();

		Vector2 topLeft = new Vector2(leftScreenEdge, 5000);
		Vector2 bottomLeft = new Vector2(leftScreenEdge, bottomScreenEdge);
		Vector2 bottomRight = new Vector2(rightScreenEdge, bottomScreenEdge);
		Vector2 topRight = new Vector2(rightScreenEdge, 5000);

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

	public void createPlatforms(float topEdge) {
		while (platformY < topEdge + 10) {
			float width = 3, height = 1;			
			float platformX = 0;
			if (placeLeft ) {
				platformX  = MathUtils.random(leftScreenEdge + width/2, 0 - width/2);
			} else {
				platformX = MathUtils.random(0 + width/2, rightScreenEdge - width/2);
			}
			placeLeft = !placeLeft;
			PolygonShape shape = new PolygonShape();
			Vector2 platformPosition = new Vector2(platformX, platformY);

			float angle = MathUtils.random(0, 1) < 0.5f ? 0 : MathUtils.random(-45 * MathUtils.degreesToRadians,
					45 * MathUtils.degreesToRadians);
			shape.setAsBox(width / 2, height / 2, platformPosition, angle);

			worldContainer.createFixture(shape, 0);

			shape.dispose();

			platformY += MathUtils.random(3, 5);
		}
	}
}
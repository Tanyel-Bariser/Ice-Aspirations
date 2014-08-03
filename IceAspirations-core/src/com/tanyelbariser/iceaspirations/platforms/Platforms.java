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

	public Platforms(World world) {
		platformY = bottomScreenEdge;

		bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(0, 0);

		ChainShape worldContainerShape = new ChainShape();

		Vector2 topLeft = new Vector2(leftScreenEdge, 500);
		Vector2 bottomLeft = new Vector2(leftScreenEdge, bottomScreenEdge);
		Vector2 bottomRight = new Vector2(rightScreenEdge, bottomScreenEdge);
		Vector2 topRight = new Vector2(rightScreenEdge, 500);

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

			float platformX = MathUtils.random(leftScreenEdge, rightScreenEdge);

			PolygonShape shape = new PolygonShape();
			Vector2 platformPosition = new Vector2(platformX, platformY);

			float width = 3, height = 1;
			shape.setAsBox(width / 2, height / 2, platformPosition,
					MathUtils.random(-5, 5));

			fixDef.shape = shape;
			fixDef.friction = 0f;
			fixDef.restitution = 0;

			worldContainer.createFixture(fixDef);

			shape.dispose();

			platformY += MathUtils.random(3, 4);
		}
	}
}
package com.tanyelbariser.iceaspirations.platforms;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.tanyelbariser.iceaspirations.screens.GameScreen;

public class Platforms {
	private BodyDef bodyDef;
	private FixtureDef fixDef;

	public Platforms(World world) {
		bodyDef = new BodyDef();		
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(0, 0);

		ChainShape worldContainerShape = new ChainShape();

		Vector2 topLeft = new Vector2(-GameScreen.width/GameScreen.ZOOM/2, 500);
		Vector2 bottomLeft = new Vector2(-GameScreen.width/GameScreen.ZOOM/2, -GameScreen.height/GameScreen.ZOOM/2);
		Vector2 bottomRight = new Vector2(GameScreen.width/GameScreen.ZOOM/2, -GameScreen.height/GameScreen.ZOOM/2);
		Vector2 topRight = new Vector2(GameScreen.width/GameScreen.ZOOM/2, 500);

		worldContainerShape.createChain(new Vector2[] {topLeft, bottomLeft, bottomRight, topRight});

		fixDef = new FixtureDef();
		fixDef.shape = worldContainerShape;
		fixDef.friction = 0f;
		fixDef.restitution = 0;

		Body worldContainer = world.createBody(bodyDef);
		worldContainer.createFixture(fixDef);
		
		worldContainerShape.dispose();
	}
}

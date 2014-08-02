package com.tanyelbariser.iceaspirations.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.tanyelbariser.iceaspirations.IceAspirations;
import com.tanyelbariser.iceaspirations.screens.GameScreen;

public class Player {
	final float height = Gdx.graphics.getHeight();
	final float width = Gdx.graphics.getWidth();
	private BodyDef bodyDef;
	private FixtureDef fixDef;
	public Body player;
	public Sprite playerSprite;
	private Array<Body> playerBodies = new Array<Body>();
	public Body body;

	public Player(World world) {
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(0, -height/GameScreen.ZOOM/3);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(1f, 2f);
		
		fixDef = new FixtureDef();
		fixDef.shape = shape;
		fixDef.density = 3f;
		fixDef.friction = 0f;
		fixDef.restitution = 0f;
		
		player = world.createBody(bodyDef);
		player.createFixture(fixDef);
		

		playerSprite = IceAspirations.skin.getSprite("Rabbit1");
		playerSprite.setSize(2.8f, 5.6f);
		playerSprite.setOrigin(playerSprite.getWidth()/2, playerSprite.getHeight()/2);
		player.setUserData(playerSprite);
		world.getBodies(playerBodies);
		body = playerBodies.first();
	}
}

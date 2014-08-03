package com.tanyelbariser.iceaspirations.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.tanyelbariser.iceaspirations.IceAspirations;
import com.tanyelbariser.iceaspirations.screens.GameScreen;

public class Player {
	final float height = Gdx.graphics.getHeight();
	final float width = Gdx.graphics.getWidth();
	private BodyDef bodyDef;
	private FixtureDef fixDef;
	public Body body;
	public Sprite playerSprite;

	public Player(World world) {
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(0, -height/GameScreen.ZOOM/3);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(0.75f, 1.5f);
		
		fixDef = new FixtureDef();
		fixDef.shape = shape;
		fixDef.density = 3f;
		fixDef.friction = 0f;
		fixDef.restitution = 0f;
		
		body = world.createBody(bodyDef);
		body.createFixture(fixDef);
		

		playerSprite = IceAspirations.skin.getSprite("Rabbit1");
		playerSprite.setSize(2.1f, 4.2f);
		playerSprite.setOrigin(playerSprite.getWidth()/2, playerSprite.getHeight()/2);
		body.setUserData(playerSprite);
	}
	
	public void update() {
		float accel = -Gdx.input.getAccelerometerX() * 2;
		float y = body.getLinearVelocity().y;
		if (accel > 0) {
			body.setLinearVelocity(accel, y);
		} else {
			body.setLinearVelocity(accel, y);
		}
	}
}

package com.tanyelbariser.iceaspirations.platforms;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.tanyelbariser.iceaspirations.IceAspirations;

public class PlatformsFactory {

	private static BodyDef bodyDef;
	private static Body platform;
	private static Sprite sprite;
	private static PolygonShape shape;

	public static ArrayList<Body> createPlatforms(World world) {
		ArrayList<Body> platforms = new ArrayList<Body>();
		
		platforms.add(createPlatform1(world));
		platforms.add(createPlatform2(world));
		platforms.add(createPlatform3(world));
		platforms.add(createPlatform4(world));
		platforms.add(createPlatform5(world));
		platforms.add(createPlatform6(world));
		platforms.add(createPlatform7(world));
		platforms.add(createPlatform8(world));
		platforms.add(createPlatform9(world));
		
		return platforms;
	}
	
	private static Body createPlatform1(World world) {
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(5, Platforms.BOTTOM_SCREEN_EDGE + 3);
		bodyDef.angle = MathUtils.random(-45 * MathUtils.degreesToRadians,
				45 * MathUtils.degreesToRadians);
		platform = world.createBody(bodyDef);

		float width = 4;
		float height = 2;
		shape = new PolygonShape();
		shape.setAsBox(width / 2, height / 2);
		platform.createFixture(shape, 0);
		shape.dispose();

		sprite = IceAspirations.skin.getSprite("Platform1");
		sprite.setSize(width, height);

		platform.setUserData(sprite);

		return platform;
	}

	private static Body createPlatform2(World world) {
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(5, Platforms.BOTTOM_SCREEN_EDGE + 8);
		bodyDef.angle = MathUtils.random(-45 * MathUtils.degreesToRadians,
				45 * MathUtils.degreesToRadians);
		platform = world.createBody(bodyDef);

		float width = 4;
		float height = 2;
		shape = new PolygonShape();
		shape.setAsBox(width / 2, height / 2);
		platform.createFixture(shape, 0);
		shape.dispose();

		sprite = IceAspirations.skin.getSprite("Platform2");
		sprite.setSize(width, height);

		platform.setUserData(sprite);

		return platform;
	}
	
	private static Body createPlatform3(World world) {
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(-5, Platforms.BOTTOM_SCREEN_EDGE + 8);
		bodyDef.angle = MathUtils.random(-45 * MathUtils.degreesToRadians,
				45 * MathUtils.degreesToRadians);
		platform = world.createBody(bodyDef);

		float width = 4;
		float height = 2;
		shape = new PolygonShape();
		shape.setAsBox(width / 2, height / 2);
		platform.createFixture(shape, 0);
		shape.dispose();

		sprite = IceAspirations.skin.getSprite("Platform3");
		sprite.setSize(width, height);

		platform.setUserData(sprite);

		return platform;
	}

	private static Body createPlatform4(World world) {
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(-5, Platforms.BOTTOM_SCREEN_EDGE + 3);
		bodyDef.angle = MathUtils.random(-45 * MathUtils.degreesToRadians,
				45 * MathUtils.degreesToRadians);
		platform = world.createBody(bodyDef);

		float width = 4;
		float height = 2;
		shape = new PolygonShape();
		shape.setAsBox(width / 2, height / 2);
		platform.createFixture(shape, 0);
		shape.dispose();

		sprite = IceAspirations.skin.getSprite("Platform4");
		sprite.setSize(width, height);

		platform.setUserData(sprite);

		return platform;
	}
	
	private static Body createPlatform5(World world) {
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(5, Platforms.BOTTOM_SCREEN_EDGE + 13);
		bodyDef.angle = MathUtils.random(-45 * MathUtils.degreesToRadians,
				45 * MathUtils.degreesToRadians);
		platform = world.createBody(bodyDef);

		float width = 8;
		float height = 1.8f;
		shape = new PolygonShape();
		shape.setAsBox(width / 2, height / 4);
		platform.createFixture(shape, 0);
		shape.dispose();

		sprite = IceAspirations.skin.getSprite("Platform5");
		sprite.setSize(width, height);

		platform.setUserData(sprite);

		return platform;
	}
	
	private static Body createPlatform6(World world) {
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(-5, Platforms.BOTTOM_SCREEN_EDGE + 13);
		bodyDef.angle = MathUtils.random(-45 * MathUtils.degreesToRadians,
				45 * MathUtils.degreesToRadians);
		platform = world.createBody(bodyDef);

		float width = 4;
		float height = 2;
		shape = new PolygonShape();
		shape.setAsBox(width / 2, height / 2);
		platform.createFixture(shape, 0);
		shape.dispose();

		sprite = IceAspirations.skin.getSprite("Platform6");
		sprite.setSize(width, height);

		platform.setUserData(sprite);

		return platform;
	}
	
	private static Body createPlatform7(World world) {
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(-5, Platforms.BOTTOM_SCREEN_EDGE + 18);
		bodyDef.angle = MathUtils.random(-45 * MathUtils.degreesToRadians,
				45 * MathUtils.degreesToRadians);
		platform = world.createBody(bodyDef);

		float width = 4;
		float height = 3;
		shape = new PolygonShape();
		shape.setAsBox(width / 2.5f, height / 2.5f);
		platform.createFixture(shape, 0);
		shape.dispose();

		sprite = IceAspirations.skin.getSprite("Platform7");
		sprite.setSize(width, height);

		platform.setUserData(sprite);

		return platform;
	}
	
	private static Body createPlatform8(World world) {
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(5, Platforms.BOTTOM_SCREEN_EDGE + 18);
		bodyDef.angle = MathUtils.random(-45 * MathUtils.degreesToRadians,
				45 * MathUtils.degreesToRadians);
		platform = world.createBody(bodyDef);

		float width = 6;
		float height = 3;
		shape = new PolygonShape();
		shape.setAsBox(width / 2.5f, height / 2.5f);
		platform.createFixture(shape, 0);
		shape.dispose();

		sprite = IceAspirations.skin.getSprite("Platform8");
		sprite.setSize(width, height);

		platform.setUserData(sprite);

		return platform;
	}
	
	private static Body createPlatform9(World world) {
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(5, Platforms.BOTTOM_SCREEN_EDGE + 23);
		bodyDef.angle = MathUtils.random(-45 * MathUtils.degreesToRadians,
				45 * MathUtils.degreesToRadians);
		platform = world.createBody(bodyDef);

		float width = 3;
		float height = 3;
		shape = new PolygonShape();
		shape.setAsBox(width / 2.5f, height / 2.5f);
		platform.createFixture(shape, 0);
		shape.dispose();

		sprite = IceAspirations.skin.getSprite("Platform9");
		sprite.setSize(width, height);

		platform.setUserData(sprite);

		return platform;
	}
}
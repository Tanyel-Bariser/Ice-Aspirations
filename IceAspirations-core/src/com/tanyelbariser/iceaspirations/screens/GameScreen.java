package com.tanyelbariser.iceaspirations.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton.ImageTextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.tanyelbariser.iceaspirations.IceAspirations;
import com.tanyelbariser.iceaspirations.entities.Player;
import com.tanyelbariser.iceaspirations.platforms.Platforms;
import com.tanyelbariser.iceaspirations.platforms.PlatformsFactory;

public class GameScreen implements Screen {
	private IceAspirations iceA;
	private final float GRAVITY = -9.81f;
	private SpriteBatch batch;
	private Stage stage;
	private Skin skin = IceAspirations.getSkin();
	private ImageButton pause;
	private static float phoneWidth = 768;
	public static final float WIDTH = Gdx.graphics.getWidth();
	public static final float HEIGHT = Gdx.graphics.getHeight();
	private static float compatibility = WIDTH / phoneWidth;
	private World world;
	private OrthographicCamera camera;
	private Box2DDebugRenderer physicsDebugger;
	private final Sprite background = new Sprite(new Texture("Background.png"));
	private float backgroundWidth = background.getWidth();
	private float backgroundHeight = background.getHeight();
	private Player player;
	public static final float ZOOM = 30f * compatibility;
	private Sprite stand;

	public enum State {
		RUNNING, PAUSED
	}

	private State state = State.RUNNING;

	private float approxFPS = 60.0f;
	private final float TIMESTEP = 1.0f / approxFPS;
	private final int VELOCITYITERATIONS = 8; // Box2d manual recommends 8 & 3
	private final int POSITIONITERATIONS = 3; // for these iterations values
	private Platforms platforms;
	private Array<Sprite> platformSprites = new Array<Sprite>();
	private Sprite playerSprite;
	private Array<Body> platformArray;
	private float frameTime;
	private float allotedTime = 60;
	private Label timeLeft;
	private int maxHeight;
	private ImageTextButton quit;
	private BitmapFont blue;
	private LabelStyle yellowStyle;
	private Body boulder;
	private Sprite boulderSprite;
	private Fixture boulderFix;
	private float heightLastBoulder = 0;
	private FixtureDef fixDef;
	private BodyDef bodyDef;
	private Body clock;
	private Sprite clockSprite;
	private float heightLastClock = 0;
	private float distanceBetweenBoulders = 200;
	private float distanceBetweenClocks = 100;
	private float timeDazed = 0;
	private Body carrot;
	private Sprite carrotSprite;
	private float heightLastCarrot = 0;
	private float distanceBetweenCarrots = 158;
	private float timeSinceCarrotTouched;
	private BitmapFont red = new BitmapFont(Gdx.files.internal("red.fnt"),
			false);
	private LabelStyle redStyle = new LabelStyle(red, Color.RED);

	public GameScreen(IceAspirations iceA) {
		this.iceA = iceA;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if (state.equals(State.RUNNING)) {
			allotedTime -= delta;
			if (player.isCarrotTouched()) {
				delta *= 1.5f;
				player.setDazed(false);
			}
			timeLeft.setText("Score: " + String.valueOf(maxHeight)
					+ "\nTime Limit: "
					+ String.valueOf(Math.round(allotedTime)));

			// adjustedDelta is rounded to nearest whole or half, i.e. 60FPS = 1
			// 45FPS = 1.5, 30FPS = 2, etc. for delta consistency per device
			float adjustedDelta = Math.round(Math.round(approxFPS * delta) * 2) / 2.0f;

			float gravity = GRAVITY * adjustedDelta * adjustedDelta;
			world.setGravity(new Vector2(0, gravity));

			world.step(TIMESTEP, VELOCITYITERATIONS, POSITIONITERATIONS);

			float topScreenEdge = camera.position.y + camera.viewportHeight / 2;
			float bottomScreenEdge = camera.position.y - camera.viewportHeight
					/ 2;
			updatePlayer(adjustedDelta, delta);
			repositionCamera(topScreenEdge, bottomScreenEdge);
			if (player.isCarrotTouched()) {
				float carrotGravity = GRAVITY * (adjustedDelta / 2)
						* (adjustedDelta / 2);
				repositionBoulder(topScreenEdge, bottomScreenEdge,
						carrotGravity);
			} else {
				repositionBoulder(topScreenEdge, bottomScreenEdge, gravity);
			}
			repositionPlatforms(topScreenEdge, bottomScreenEdge);
			repositionClock(topScreenEdge);
			repositionCarrot(topScreenEdge, delta / 2);

			// Position background at camera's position
			background.setPosition(camera.position.x - backgroundWidth / 2,
					camera.position.y - backgroundHeight / 2);

			manageTimeScore();
		}
		drawToScreen(delta);
		// physicsDebugger.render(world, camera.combined);
	}

	private void updatePlayer(float adjustedDelta, float delta) {
		player.update(adjustedDelta);
		if (player.getBody().getPosition().x > Platforms.RIGHT_SCREEN_EDGE) {
			player.getBody().setTransform(Platforms.RIGHT_SCREEN_EDGE - 0.5f,
					player.getBody().getPosition().y, 0);
		} else if (player.getBody().getPosition().x < Platforms.LEFT_SCREEN_EDGE) {
			player.getBody().setTransform(Platforms.LEFT_SCREEN_EDGE + 0.5f,
					player.getBody().getPosition().y, 0);
		}
		frameTime += delta;
		if (player.isDazed()) {
			playerSprite = player.getDazedSprite();
			timeDazed += delta;
			if (timeDazed == delta) {
				player.playHitSound();
			}
			if (timeDazed > 3) {
				player.setDazed(false);
				timeDazed = 0;
			}
		} else if (player.getBody().getLinearVelocity().y > 2) {
			if (player.isCarrotTouched()) {
				Animation specialJumpAnimation = player
						.getSpecialJumpAnimation();
				playerSprite = (Sprite) specialJumpAnimation.getKeyFrame(
						frameTime, true);
			} else {
				Animation jumpAnimation = player.getjumpAnimation();
				playerSprite = (Sprite) jumpAnimation.getKeyFrame(frameTime,
						false);
			}
		} else if (player.getBody().getLinearVelocity().y < -4
				& !player.isStanding()) {
			if (player.isCarrotTouched()) {
				Animation fallAnimation = player.getSpecialFallAnimation();
				playerSprite = (Sprite) fallAnimation.getKeyFrame(frameTime,
						true);
			} else {
				playerSprite = player.getFallingSprite();
				frameTime = 0;
			}
		} else {
			if (player.isCarrotTouched()) {
				Animation standAnimation = player.getSpecialStandAnimation();
				playerSprite = (Sprite) standAnimation.getKeyFrame(frameTime,
						true);
			} else {
				playerSprite = stand;
				frameTime = 0;
			}
		}
		boolean facingLeft = player.getFacingLeft() && playerSprite.isFlipX();
		boolean facingRight = !player.getFacingLeft()
				&& !playerSprite.isFlipX();
		if (facingLeft || facingRight) {
			// do nothing if player is moving left AND the playerSprite is
			// already facing left (same for right)
		} else {
			playerSprite.flip(true, false);
		}
		playerSprite.setPosition(player.getBody().getPosition().x
				- playerSprite.getWidth() / 2, player.getBody().getPosition().y
				- playerSprite.getHeight() / 2);
		playerSprite.setRotation(player.getBody().getAngle()
				* MathUtils.radiansToDegrees);
	}

	// Set camera position based on player position with
	// high speed camera catch-up lag
	private void repositionCamera(float topScreenEdge, float bottomScreenEdge) {
		float playerY = player.getBody().getPosition().y;
		float highSpeed = 30;
		if (player.getBody().getLinearVelocity().y > highSpeed
				&& camera.position.y > HEIGHT) {
			// Camera rises slower than player causing a camera lag giving the
			// effect that the player is too fast for the camera to keep up.
			camera.position.y += 0.8f;
		} else if (playerY > topScreenEdge) {
			camera.position.y += 3f;
		} else if (playerY > camera.position.y + 2f) {
			camera.position.y += 0.8f;
		} else if (playerY > 0 && playerY < camera.position.y - 2f) {
			camera.position.y -= 0.3f;
		} else if (playerY < bottomScreenEdge) {
			camera.position.y -= 3f;
		} else if (playerY > 0) {
			camera.position.y = playerY;
		} else {
			camera.position.y = 0;
		}
		camera.update();
	}

	private void repositionBoulder(float topScreenEdge, float bottomScreenEdge,
			float gravity) {
		if (boulder.getPosition().y < bottomScreenEdge - 10
				&& camera.position.y > heightLastBoulder
						+ distanceBetweenBoulders) {
			boulder.setTransform(player.getBody().getPosition().x,
					topScreenEdge + 10, 0);
			heightLastBoulder = camera.position.y;
			if (distanceBetweenBoulders > 20) {
				distanceBetweenBoulders -= 20;
			}
		}
		if (boulder.getLinearVelocity().y > -1) {
			boulder.setLinearVelocity(0, gravity);
		}
		boulderSprite.setPosition(
				boulder.getPosition().x - boulderSprite.getWidth() / 2,
				boulder.getPosition().y - boulderSprite.getHeight() / 2);
		boulderSprite.setRotation(boulder.getAngle()
				* MathUtils.radiansToDegrees);
	}

	// Repositions platform if out of camera/screen view
	private void repositionPlatforms(float topScreenEdge, float bottomScreenEdge) {
		for (Body platform : platformArray) {
			if (platform.getPosition().y < bottomScreenEdge - 25) {
				platforms.repositionAbove(platform, topScreenEdge);
			} else if (platform.getPosition().y > topScreenEdge + 25) {
				platforms.repositionBelow(platform, bottomScreenEdge);
			}
		}
	}

	// Reposition clock after being touched
	private void repositionClock(float topScreenEdge) {
		if (!player.isClockTouched()
				&& camera.position.y > heightLastClock + distanceBetweenClocks) {
			for (Body platform : platformArray) {
				if (platform.getPosition().y > topScreenEdge) {
					clock.setTransform(platform.getPosition().x,
							platform.getPosition().y + 2.5f,
							platform.getAngle());
					break;
				}
			}
			heightLastClock = camera.position.y;
			distanceBetweenClocks += 20;
		}
		if (player.isClockTouched()) {
			allotedTime += 10;
			clock.setTransform(-50, 0, 0);
			player.setClockTouched(false);
		}
		clockSprite.setPosition(clock.getPosition().x - clockSprite.getWidth()
				/ 2, clock.getPosition().y - clockSprite.getHeight() / 2);
		clockSprite.setRotation(clock.getAngle() * MathUtils.radiansToDegrees);
	}

	// Reposition carrot after being touched
	private void repositionCarrot(float topScreenEdge, float delta) {
		if (player.isCarrotTouched()) {
			carrot.setTransform(-50, 0, 0);
			timeSinceCarrotTouched += delta;
		}
		if (timeSinceCarrotTouched > 10) {
			timeSinceCarrotTouched = 0;
			player.setCarrotTouched(false);
		}
		if (!player.isCarrotTouched()
				&& camera.position.y > heightLastCarrot
						+ distanceBetweenCarrots) {
			for (Body platform : platformArray) {
				if (platform.getPosition().y > topScreenEdge) {
					carrot.setTransform(platform.getPosition().x,
							platform.getPosition().y + 2.5f,
							platform.getAngle());
					break;
				}
			}
			heightLastCarrot = camera.position.y;
			distanceBetweenCarrots += 70;
		}
		carrotSprite.setPosition(
				carrot.getPosition().x - carrotSprite.getWidth() / 2,
				carrot.getPosition().y - carrotSprite.getHeight() / 2);
		carrotSprite
				.setRotation(carrot.getAngle() * MathUtils.radiansToDegrees);
	}

	private void manageTimeScore() {
		if (camera.position.y > maxHeight) {
			// maxHeight records users score
			maxHeight = (int) camera.position.y;
		}
		if (allotedTime < 0) {
			iceA.setNextScreen(new GameOverScreen(iceA, maxHeight));
		} else if (allotedTime < 11) {
			if (IceAspirations.getMusic().isPlaying()
					|| IceAspirations.getCarrotMusic().isPlaying()
					&& !IceAspirations.getTimeOutMusic().isPlaying()) {
				IceAspirations.getTimeOutMusic().play();
			}
			timeLeft.setStyle(redStyle);
			timeLeft.setPosition(timeLeft.getWidth() / 10,
					HEIGHT - timeLeft.getHeight() * 2);
		} else {
			if (IceAspirations.getTimeOutMusic().isPlaying()) {
				IceAspirations.getTimeOutMusic().stop();
			}
			timeLeft.setStyle(yellowStyle);
			timeLeft.setPosition(timeLeft.getWidth() / 10,
					HEIGHT - timeLeft.getHeight() * 1.5f);
		}
		if (player.isCarrotTouched()) {
			if (IceAspirations.getMusic().isPlaying()
					&& !IceAspirations.getCarrotMusic().isPlaying()) {
				IceAspirations.getMusic().stop();
				IceAspirations.getCarrotMusic().play();
			}
		} else {
			IceAspirations.getMusic().play();
		}
	}

	private void drawToScreen(float delta) {
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		background.draw(batch);
		playerSprite.draw(batch);
		boulderSprite.draw(batch);
		clockSprite.draw(batch);
		carrotSprite.draw(batch);
		for (Sprite platform : platformSprites) {
			platform.draw(batch);
		}
		batch.end();

		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		camera.viewportWidth = width / ZOOM;
		camera.viewportHeight = height / ZOOM;
		camera.update();
	}

	@Override
	public void show() {
		batch = new SpriteBatch();
		stage = new Stage();

		background.setScale(1f / ZOOM * compatibility);

		pauseButtonSetUp();
		quitButtonSetUp();

		world = new World(new Vector2(0, GRAVITY), true);
		physicsDebugger = new Box2DDebugRenderer();
		camera = new OrthographicCamera(WIDTH / ZOOM, HEIGHT / ZOOM);

		player = new Player(world);
		Gdx.input.setInputProcessor(new InputMultiplexer(stage, player));
		stand = player.getStandSprite();

		platforms = new Platforms(world);

		platformArray = PlatformsFactory.createPlatforms(world);
		platformArray.ordered = false;

		// Match sprite position to platform
		for (Body platform : platformArray) {
			Sprite sprite = (Sprite) platform.getUserData();
			sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
			sprite.setPosition(
					platform.getPosition().x - sprite.getWidth() / 2,
					platform.getPosition().y - sprite.getHeight() / 2);
			sprite.setRotation(platform.getAngle() * MathUtils.radiansToDegrees);
			platformSprites.add(sprite);
		}

		createIceBoulder();
		createClock();
		createCarrot();

		// Create Label to show remaining game time
		BitmapFont yellow = new BitmapFont(Gdx.files.internal("yellow.fnt"),
				false);
		yellowStyle = new LabelStyle(yellow, Color.YELLOW);
		timeLeft = new Label("60", yellowStyle);
		timeLeft.setPosition(timeLeft.getWidth() / 10,
				HEIGHT - timeLeft.getHeight() * 1.5f);
		timeLeft.setScale(HEIGHT);
		stage.addActor(timeLeft);
	}

	private void createClock() {
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(0, -HEIGHT);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(1, 1);

		fixDef = new FixtureDef();
		fixDef.shape = shape;
		fixDef.isSensor = true;

		clock = world.createBody(bodyDef);
		clock.createFixture(fixDef).setUserData("clock");

		shape.dispose();

		clockSprite = new Sprite(new Texture("Clock.png"));
		clockSprite.setPosition(0, -HEIGHT);
		clockSprite.setSize(2, 2);
		clockSprite.setOrigin(clockSprite.getWidth() / 2,
				clockSprite.getHeight() / 2);
	}

	private void createCarrot() {
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(0, -HEIGHT);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(1, 1);

		fixDef = new FixtureDef();
		fixDef.shape = shape;
		fixDef.isSensor = true;

		carrot = world.createBody(bodyDef);
		carrot.createFixture(fixDef).setUserData("carrot");

		shape.dispose();

		carrotSprite = new Sprite(new Texture("Carrot.png"));
		carrotSprite.setPosition(0, HEIGHT);
		carrotSprite.setSize(2, 2);
		carrotSprite.setOrigin(carrotSprite.getWidth() / 2,
				carrotSprite.getHeight() / 2);
	}

	private void createIceBoulder() {
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(0, -HEIGHT);

		CircleShape shape = new CircleShape();
		shape.setRadius(2.5f);

		fixDef = new FixtureDef();
		fixDef.shape = shape;
		fixDef.density = 3f;
		fixDef.friction = 1f;
		fixDef.restitution = 0f;

		boulder = world.createBody(bodyDef);
		boulderFix = boulder.createFixture(fixDef);
		boulderFix.setUserData("boulder");

		shape.dispose();

		boulderSprite = new Sprite(new Texture("Boulder.png"));
		boulderSprite.setSize(5f, 5f);
		boulderSprite.setOrigin(boulderSprite.getWidth() / 2,
				boulderSprite.getHeight() / 2);
	}

	// For some reason pause button suddenly doesn't work on desktop, but still
	// works on Android. Don't bother trying to fix this yet as I'll re-do this
	// with an AssetManger, which may fix it.
	private void pauseButtonSetUp() {
		ImageButtonStyle imageStyle = new ImageButtonStyle();
		imageStyle.up = skin.getDrawable("Pause");
		imageStyle.down = skin.getDrawable("Play");
		imageStyle.checked = imageStyle.down;

		pause = new ImageButton(imageStyle);

		float pauseSize = WIDTH / 6;
		pause.setSize(pauseSize, pauseSize);

		pause.setPosition(WIDTH - pauseSize, HEIGHT - pauseSize);

		pause.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				boolean checked = pause.isChecked();
				if (checked) {
					pause.setChecked(true);
					pause();
					stage.addActor(quit);
				} else {
					pause.setChecked(false);
					resume();
					quit.remove();
				}
			}
		});
		stage.addActor(pause);
	}

	// Button to quit current game
	private void quitButtonSetUp() {
		ImageTextButtonStyle style = new ImageTextButtonStyle();
		blue = IceAspirations.getBlue();
		blue.setScale(WIDTH / 300);
		style.font = blue;
		quit = new ImageTextButton("Quit", style);

		float x = WIDTH / 2 - quit.getWidth() / 2;
		float y = HEIGHT / 2 - quit.getHeight() / 2;
		quit.setPosition(x, y);

		quit.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				if (IceAspirations.getTimeOutMusic().isPlaying()) {
					IceAspirations.getTimeOutMusic().stop();
				}
				iceA.setScreen(new MainScreen(iceA));
			}
		});
	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void pause() {
		state = State.PAUSED;

	}

	@Override
	public void resume() {
		state = State.RUNNING;
	}

	@Override
	public void dispose() {
		batch.dispose();
		stage.dispose();
		world.dispose();
		physicsDebugger.dispose();
		background.getTexture().dispose();
	}
}
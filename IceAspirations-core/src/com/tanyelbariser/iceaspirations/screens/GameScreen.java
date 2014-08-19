package com.tanyelbariser.iceaspirations.screens;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton.ImageTextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.tanyelbariser.iceaspirations.AudioManager;
import com.tanyelbariser.iceaspirations.CollisionDetection;
import com.tanyelbariser.iceaspirations.Controller;
import com.tanyelbariser.iceaspirations.IceAspirations;
import com.tanyelbariser.iceaspirations.entities.Boulder;
import com.tanyelbariser.iceaspirations.entities.Clock;
import com.tanyelbariser.iceaspirations.entities.Player;
import com.tanyelbariser.iceaspirations.factories.ButtonFactory;
import com.tanyelbariser.iceaspirations.factories.PlatformsFactory;
import com.tanyelbariser.iceaspirations.factories.SpriteFactory;
import com.tanyelbariser.iceaspirations.platforms.PlatformManager;

public class GameScreen implements Screen {
	private IceAspirations iceA;
	private final float GRAVITY = -9.81f;
	private SpriteBatch batch;
	private Stage stage;
	private static float phoneWidth = 768;
	public static final float WIDTH = Gdx.graphics.getWidth();
	public static final float HEIGHT = Gdx.graphics.getHeight();
	private static float compatibility = WIDTH / phoneWidth;
	private World world;
	private OrthographicCamera camera;
	private Box2DDebugRenderer physicsDebugger;
	private final Sprite background = SpriteFactory.createBackground();
	private float backgroundWidth = background.getWidth();
	private float backgroundHeight = background.getHeight();
	private Player player;
	public static final float ZOOM = 30f * compatibility;;
	private State state = State.RUNNING;
	private float approxFPS = 60.0f;
	private final float TIMESTEP = 1.0f / approxFPS;
	private final int VELOCITYITERATIONS = 8; // Box2d manual recommends 8 & 3
	private final int POSITIONITERATIONS = 3; // for these iterations values
	private Array<Sprite> platformSprites = new Array<Sprite>();
	private Sprite playerSprite;
	private Array<Body> platformArray;
	private float allotedTime = 100;
	private Label timeLeft;
	private int maxHeight;
	private ImageTextButton quit;
	private BitmapFont blue;
	private LabelStyle yellowStyle;
	private Sprite boulderSprite;
	private FixtureDef fixDef;
	private BodyDef bodyDef;
	private Sprite clockSprite;
	private Body carrot;
	private Sprite carrotSprite;
	private float heightLastCarrot = 0;
	private float distanceBetweenCarrots = 158;
	private float timeSinceCarrotTouched;
	private BitmapFont red = new BitmapFont(Gdx.files.internal("red.fnt"),
			false);
	private LabelStyle redStyle = new LabelStyle(red, Color.RED);
	private CollisionDetection contact;
	private final float CARROT_MODE = 1.5f;

	public GameScreen(IceAspirations iceA) {
		this.iceA = iceA;
	}

	public void setPlayerSprite(Sprite playerSprite) {
		this.playerSprite = playerSprite;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if (state.equals(State.RUNNING)) {
			allotedTime -= delta;
			if (contact.isCarrotTouched()) {
				delta *= CARROT_MODE;
				contact.setDazed(false);
			}
			timeLeft.setText("Score: " + String.valueOf(maxHeight) + "\nTime: "
					+ String.valueOf(Math.round(allotedTime)));

			float adjustedDelta = approxFPS * delta;

			float gravity = GRAVITY * adjustedDelta * adjustedDelta;
			world.setGravity(new Vector2(0, gravity));

			world.step(TIMESTEP, VELOCITYITERATIONS, POSITIONITERATIONS);

			float topScreenEdge = camera.position.y + camera.viewportHeight / 2;
			float bottomScreenEdge = camera.position.y - camera.viewportHeight
					/ 2;
			player.update(adjustedDelta);
			playerSprite = player.updateSprite(adjustedDelta, delta);
			repositionCamera(topScreenEdge, bottomScreenEdge);
			if (contact.isCarrotTouched()) {
				float carrotGravity = GRAVITY * (adjustedDelta / CARROT_MODE)
						* (adjustedDelta / CARROT_MODE);
				boulderSprite = Boulder.repositionBoulder(boulderSprite,
						camera, topScreenEdge, bottomScreenEdge, carrotGravity,
						player.getBody().getPosition().x);
			} else {
				boulderSprite = Boulder.repositionBoulder(boulderSprite,
						camera, topScreenEdge, bottomScreenEdge, gravity,
						player.getBody().getPosition().x);
			}
			PlatformManager.repositionPlatforms(topScreenEdge,
					bottomScreenEdge, platformArray);
			clockSprite = Clock.repositionClock(clockSprite, camera, contact,
					topScreenEdge, platformArray, this);
			repositionCarrot(topScreenEdge, delta / CARROT_MODE);

			// Position background at camera's position
			background.setPosition(camera.position.x - backgroundWidth / 2,
					camera.position.y - backgroundHeight / 2);
		}
		manageTimeScore();
		drawToScreen(delta);
		// physicsDebugger.render(world, camera.combined);
	}

	// Set camera position based on player position with
	// high speed camera catch-up lag
	private void repositionCamera(float topScreenEdge, float bottomScreenEdge) {
		float playerY = player.getBody().getPosition().y;
		float veryHighSpeed = 80, highSpeed = 30;
		if (player.getBody().getLinearVelocity().y > veryHighSpeed
				&& camera.position.y > HEIGHT) {
			// Camera rises slower than player causing a camera lag giving the
			// effect that the player is too fast for the camera to keep up.
			camera.position.y += 0.8f;
		} else if (player.getBody().getLinearVelocity().y > highSpeed
				&& camera.position.y > HEIGHT) {
			camera.position.y += 0.5f;
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

	// Reposition carrot after being touched
	private void repositionCarrot(float topScreenEdge, float delta) {
		if (contact.isCarrotTouched()) {
			carrot.setTransform(-50, 0, 0);
			timeSinceCarrotTouched += delta;
		}
		if (timeSinceCarrotTouched > 7.5f) {
			timeSinceCarrotTouched = 0;
			contact.setCarrotTouched(false);
		}
		if (!contact.isCarrotTouched()
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
		} else if (allotedTime < 10) {
			AudioManager.playLowTimeMusic();

			timeLeft.setStyle(redStyle);
			timeLeft.setPosition(timeLeft.getWidth() / 10,
					HEIGHT - timeLeft.getHeight() * 2.5f);
		} else {
			AudioManager.stopLowTimeMusic();

			timeLeft.setStyle(yellowStyle);
			timeLeft.setPosition(timeLeft.getWidth() / 10,
					HEIGHT - timeLeft.getHeight() * 2);
		}
		if (contact.isCarrotTouched()) {
			AudioManager.playSuperMusic();
		} else {
			AudioManager.playMainMusic();
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

		background.setScale((1f / ZOOM * compatibility) * 1.1f);

		pauseButtonSetUp();
		quitButtonSetUp();

		physicsDebugger = new Box2DDebugRenderer();
		camera = new OrthographicCamera(WIDTH / ZOOM, HEIGHT / ZOOM);

		world = new World(new Vector2(0, GRAVITY), true);
		player = new Player(world);
		contact = new CollisionDetection(player);
		world.setContactListener(contact);
		Gdx.input.setInputProcessor(new InputMultiplexer(stage, new Controller(
				player, contact)));

		PlatformsFactory.createGroundWalls(world);
		platformArray = PlatformsFactory.createPlatforms(world);

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

		Boulder.createIceBoulder(world);
		boulderSprite = SpriteFactory.createBoulder();
		Clock.createClock(world);
		clockSprite = SpriteFactory.createClock();
		createCarrot();

		// Create Label to show remaining game time
		BitmapFont yellow = new BitmapFont(Gdx.files.internal("yellow.fnt"),
				false);
		yellowStyle = new LabelStyle(yellow, Color.YELLOW);
		timeLeft = new Label("60", yellowStyle);
		timeLeft.setPosition(timeLeft.getWidth() / 10,
				HEIGHT - timeLeft.getHeight() * 2);
		if (Gdx.app.getType() == ApplicationType.Android) {
			yellowStyle.font.setScale(1.5f);
			redStyle.font.setScale(1.5f);
		}
		stage.addActor(timeLeft);
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

		carrotSprite = SpriteFactory.createCarrot();
	}

	private void pauseButtonSetUp() {
		final ImageButton pause = ButtonFactory.createImageButton("Pause",
				"Play", 0, 0, false);
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
				AudioManager.stopLowTimeMusic();
				AudioManager.playMainMusic();
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
	}

	public void increaseAllotedTime(float increment) {
		allotedTime += increment;
	}
}
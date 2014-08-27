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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
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
import com.tanyelbariser.iceaspirations.Assets;
import com.tanyelbariser.iceaspirations.AudioManager;
import com.tanyelbariser.iceaspirations.CollisionDetection;
import com.tanyelbariser.iceaspirations.Controller;
import com.tanyelbariser.iceaspirations.IceAspirations;
import com.tanyelbariser.iceaspirations.entities.Boulder;
import com.tanyelbariser.iceaspirations.entities.Carrot;
import com.tanyelbariser.iceaspirations.entities.Clock;
import com.tanyelbariser.iceaspirations.entities.Platforms;
import com.tanyelbariser.iceaspirations.entities.Player;
import com.tanyelbariser.iceaspirations.factories.ButtonFactory;
import com.tanyelbariser.iceaspirations.factories.SpriteFactory;

public class GameScreen implements Screen {
	private IceAspirations iceA;
	private AudioManager audio = IceAspirations.getAudio();
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
	private Sprite clockSprite;
	private Sprite carrotSprite;
	private BitmapFont red = IceAspirations.getAssets().getManager()
			.get(Assets.RED_FONT, BitmapFont.class);
	private LabelStyle redStyle = new LabelStyle(red, Color.RED);
	private CollisionDetection contact;
	private final float CARROT_MODE = 1.5f;
	private Platforms platforms;
	private Boulder boulder;
	private Carrot carrot;
	private Clock clock;
	private int prevTime = 100;
	private String time = null;
	private float topScreenEdge;
	private float bottomScreenEdge;

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
			// prevents creating new string object of time every frame
			if (allotedTime < prevTime) {
				time = String.valueOf(prevTime);
				prevTime = (int) allotedTime;
			}
			timeLeft.setText("Score: " + String.valueOf(maxHeight) + "\nTime: "
					+ time);

			float adjustedDelta = approxFPS * delta;

			float gravity = GRAVITY * adjustedDelta * adjustedDelta;
			world.setGravity(new Vector2(0, gravity));

			world.step(TIMESTEP, VELOCITYITERATIONS, POSITIONITERATIONS);

			topScreenEdge = camera.position.y + camera.viewportHeight / 2;
			bottomScreenEdge = camera.position.y - camera.viewportHeight / 2;
			player.update(adjustedDelta);
			playerSprite = player.updateSprite(adjustedDelta, delta);
			repositionCamera(topScreenEdge, bottomScreenEdge);
			if (contact.isCarrotTouched()) {
				float carrotGravity = GRAVITY * (adjustedDelta / CARROT_MODE)
						* (adjustedDelta / CARROT_MODE);
				boulder.repositionBoulder(boulderSprite, camera, topScreenEdge,
						bottomScreenEdge, carrotGravity, player.getBody()
								.getPosition().x);
			} else {
				boulder.repositionBoulder(boulderSprite, camera, topScreenEdge,
						bottomScreenEdge, gravity, player.getBody()
								.getPosition().x);
			}
			platforms.repositionPlatforms(topScreenEdge, bottomScreenEdge,
					platformArray);
			clock.repositionClock(clockSprite, camera, contact, topScreenEdge,
					platformArray, this);
			carrot.repositionCarrot(carrotSprite, camera, contact,
					topScreenEdge, delta / CARROT_MODE, platformArray);

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
		float highSpeed = 80;
		float playerSpeed = player.getBody().getLinearVelocity().y;
		if (playerY > topScreenEdge) {
			camera.position.y += 2;
		} else if (playerY < bottomScreenEdge) {
			camera.position.y -= 2;
		} else if ((playerSpeed > highSpeed && camera.position.y > HEIGHT)
				|| playerY > camera.position.y + 2) {
			camera.position.y += 0.8f;
		} else if (playerY > 0 && playerY < camera.position.y - 1) {
			camera.position.y -= 0.8f;
		} else if (playerY > 0) {
			camera.position.y = playerY;
		} else {
			camera.position.y = 0;
		}
		camera.update();
	}

	private void manageTimeScore() {
		if (camera.position.y > maxHeight) {
			// maxHeight records users score
			maxHeight = (int) camera.position.y;
		}
		if (allotedTime < 0) {
			iceA.setNextScreen(new GameOverScreen(iceA, maxHeight));
		} else if (allotedTime < 10) {
			audio.playLowTimeMusic();

			timeLeft.setStyle(redStyle);
			timeLeft.setPosition(timeLeft.getWidth() / 10,
					HEIGHT - timeLeft.getHeight() * 2.5f);
		} else {
			audio.stopLowTimeMusic();

			timeLeft.setStyle(yellowStyle);
			timeLeft.setPosition(timeLeft.getWidth() / 10,
					HEIGHT - timeLeft.getHeight() * 2);
		}
		if (contact.isCarrotTouched()) {
			audio.playSuperMusic();
		} else {
			audio.playMainMusic();
		}
	}

	private void drawToScreen(float delta) {
		Array<Sprite> cullSprites = cullSprites();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		background.draw(batch);
		playerSprite.draw(batch);
		for (Sprite sprite : cullSprites) {
			sprite.draw(batch);
		}
		batch.end();

		stage.act(delta);
		stage.draw();
	}
	
	private Array<Sprite> cullSprites() {
		Array<Sprite> cullSprites = new Array<Sprite>();
		cullSprites.clear();
		if (boulderSprite.getY() < topScreenEdge + 4
				&& boulderSprite.getY() > bottomScreenEdge
						- 4) {
			cullSprites.add(boulderSprite);
		}
		if (clockSprite.getY() < topScreenEdge + 4
				&& clockSprite.getY() > bottomScreenEdge
						- 4) {
			cullSprites.add(clockSprite);
		}
		if (carrotSprite.getY() < topScreenEdge + 4
				&& carrotSprite.getY() > bottomScreenEdge
						- 4) {
			cullSprites.add(carrotSprite);
		}
		for (Sprite platform : platformSprites) {
			if (platform.getY() < topScreenEdge + 4
					&& platform.getY() > bottomScreenEdge - 4) {
				cullSprites.add(platform);
			}
		}
		return cullSprites;
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

		platforms = new Platforms(world);
		platforms.createGroundWalls();
		platformArray = platforms.createPlatforms();
		platformSprites = platforms.initiseSprites();

		SpriteFactory spriteFactory = new SpriteFactory();
		boulder = new Boulder(world);
		boulderSprite = spriteFactory.createBoulder();
		clock = new Clock(world);
		clockSprite = spriteFactory.createClock();
		carrot = new Carrot(world);
		carrotSprite = spriteFactory.createCarrot();

		scoreTimeLabelSetUp();
	}

	private void scoreTimeLabelSetUp() {
		BitmapFont yellow = IceAspirations.getAssets().getManager()
				.get(Assets.YELLOW_FONT, BitmapFont.class);
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

	private void pauseButtonSetUp() {
		final ImageButton pause = new ButtonFactory().createImageButton(
				"Pause", "Play", 0, 0, false);
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
				audio.stopLowTimeMusic();
				audio.playMainMusic();
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
		prevTime = (int) allotedTime;
	}
}
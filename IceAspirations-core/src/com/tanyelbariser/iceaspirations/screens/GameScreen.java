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
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
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
		Running, Paused
	}

	State state = State.Running;

	private float approxFPS = 60.0f;
	private final float TIMESTEP = 1.0f / approxFPS;
	private final int VELOCITYITERATIONS = 8;
	private final int POSITIONITERATIONS = 3;
	private Platforms platforms;
	private Array<Sprite> platformSprites = new Array<Sprite>();
	private Sprite playerSprite;
	private Array<Body> platformArray;
	private float frameTime;
	private float allotedTime = 15;
	private Label timeLeft;
	private int maxHeight;
	private ImageTextButton quit;
	private BitmapFont blue;
	private LabelStyle yellowStyle;

	public GameScreen(IceAspirations iceA) {
		this.iceA = iceA;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if (state.equals(State.Running)) {
			if (camera.position.y > maxHeight) {
				maxHeight = (int) camera.position.y;
			}

			allotedTime -= delta;
			timeLeft.setText("Score: " + String.valueOf(maxHeight)
					+ "\nTime Limit: "
					+ String.valueOf(Math.round(allotedTime)));

			// adjustedDelta is delta rounded to nearest whole or half, i.e.
			// 60FPS = 1, 45FPS = 1.5, 30FPS = 2, etc. for a consistent delta
			// per device
			float adjustedDelta = Math.round(Math.round(approxFPS * delta) * 2) / 2.0f;

			world.setGravity(new Vector2(0, -9.81f * adjustedDelta
					* adjustedDelta));

			world.step(TIMESTEP, VELOCITYITERATIONS, POSITIONITERATIONS);

			// Repositions platform if out of camera/screen view
			float topScreenEdge = camera.position.y + camera.viewportHeight / 2;
			float bottomScreenEdge = camera.position.y - camera.viewportHeight
					/ 2;
			for (Body platform : platformArray) {
				if (platform.getPosition().y < bottomScreenEdge - 14) {
					platforms.repositionAbove(platform, topScreenEdge);
					continue;
				} else if (platform.getPosition().y > topScreenEdge + 14) {
					platforms.repositionBelow(platform, bottomScreenEdge);
					continue;
				}
			}

			// Player Updates
			player.update(adjustedDelta);
			if (player.getBody().getPosition().x > Platforms.RIGHT_SCREEN_EDGE) {
				player.getBody().setTransform(
						Platforms.RIGHT_SCREEN_EDGE - 0.5f,
						player.getBody().getPosition().y, 0);
			} else if (player.getBody().getPosition().x < Platforms.LEFT_SCREEN_EDGE) {
				player.getBody().setTransform(
						Platforms.LEFT_SCREEN_EDGE + 0.5f,
						player.getBody().getPosition().y, 0);
			}
			frameTime += delta;
			if (player.getBody().getLinearVelocity().y > 2) {
				Animation jumpAnimation = player.getjumpAnimation();
				playerSprite = (Sprite) jumpAnimation.getKeyFrame(frameTime,
						false);
			} else if (player.getBody().getLinearVelocity().y < -4
					& !player.isStanding()) {
				playerSprite = player.getFallingSprite();
				frameTime = 0;
			} else {
				playerSprite = stand;
				frameTime = 0;
			}

			boolean facingLeft = player.getFacingLeft()
					&& playerSprite.isFlipX();
			boolean facingRight = !player.getFacingLeft()
					&& !playerSprite.isFlipX();
			if (facingLeft) {
				// if the user is moving the player left and the playerSprite is
				// facing left then do nothing
			} else if (facingRight) {
				// if the user is moving the player right and the playerSprite
				// is
				// facing right then do nothing
			} else {// else flip player because he's facing the wrong way
				playerSprite.flip(true, false);
			}

			playerSprite.setPosition(player.getBody().getPosition().x
					- playerSprite.getWidth() / 2, player.getBody()
					.getPosition().y - playerSprite.getHeight() / 2);
			playerSprite.setRotation(player.getBody().getAngle()
					* MathUtils.radiansToDegrees);

			// Set camera position based on player position
			float playerY = player.getBody().getPosition().y;
			float highSpeed = 80;
			if (player.getBody().getLinearVelocity().y > highSpeed) {
				// High speed lag
			} else if (playerY > topScreenEdge) {
				camera.position.y += 1f;
			} else if (playerY > camera.position.y + 2f) {
				camera.position.y += 0.8f;
			} else if (playerY > camera.position.y + 1f) {
				camera.position.y += 0.3f;
			} else if (playerY > 0) {
				camera.position.y = playerY;
			} else {
				camera.position.y = 0;
			}

			// Position background at camera's position
			background.setPosition(camera.position.x - backgroundWidth / 2,
					camera.position.y - backgroundHeight / 2);

			camera.update();
		}
		if (allotedTime < 0) {
			iceA.setNextScreen(new GameOverScreen(iceA, maxHeight));
		} else if (allotedTime < 11) {
			if (IceAspirations.getMusic().isPlaying()) {
				IceAspirations.getMusic().stop();
			}
			if (!IceAspirations.getTimeOutMusic().isPlaying()) {
				IceAspirations.getTimeOutMusic().play();
				BitmapFont red = new BitmapFont(Gdx.files.internal("red.fnt"),
						false);
				LabelStyle redStyle = new LabelStyle(red, Color.RED);
				timeLeft.setStyle(redStyle);
				timeLeft.setPosition(timeLeft.getWidth() / 10, HEIGHT
						- timeLeft.getHeight() * 2);
			}
		} else if (!IceAspirations.getMusic().isPlaying()) {
			IceAspirations.getMusic().play();
			timeLeft.setStyle(yellowStyle);
			timeLeft.setPosition(timeLeft.getWidth() / 10,
					HEIGHT - timeLeft.getHeight() * 1.5f);
		}

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		background.draw(batch);
		playerSprite.draw(batch);
		for (Sprite platform : platformSprites) {
			platform.draw(batch);
		}
		batch.end();

		stage.act(delta);
		stage.draw();

		// physicsDebugger.render(world, camera.combined);
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

		float gravity = -9.81f;
		world = new World(new Vector2(0, gravity), true);
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

	// For some reason pause button suddenly doesn't work on desktop, but still
	// works on Android. Don't bother trying to fix this yet as I'll redo this
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
				if (!IceAspirations.getMusic().isPlaying()) {
					IceAspirations.getMusic().play();
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
		state = State.Paused;

	}

	@Override
	public void resume() {
		state = State.Running;
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
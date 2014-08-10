package com.tanyelbariser.iceaspirations.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
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
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.tanyelbariser.iceaspirations.IceAspirations;
import com.tanyelbariser.iceaspirations.entities.Player;
import com.tanyelbariser.iceaspirations.platforms.Platforms;
import com.tanyelbariser.iceaspirations.platforms.PlatformsFactory;

public class GameScreen implements Screen {
	IceAspirations iceA;
	SpriteBatch batch;
	Stage stage;
	ImageButton pause, back;
	static float phoneWidth = 768;
	public static float width = Gdx.graphics.getWidth();
	public static float height = Gdx.graphics.getHeight();
	static float compatibility = width / phoneWidth;
	private World world;
	private OrthographicCamera camera;
	private Box2DDebugRenderer physicsDebugger;
	private final Sprite background = new Sprite(new Texture("Background.png"));
	float backgroundWidth = background.getWidth();
	float backgroundHeight = background.getHeight();
	private Player player;
	public static final float ZOOM = 30f * compatibility;
	private Sprite stand;

	public enum State {
		Running, Paused
	}

	State state = State.Running;

	float approxFPS = 60.0f;
	final float TIMESTEP = 1.0f / approxFPS;
	final int VELOCITYITERATIONS = 8;
	final int POSITIONITERATIONS = 3;
	private Platforms platforms;
	Array<Sprite> platformSprites = new Array<Sprite>();
	private Sprite jumping;
	private Sprite playerSprite;
	private Array<Body> platformArray;
	private float frameTime;

	public GameScreen(IceAspirations iceA) {
		this.iceA = iceA;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if (state.equals(State.Running)) {
			Gdx.app.log("FRAMES PER SECOND", String.valueOf(1 / delta));
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
			if (player.body.getPosition().x > Platforms.RIGHT_SCREEN_EDGE) {
				player.body.setTransform(Platforms.RIGHT_SCREEN_EDGE - 0.5f,
						player.body.getPosition().y, 0);
			} else if (player.body.getPosition().x < Platforms.LEFT_SCREEN_EDGE) {
				player.body.setTransform(Platforms.LEFT_SCREEN_EDGE + 0.5f,
						player.body.getPosition().y, 0);
			}
			frameTime += delta;
			if (player.body.getLinearVelocity().y > 2) {
				Animation jumpAnimation = player.getjumpAnimation();
				playerSprite = (Sprite) jumpAnimation.getKeyFrame(frameTime,
						false);
			} else if (player.body.getLinearVelocity().y < -4 & !player.isStanding()) {
				playerSprite = player.getFallingSprite();
				frameTime = 0;
			} else {
				playerSprite = stand;
				frameTime = 0;
			}
			playerSprite.setPosition(
					player.body.getPosition().x - playerSprite.getWidth() / 2,
					player.body.getPosition().y - playerSprite.getHeight() / 2);
			playerSprite.setRotation(player.body.getAngle()
					* MathUtils.radiansToDegrees);

			// Set camera position based on player position
			float playerY = player.body.getPosition().y;
			float highSpeed = 80;
			if (player.body.getLinearVelocity().y > highSpeed) {
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
		Gdx.input.setInputProcessor(stage);

		background.setScale(1f / ZOOM * compatibility);

		pauseButtonSetUp();
		backButtonSetUp();

		float gravity = -9.81f;
		world = new World(new Vector2(0, gravity), true);
		physicsDebugger = new Box2DDebugRenderer();
		camera = new OrthographicCamera(width / ZOOM, height / ZOOM);

		player = new Player(world);
		Gdx.input.setInputProcessor(new InputMultiplexer(stage, player));
		stand = player.stand;
		jumping = player.jumping;

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
	}

	private void pauseButtonSetUp() {
		ImageButtonStyle imageStyle = new ImageButtonStyle();
		imageStyle.up = IceAspirations.skin.getDrawable("Pause");
		imageStyle.down = IceAspirations.skin.getDrawable("Play");
		imageStyle.checked = imageStyle.down;

		pause = new ImageButton(imageStyle);

		float pauseSize = width / 6;
		pause.setSize(pauseSize, pauseSize);

		pause.setPosition(width - pauseSize, height - pauseSize);

		pause.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				boolean checked = pause.isChecked();
				if (checked) {
					pause.setChecked(true);
					pause();
					stage.addActor(back);
				} else {
					pause.setChecked(false);
					resume();
					back.remove();
				}
			}
		});
		stage.addActor(pause);
	}

	private void backButtonSetUp() {
		ImageButtonStyle imageStyle = new ImageButtonStyle();
		imageStyle.up = IceAspirations.skin.getDrawable("Back");
		imageStyle.down = IceAspirations.skin.getDrawable("Back");

		back = new ImageButton(imageStyle);

		float backSize = width / 6;
		back.setSize(backSize, backSize);

		back.setPosition(0, 0);

		back.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
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
package com.tanyelbariser.iceaspirations.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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

	float approxFPS = 60;
	final float TIMESTEP = 1 / approxFPS;
	final int VELOCITYITERATIONS = 8;
	final int POSITIONITERATIONS = 3;
	private Platforms platforms;
	Array<Sprite> platformSprites = new Array<Sprite>();
	private Sprite jumping;
	private Sprite playerSprite;
	private ArrayList<Body> platformArray;

	public GameScreen(IceAspirations iceA) {
		this.iceA = iceA;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if (state.equals(State.Running)) {
			world.step(TIMESTEP, VELOCITYITERATIONS, POSITIONITERATIONS);
			
			//Player Updates
			if (player.body.getLinearVelocity().y > 2) {
				playerSprite = jumping;
			} else {
				playerSprite = stand;
			}
			player.update(delta);
			playerSprite.setPosition(
					player.body.getPosition().x - playerSprite.getWidth() / 2,
					player.body.getPosition().y - playerSprite.getHeight() / 2);
			playerSprite.setRotation(player.body.getAngle()
					* MathUtils.radiansToDegrees);

			//Set camera position based on player position
			float playerY = player.body.getPosition().y;
			if (playerY > 0) {
				camera.position.y = playerY;
			} else {
				camera.position.y = 0;
			}

			//Position background at camera's position
			background.setPosition(camera.position.x - backgroundWidth / 2,
					camera.position.y - backgroundHeight / 2);

			//Reposition platform if out of screen
			float topScreenEdge = camera.position.y + camera.viewportHeight / 2;
			float bottomScreenEdge = camera.position.y - camera.viewportHeight
					/ 2;
			// Repositions platform if out of camera/screen view
			Body bottomPlatform = platformArray.get(0);
			float bottomPlatformY = bottomPlatform.getPosition().y;
			// Replace platformArray.size() with a constant for performance
			Body topPlatform = platformArray.get(platformArray.size() - 1);
			float topPlatformY = topPlatform.getPosition().y;
			if (bottomPlatformY < bottomScreenEdge - 2) {
				platforms.repositionAbove(platformArray.remove(0),
						bottomPlatformY);
				platformArray.add(bottomPlatform);
			} else if (topPlatformY > topScreenEdge + 2) {
				platforms.repositionBelow(
						platformArray.remove(platformArray.size() - 1),
						topPlatformY);
				platformArray.add(0, topPlatform);
			}

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

		physicsDebugger.render(world, camera.combined);
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

		//Match sprite position to platform
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
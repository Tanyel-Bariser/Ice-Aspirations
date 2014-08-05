package com.tanyelbariser.iceaspirations.screens;

import com.badlogic.gdx.Gdx;
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
	private Sprite platformSprite;

	Array<Sprite> platformSprites = new Array<Sprite>();
	private Sprite jump;
	private Sprite playerSprite;

	public GameScreen(IceAspirations iceA) {
		this.iceA = iceA;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (player.standing) {
			playerSprite = stand;
		} else {
			playerSprite = jump;
		}
		
		if (state.equals(State.Running)) {
			world.step(TIMESTEP, VELOCITYITERATIONS, POSITIONITERATIONS);

			player.update(delta);

			float playerY = player.body.getPosition().y;
			if (playerY > 0) {
				camera.position.y = playerY;
			} else {
				camera.position.y = 0;
			}
			background.setPosition(camera.position.x - backgroundWidth / 2,
					camera.position.y - backgroundHeight / 2);
			playerSprite.setPosition(
					player.body.getPosition().x - playerSprite.getWidth() / 2,
					player.body.getPosition().y - playerSprite.getHeight() / 2);
			playerSprite.setRotation(player.body.getAngle()
					* MathUtils.radiansToDegrees);

			platformSprite = IceAspirations.skin.getSprite("Platform5");
			platforms.createPlatforms(camera.position.y + camera.viewportHeight
					/ 2, platformSprite);
//
//			Array<Body> tmpBodies = new Array<Body>();
//			world.getBodies(tmpBodies);
//			for (Body body : tmpBodies) {
//				if (body.getUserData() instanceof Sprite) {
//					Sprite sprite = (Sprite) body.getUserData();
//					sprite.setSize(3f, 1f);
//					sprite.setOrigin(sprite.getWidth() / 2,
//							sprite.getHeight() / 2);
//					sprite.setPosition(body.getPosition().x - sprite.getWidth()
//							/ 2, body.getPosition().y - sprite.getHeight() / 2);
//					sprite.setRotation(body.getAngle()
//							* MathUtils.radiansToDegrees);
//					platformSprites.add(sprite);
//				}
//			}

			camera.update();
		}

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		background.draw(batch);
		playerSprite.draw(batch);
//		for (Sprite platform : platformSprites) {
//			platform.draw(batch);
//		}
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
		stand = player.stand;
		jump = player.jump;

		platforms = new Platforms(world);
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
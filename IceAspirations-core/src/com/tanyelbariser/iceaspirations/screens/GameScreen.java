package com.tanyelbariser.iceaspirations.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.tanyelbariser.iceaspirations.IceAspirations;

public class GameScreen implements Screen {
	IceAspirations iceA;
	SpriteBatch batch;
	Stage stage;
	ImageButton pause, back;
	float width = Gdx.graphics.getWidth();
	float height = Gdx.graphics.getHeight();
	private World world;
	private OrthographicCamera camera;
	private Box2DDebugRenderer physicsDebugger;
	private BodyDef bodyDef;
	private FixtureDef fixDef;
	
	public enum State {
	    Running, Paused
	}
	State state = State.Running;

	public GameScreen(IceAspirations iceA) {
		this.iceA = iceA;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		float approxFPS = 60;
		final float TIMESTEP = 1/approxFPS;
		final int VELOCITYITERATIONS = 11;
		final int POSITIONITERATIONS = 4;
		
		if (state.equals(State.Running)) {
        	world.step(TIMESTEP, VELOCITYITERATIONS, POSITIONITERATIONS);
        	//future updates
		}
		
		batch.begin();
		IceAspirations.background.draw(batch);
		batch.end();

		stage.act(delta);
		stage.draw();
			
		physicsDebugger.render(world, camera.combined);
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		batch = new SpriteBatch();
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		pauseButtonSetUp();
		backButtonSetUp();
		
		float gravity = -9.81f;
		world = new World(new Vector2(0, gravity), true);
		physicsDebugger = new Box2DDebugRenderer();
		camera = new OrthographicCamera(width/25, height/25);
		
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(0, 1);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(0.5f, 1);
		
		fixDef = new FixtureDef();
		fixDef.shape = shape;
		fixDef.density = 3f;
		fixDef.friction = 0f;
		fixDef.restitution = 0f;
		
		world.createBody(bodyDef).createFixture(fixDef);
		
		
		
		
		
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(0, 0);

		// ground shape
		ChainShape groundShape = new ChainShape();
		Vector3 bottomLeft = new Vector3(0, Gdx.graphics.getHeight(), 0);
		Vector3 bottomRight = new Vector3(Gdx.graphics.getWidth(), bottomLeft.y, 0);
		camera.unproject(bottomLeft);
		camera.unproject(bottomRight);

		groundShape.createChain(new float[] {bottomLeft.x, bottomLeft.y, bottomRight.x, bottomRight.y});

		// fixture definition
		fixDef.shape = groundShape;
		fixDef.friction = .5f;
		fixDef.restitution = 0;

		Body ground = world.createBody(bodyDef);
		ground.createFixture(fixDef);
		
		groundShape.dispose();
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
	}
}
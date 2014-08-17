package com.tanyelbariser.iceaspirations.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.tanyelbariser.iceaspirations.IceAspirations;
import com.tanyelbariser.iceaspirations.factories.ButtonFactory;
import com.tanyelbariser.iceaspirations.factories.SpriteFactory;

public class HighScoresScreen implements Screen {
	private IceAspirations iceA;
	private final Preferences prefs = Gdx.app.getPreferences("IceAspirations");
	private SpriteBatch batch;
	private Stage stage;
	private Table table;
	private LabelStyle style;
	private Label heading;
	private final float WIDTH = Gdx.graphics.getWidth();
	private final float HEIGHT = Gdx.graphics.getHeight();
	private int[] highScores = new int[5];
	private int maxHeight = 0;
	private Sprite background;

	public HighScoresScreen(IceAspirations iceA, int maxHeight) {
		this.iceA = iceA;
		this.maxHeight = maxHeight;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		background.draw(batch);
		batch.end();

		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
	}

	public void newScore(int newScore) {
		for (int i = 0; i < 5; i++) {
			if (highScores[i] < newScore) {
				for (int j = 4; j > i; j--)
					highScores[j] = highScores[j - 1];
				highScores[i] = newScore;
				break;
			}
		}
		for (int i = 0; i < 5; i++) {
			String key ="HighScore" + String.valueOf(i);
			String value = String.valueOf(highScores[i]);
			prefs.putString(key, value);
		}
		prefs.flush();
	}

	@Override
	public void show() {
		batch = new SpriteBatch();
		background = SpriteFactory.createBackground();
		background.setSize(WIDTH, HEIGHT);

		for (int i = 0; i < 5; i++) {
			String key = "HighScore" + String.valueOf(i);
			if (prefs.contains(key)) {
				String value = prefs.getString(key);
				highScores[i] = Integer.parseInt(value);
			} else {
				prefs.putInteger(key, 0);
			}
		}
		
		newScore(maxHeight);

		String score1 = "1) " + prefs.getString("HighScore0");
		String score2 = "2) " + prefs.getString("HighScore1");
		String score3 = "3) " + prefs.getString("HighScore2");
		String score4 = "4) " + prefs.getString("HighScore3");
		String score5 = "5) " + prefs.getString("HighScore4");

		style = new LabelStyle(IceAspirations.getBlue(), Color.BLUE);
		heading = new Label("High Scores", style);

		Label points1 = new Label(score1, style);
		Label points2 = new Label(score2, style);
		Label points3 = new Label(score3, style);
		Label points4 = new Label(score4, style);
		Label points5 = new Label(score5, style);

		table = new Table();
		table.setBounds(0, 0, WIDTH, HEIGHT);
		table.add(heading).center().row();
		table.add(points1).row();
		table.add(points2).row();
		table.add(points3).row();
		table.add(points4).row();
		table.add(points5);

		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		stage.addActor(table);

		backButtonSetUp();
	}

	private void backButtonSetUp() {
		ImageButton back = ButtonFactory
				.createImageButton("Back", "Back", 0, 0, false);
		back.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				iceA.setScreen(new MainScreen(iceA));
			}
		});
		stage.addActor(back);
	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		batch.dispose();
		stage.dispose();
	}
}
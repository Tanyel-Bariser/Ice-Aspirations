package com.tanyelbariser.iceaspirations.factories;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.tanyelbariser.iceaspirations.Assets;
import com.tanyelbariser.iceaspirations.IceAspirations;

public class ButtonFactory {
	private Skin skin;
	private final static float WIDTH = Gdx.graphics.getWidth();

	public ButtonFactory() {
		skin = new Skin(IceAspirations.getAssets().getManager()
				.get(Assets.ATLAS, TextureAtlas.class));
	}

	public ImageButton createImageButton(String up, String down,
			float positionX, float positionY, boolean isChecked) {
		ImageButtonStyle imageStyle = new ImageButtonStyle();
		imageStyle.up = skin.getDrawable(up);
		imageStyle.down = skin.getDrawable(down);
		imageStyle.checked = imageStyle.down;

		ImageButton button = new ImageButton(imageStyle);
		button.setChecked(isChecked);
		float size = WIDTH / 6;
		button.setSize(size, size);
		button.setPosition(positionX, positionY);

		return button;
	}
}
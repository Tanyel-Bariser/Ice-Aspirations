package com.tanyelbariser.iceaspirations.factories;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.tanyelbariser.iceaspirations.Assets;

public class ButtonFactory {
	private static final Skin SKIN = new Skin(Assets.MANAGER.get(Assets.ATLAS,
			TextureAtlas.class));
	private final static float WIDTH = Gdx.graphics.getWidth();

	private ButtonFactory() {
	}

	public static ImageButton createImageButton(String up, String down,
			float positionX, float positionY, boolean isChecked) {
		ImageButtonStyle imageStyle = new ImageButtonStyle();
		imageStyle.up = SKIN.getDrawable(up);
		imageStyle.down = SKIN.getDrawable(down);
		imageStyle.checked = imageStyle.down;

		ImageButton button = new ImageButton(imageStyle);
		button.setChecked(isChecked);
		float size = WIDTH / 6;
		button.setSize(size, size);
		button.setPosition(positionX, positionY);
		
		return button;
	}
}
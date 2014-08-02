package com.tanyelbariser.iceaspirations.android;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.tanyelbariser.iceaspirations.IceAspirations;

public class AndroidLauncher extends AndroidApplication {
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.hideStatusBar = true;


		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			getWindow().getDecorView().setSystemUiVisibility(
					View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
			getWindow().getDecorView().setSystemUiVisibility(
					View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
			getWindow().getDecorView().setSystemUiVisibility(
					View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getWindow().getDecorView().setSystemUiVisibility(
					View.STATUS_BAR_VISIBLE);
			getWindow().getDecorView().setSystemUiVisibility(
					View.STATUS_BAR_HIDDEN);
		}

		initialize(new IceAspirations(), config);
	}

	@Override
	public void onBackPressed() {
		onPause();
		new AlertDialog.Builder(this)
				.setTitle("Confirm Exit")
				.setMessage("Do you want to quit the game?")
				.setNegativeButton(android.R.string.cancel,
						new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								onResume();
							}
						})
				.setPositiveButton(android.R.string.ok, new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						AndroidLauncher.super.onBackPressed();
					}
				}).create().show();
	}
}
package com.tanyelbariser.iceaspirations.android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.tanyelbariser.iceaspirations.IceAspirations;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new IceAspirations(), config);
	}
	@Override
	public void onBackPressed() {
		onPause();
	    new AlertDialog.Builder(this)
	        .setTitle("Confirm Exit")
	        .setMessage("Do you want to quit the game?")
	        .setNegativeButton(android.R.string.cancel, new OnClickListener() {
	        	@Override
	        	public void onClick(DialogInterface dialog, int which) {
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
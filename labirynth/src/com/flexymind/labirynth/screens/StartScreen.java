package com.flexymind.labirynth.screens;

import com.flexymind.labirynth.R;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class StartScreen extends Activity implements OnClickListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start);

		Display display = getWindowManager().getDefaultDisplay();

		ScreenSettings.GenerateSettings(display.getHeight(), display.getWidth());
		
		// Кнопка "Start"
		Button startButton = (Button) findViewById(R.id.StartButton);
		startButton.setOnClickListener(this);
		this.AutoSize(startButton);

		// Кнопка "Exit"
		Button exitButton = (Button) findViewById(R.id.ExitButton);
		exitButton.setOnClickListener(this);
		this.AutoSize(exitButton);

		// Кнопка "Settings"
		Button settingsButton = (Button) findViewById(R.id.SettingsButton);
		settingsButton.setOnClickListener(this);
		this.AutoSize(settingsButton);
	}

	/** Обработка нажатия кнопок */
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.StartButton: {
			Intent intent = new Intent();
			intent.setClass(this, GameScreen.class);
			startActivity(intent);
			break;
		}

		case R.id.SettingsButton: {
			break;
		}

		case R.id.ExitButton:
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	public void resize(double ScaleFactorX, double ScaleFactorY, Button button) {
		int height = button.getHeight();
		int width  = button.getWidth();

		height = (int) (ScaleFactorX * height);
		width  = (int) (ScaleFactorY * width);

		button.setHeight(height);
		button.setWidth(width);
	}

	private void AutoSize(Button button) {
		if (ScreenSettings.AutoScale()) {
			this.resize(ScreenSettings.ScaleFactorX(),
						ScreenSettings.ScaleFactorY(), button);
		}

	}

}
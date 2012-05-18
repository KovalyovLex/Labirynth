package com.flexymind.labirynth.screens.start;

import com.flexymind.labirynth.R;
import com.flexymind.labirynth.screen.choicelevel.ChoiceLevelScreen;
import com.flexymind.labirynth.screens.settings.ScreenSettings;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class StartScreen extends Activity implements OnClickListener {
	
	private final int ID_EXIT = 0;
	private final CharSequence MESSAGE= "Вы действительно хотите выйти?";
	private final CharSequence YES_ANSWER= "Да";
	private final CharSequence NO_ANSWER= "Нет";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start);

		Display display = getWindowManager().getDefaultDisplay();

		ScreenSettings.GenerateSettings(display.getWidth(), display.getHeight());
		
		// Кнопка "Start"
		Button startButton = (Button) findViewById(R.id.StartButton);
		startButton.setOnClickListener(this);
			
		// Кнопка "Exit"
		Button exitButton = (Button) findViewById(R.id.ExitButton);
		exitButton.setOnClickListener(this);
			
		// Кнопка "Settings"
		Button settingsButton = (Button) findViewById(R.id.SettingsButton);
		settingsButton.setOnClickListener(this);
		
		AutoSize();
	}

	/** Обработка нажатия кнопок */
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.StartButton: {
			Intent intent = new Intent();
			intent.setClass(this, ChoiceLevelScreen.class);
			startActivity(intent);
			break;
		}

		case R.id.SettingsButton: {
			break;
		}

		case R.id.ExitButton:
			showDialog(ID_EXIT);
			break;

		default:
			break;
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	
	protected Dialog onCreateDialog(int id){
		switch(id) {
		case ID_EXIT:{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(MESSAGE);
			builder.setPositiveButton(YES_ANSWER, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					finish();			
				}
			});
		
			builder.setNegativeButton(NO_ANSWER, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();			
				}
			});
			builder.setCancelable(false);
			return builder.create();
		}
			
		default:
			return null;
		}
	
	}
	public void AutoSize() {
		if (ScreenSettings.AutoScale()) {
			
			// высота и ширина кнопок на экране 480x800
			int height = 80;
			int width = 250;
			
			LinearLayout llayout = (LinearLayout)findViewById(R.id.buttLayout);
			
			RelativeLayout.LayoutParams llparams = (RelativeLayout.LayoutParams)llayout.getLayoutParams();
			llparams.topMargin		= (int)(ScreenSettings.ScaleFactorY() * llparams.topMargin);
			llparams.bottomMargin	= (int)(ScreenSettings.ScaleFactorY() * llparams.bottomMargin);
			llparams.leftMargin		= (int)(ScreenSettings.ScaleFactorX() * llparams.leftMargin);
			llparams.rightMargin	= (int)(ScreenSettings.ScaleFactorX() * llparams.rightMargin);
			
			RelativeLayout mainLayout = (RelativeLayout)findViewById(R.id.startmainlayout);
			
			// Кнопка "Start"
			Button startButton = (Button) findViewById(R.id.StartButton);
	
			// Кнопка "Exit"
			Button exitButton = (Button) findViewById(R.id.ExitButton);
	
			// Кнопка "Settings"
			Button settingsButton = (Button) findViewById(R.id.SettingsButton);
			
			// удаляем все кнопки
			llayout.removeAllViews();
			
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams( 	(int)(ScreenSettings.ScaleFactorX() * width), 
																					(int)(ScreenSettings.ScaleFactorY() * height) );
			
			llayout.addView(startButton, params);
			llayout.addView(settingsButton, params);
			llayout.addView(exitButton, params);
			
			// удаляем layout с кнопками
			mainLayout.removeAllViews();
			
			mainLayout.addView(llayout, llparams);
			
		}

	}

}
package com.flexymind.labirynth.screens.start;

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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.flexymind.labirynth.R;
import com.flexymind.labirynth.screens.choicelevel.ChoiceLevelScreen;
import com.flexymind.labirynth.screens.settings.SettingsScreen;
import com.flexymind.labirynth.storage.Settings;
import com.flexymind.labirynth.storage.SettingsStorage;

public class StartScreen extends Activity implements OnClickListener {
	
	public static final int ID_SETTINGSSCREEN = 1;
	public static final int ID_CHOISELEVELSCREEN = 2;
	public static final int ID_GAMESCREEN = 3;
	
	public static Activity startActivity = null;
	
	private final int ID_EXIT = 0;
	private final CharSequence MESSAGE		= "Вы действительно хотите выйти?";
	private final CharSequence YES_ANSWER	= "Да";
	private final CharSequence NO_ANSWER	= "Нет";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start);

		Display display = getWindowManager().getDefaultDisplay();
		
		Settings.GenerateSettings(display.getWidth(), display.getHeight());

		// Кнопка "Start"
		Button startButton = (Button) findViewById(R.id.StartButton);
		startButton.setOnClickListener(this);
			
		// Кнопка "Exit"
		Button exitButton = (Button) findViewById(R.id.ExitButton);
		exitButton.setOnClickListener(this);
			
		// Кнопка "Settings"
		Button settingsButton = (Button) findViewById(R.id.SettingsButton);
		settingsButton.setOnClickListener(this);
		
		SettingsStorage.initialize(this);
		
		AutoSize();
		
		startActivity = this;
	}

	/** Обработка нажатия кнопок */
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.StartButton: {
			Intent intent = new Intent();
			intent.setClass(this, ChoiceLevelScreen.class);
			startActivityForResult(intent, ID_CHOISELEVELSCREEN);
			break;
		}

		case R.id.SettingsButton: {
			Intent intent = new Intent();
			intent.setClass(this, SettingsScreen.class);
			startActivityForResult(intent, ID_SETTINGSSCREEN);
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

	@Override
	public void onBackPressed(){
		showDialog(ID_EXIT);
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
		if (Settings.getAutoScale()) {
			
			LinearLayout llayout = (LinearLayout)findViewById(R.id.buttLayout);
			
			RelativeLayout.LayoutParams llparams = (RelativeLayout.LayoutParams)llayout.getLayoutParams();
			llparams.topMargin		= (int)(Settings.getScaleFactorY() * llparams.topMargin);
			llparams.bottomMargin	= (int)(Settings.getScaleFactorY() * llparams.bottomMargin);
			llparams.leftMargin		= (int)(Settings.getScaleFactorX() * llparams.leftMargin);
			llparams.rightMargin	= (int)(Settings.getScaleFactorX() * llparams.rightMargin);
			
			RelativeLayout mainLayout = (RelativeLayout)findViewById(R.id.startmainlayout);
			
			// Кнопка "Start"
			Button startButton = (Button) findViewById(R.id.StartButton);
	
			// Кнопка "Exit"
			Button exitButton = (Button) findViewById(R.id.ExitButton);
	
			// Кнопка "Settings"
			Button settingsButton = (Button) findViewById(R.id.SettingsButton);
			
			// удаляем все кнопки
			llayout.removeAllViews();
			
			ViewGroup.LayoutParams buttonparams = (ViewGroup.LayoutParams)startButton.getLayoutParams();

			buttonparams.height = (int)(Settings.getScaleFactorY() * buttonparams.height);
			buttonparams.width  = (int)(Settings.getScaleFactorX() * buttonparams.width);

			llayout.addView(startButton, buttonparams);
			llayout.addView(settingsButton, buttonparams);
			llayout.addView(exitButton, buttonparams);
			
			// удаляем layout с кнопками
			mainLayout.removeAllViews();
			
			mainLayout.addView(llayout, llparams);
			
		}

	}

}
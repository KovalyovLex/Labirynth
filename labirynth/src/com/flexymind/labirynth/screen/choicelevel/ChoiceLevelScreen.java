package com.flexymind.labirynth.screen.choicelevel;

import com.flexymind.labirynth.R;
import com.flexymind.labirynth.screens.GameScreen;
import com.flexymind.labirynth.screens.settings.ScreenSettings;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class ChoiceLevelScreen extends Activity implements OnClickListener{
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choicelevel);

		Display display = getWindowManager().getDefaultDisplay();

		ScreenSettings.GenerateSettings(display.getWidth(), display.getHeight());
		
		// Кнопка "Start"
		Button startButton = (Button) findViewById(R.id.StartChosenButton);
		startButton.setOnClickListener(this);
		
		AutoSize();
	}
			
		/** Обработка нажатия кнопок */
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.StartChosenButton: {
				Intent intent = new Intent();
				intent.setClass(this, GameScreen.class);
				startActivity(intent);
				break;
			}
			
			default:
				break;
			}
		}
			
			public void onConfigurationChanged(Configuration newConfig) {
				super.onConfigurationChanged(newConfig);
			}
			
			public void AutoSize() {
				if (ScreenSettings.AutoScale()) {
					
					// высота и ширина кнопок на экране 480x800
					int height = 80;
					int width = 250;
					
					LinearLayout llayout = (LinearLayout)findViewById(R.id.buttonLayout);
					
					RelativeLayout.LayoutParams llparams = (RelativeLayout.LayoutParams)llayout.getLayoutParams();
					llparams.topMargin = (int)(ScreenSettings.ScaleFactorY() * llparams.topMargin);
					llparams.bottomMargin = (int)(ScreenSettings.ScaleFactorY() * llparams.bottomMargin);
					llparams.leftMargin = (int)(ScreenSettings.ScaleFactorX() * llparams.leftMargin);
					llparams.rightMargin = (int)(ScreenSettings.ScaleFactorX() * llparams.rightMargin);
					
					RelativeLayout mainLayout = (RelativeLayout)findViewById(R.id.choicemainlayout);
					
					// Кнопка "Start"
					Button startButton = (Button) findViewById(R.id.StartChosenButton);
			
					// удаляем все кнопки
					llayout.removeAllViews();	
					
					RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams( 	(int)(ScreenSettings.ScaleFactorX() * width), 
																							(int)(ScreenSettings.ScaleFactorY() * height) );
					
					llayout.addView(startButton, params);
					
					// удаляем layout с кнопками
					mainLayout.removeAllViews();
					
					mainLayout.addView(llayout, llparams);
					
				}

			}

}

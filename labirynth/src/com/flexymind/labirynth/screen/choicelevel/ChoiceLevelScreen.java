package com.flexymind.labirynth.screen.choicelevel;

import com.flexymind.labirynth.R;
import com.flexymind.labirynth.screens.GameScreen;
import com.flexymind.labirynth.screens.settings.ScreenSettings;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
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
					int heightButton = 80;
					int widthButton = 250;
					int heightImageBut = 160;
					int widthImageBut = 256;
					int newX;
			    	int newY;
					
					LinearLayout buttonLayout = (LinearLayout)findViewById(R.id.buttonLayout);
					LinearLayout imageLayout = (LinearLayout)findViewById(R.id.levelsLayout);
					
					RelativeLayout.LayoutParams butLLparams = (RelativeLayout.LayoutParams)buttonLayout.getLayoutParams();
					butLLparams.topMargin = (int)(ScreenSettings.ScaleFactorY() * butLLparams.topMargin);
					butLLparams.bottomMargin = (int)(ScreenSettings.ScaleFactorY() * butLLparams.bottomMargin);
					butLLparams.leftMargin = (int)(ScreenSettings.ScaleFactorX() * butLLparams.leftMargin);
					butLLparams.rightMargin = (int)(ScreenSettings.ScaleFactorX() * butLLparams.rightMargin);
					
					RelativeLayout.LayoutParams imageLLparams = (RelativeLayout.LayoutParams)imageLayout.getLayoutParams();
					imageLLparams.topMargin = (int)(ScreenSettings.ScaleFactorY() * imageLLparams.topMargin);
					imageLLparams.bottomMargin = (int)(ScreenSettings.ScaleFactorY() * imageLLparams.bottomMargin);
					imageLLparams.leftMargin = (int)(ScreenSettings.ScaleFactorX() * imageLLparams.leftMargin);
					imageLLparams.rightMargin = (int)(ScreenSettings.ScaleFactorX() * imageLLparams.rightMargin);
					
					//resizeLayout(buttonLayout);
					//resizeLayout(imageLayout);
					
					RelativeLayout mainLayout = (RelativeLayout)findViewById(R.id.choicemainlayout);
					
					// Кнопка "Start"
					Button startButton = (Button) findViewById(R.id.StartChosenButton);
			
					ImageButton level1ImageBut = (ImageButton)findViewById(R.id.imageLevel1);
					ImageButton level2ImageBut = (ImageButton)findViewById(R.id.imageLevel2);
					ImageButton level3ImageBut = (ImageButton)findViewById(R.id.imageLevel3);
					
					// удаляем все кнопки				
					buttonLayout.removeAllViews();	
					imageLayout.removeAllViews();	
					
					RelativeLayout.LayoutParams paramsButton = new RelativeLayout.LayoutParams( (int)(ScreenSettings.ScaleFactorX() * widthButton), 
																								(int)(ScreenSettings.ScaleFactorY() * heightButton) );
					RelativeLayout.LayoutParams paramsImageBut = new RelativeLayout.LayoutParams( (int)/*(ScreenSettings.ScaleFactorX() */ widthImageBut,
																								  (int)/*(ScreenSettings.ScaleFactorY() */ heightImageBut );
					
					Bitmap tmp=((BitmapDrawable)level1ImageBut.getDrawable()).getBitmap();
					newX = (int)(ScreenSettings.ScaleFactorX() * tmp.getWidth());
			    	newY = (int)(ScreenSettings.ScaleFactorY() * tmp.getHeight());
			    	Bitmap bmp = Bitmap.createScaledBitmap(tmp, newX, newY, true);
					level1ImageBut.setImageDrawable(new BitmapDrawable(bmp));
					
					tmp=((BitmapDrawable)level2ImageBut.getDrawable()).getBitmap();
			    	bmp = Bitmap.createScaledBitmap(tmp, newX, newY, true);
			    	level2ImageBut.setImageDrawable(new BitmapDrawable(bmp));
					
			    	tmp=((BitmapDrawable)level3ImageBut.getDrawable()).getBitmap();
			    	bmp = Bitmap.createScaledBitmap(tmp, newX, newY, true);
			    	level3ImageBut.setImageDrawable(new BitmapDrawable(bmp));
					
					
					buttonLayout.addView(startButton, paramsButton);
					imageLayout.addView(level1ImageBut, paramsImageBut);
					imageLayout.addView(level2ImageBut, paramsImageBut);
					imageLayout.addView(level3ImageBut, paramsImageBut);
					
					// удаляем layout с кнопками
					mainLayout.removeAllViews();
					
					mainLayout.addView(buttonLayout, butLLparams);
					mainLayout.addView(imageLayout, imageLLparams);
					
				}

			}
			
			public void resizeLayout(LinearLayout llayout){
				RelativeLayout.LayoutParams llparams = (RelativeLayout.LayoutParams)llayout.getLayoutParams();
				llparams.topMargin = (int)(ScreenSettings.ScaleFactorY() * llparams.topMargin);
				llparams.bottomMargin = (int)(ScreenSettings.ScaleFactorY() * llparams.bottomMargin);
				llparams.leftMargin = (int)(ScreenSettings.ScaleFactorX() * llparams.leftMargin);
				llparams.rightMargin = (int)(ScreenSettings.ScaleFactorX() * llparams.rightMargin);
			}

}

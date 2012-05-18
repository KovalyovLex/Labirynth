package com.flexymind.labirynth.screen.choicelevel;

import java.util.Vector;

import com.flexymind.labirynth.R;
import com.flexymind.labirynth.screens.GameScreen;
import com.flexymind.labirynth.screens.settings.ScreenSettings;
import com.flexymind.labirynth.storage.LevelStorage;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class ChoiceLevelScreen extends Activity implements OnClickListener{

	private static final int id = 2376;
	private Vector<String> names = null;
	private LevelStorage lvlstor = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choicelevel);

		Display display = getWindowManager().getDefaultDisplay();

		ScreenSettings.GenerateSettings(display.getWidth(), display.getHeight());
		
		lvlstor = new LevelStorage(this);
		
		names = lvlstor.get_level_names();
		
		addButtons();
	}
			
	/** Обработка нажатия кнопок */
	public void onClick(View v) {
		int buttnum = v.getId() - id;
		
		Intent intent = new Intent(this, GameScreen.class);
		Bundle bundle = new Bundle();
		bundle.putString(GameScreen.LEVELNAME, names.elementAt(buttnum));
		intent.putExtras(bundle);
		intent.setAction(GameScreen.LEVELCHOOSEACTION);
		startActivity(intent);
	}
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
		
	public void addButtons() {
		// высота и ширина кнопок на экране 480x800
		int heightImageBut = 220;
		int widthImageBut = 220;
		
		LinearLayout levelslay = (LinearLayout)findViewById(R.id.levelsLayout);
		
		TextView newnameOfLvl;
		ImageButton newbutton;
		LinearLayout newbulllay;
		
		levelslay.removeAllViews();
	    
		if (ScreenSettings.AutoScale()) {
			heightImageBut = (int)(heightImageBut * ScreenSettings.ScaleFactorY());
			widthImageBut = (int)(widthImageBut * ScreenSettings.ScaleFactorX());
			
			HorizontalScrollView hscroll = (HorizontalScrollView)findViewById(R.id.horizontalScrollView1);
			
			RelativeLayout parent = (RelativeLayout)hscroll.getParent();
			
			parent.removeAllViews();
			
			RelativeLayout.LayoutParams rlparams = (RelativeLayout.LayoutParams)hscroll.getLayoutParams();
			rlparams.topMargin = (int)(ScreenSettings.ScaleFactorY() * rlparams.topMargin);
			rlparams.bottomMargin = (int)(ScreenSettings.ScaleFactorY() * rlparams.bottomMargin);
			rlparams.leftMargin = (int)(ScreenSettings.ScaleFactorX() * rlparams.leftMargin);
			rlparams.rightMargin = (int)(ScreenSettings.ScaleFactorX() * rlparams.rightMargin);
			rlparams.height = (int)(ScreenSettings.ScaleFactorY() * rlparams.height);
			rlparams.width = (int)(ScreenSettings.ScaleFactorX() * rlparams.width);
			
			parent.addView(hscroll,rlparams);
		}

		for (int i = 0; i < names.size(); i++){
			newnameOfLvl = new TextView(this);
			newbutton = new ImageButton(this);
			newbulllay = new LinearLayout(this);
			
			newbulllay.setOrientation(LinearLayout.VERTICAL);
			
			newbutton.setEnabled(lvlstor.isFree(names.get(i)));
			newbutton.setId(id + i);
			newbutton.setOnClickListener(this);
			newbutton.setBackgroundDrawable(lvlstor.getPrevPictireByName(names.get(i)));
			
			RelativeLayout.LayoutParams buttparams = new RelativeLayout.LayoutParams(widthImageBut, heightImageBut);
			
			newnameOfLvl.setText(names.get(i));
			newnameOfLvl.setGravity(Gravity.CENTER_HORIZONTAL);
			
			newbulllay.removeAllViews();
			newbulllay.addView(newnameOfLvl);
			newbulllay.addView(newbutton,buttparams);
			
			levelslay.addView(newbulllay);
		}
	}

}

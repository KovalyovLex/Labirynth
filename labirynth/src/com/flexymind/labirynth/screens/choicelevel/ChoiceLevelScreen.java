package com.flexymind.labirynth.screens.choicelevel;

import java.util.Vector;

import com.flexymind.labirynth.R;
import com.flexymind.labirynth.screens.GameScreen;
import com.flexymind.labirynth.storage.LevelStorage;
import com.flexymind.labirynth.storage.Settings;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class ChoiceLevelScreen extends Activity implements OnClickListener{

	private static final int id = 2376;
	private Vector<String> names = null;
	private LevelStorage lvlstor = null;
	private Vector<Boolean> access = new Vector<Boolean>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choicelevel);

		Display display = getWindowManager().getDefaultDisplay();

		Settings.GenerateSettings(display.getWidth(), display.getHeight());
		
		lvlstor = new LevelStorage(this);
		
		names = lvlstor.getLevelNames();
		
		addButtons();
	}
	
	public void onClick(View v) {
		
		int buttnum = v.getId() - id;
		if (access.get(buttnum))
		{
			Intent intent = new Intent(this, GameScreen.class);
			Bundle bundle = new Bundle();
			bundle.putString(GameScreen.LEVELNAME, names.elementAt(buttnum));
			intent.putExtras(bundle);
			intent.setAction(GameScreen.LEVELCHOOSEACTION);
			startActivity(intent);
		} else {
			Toast toast = Toast.makeText(getApplicationContext(), 
					getApplicationContext().getString(R.string.blockedLevel), 
					Toast.LENGTH_LONG);
	        toast.setGravity(Gravity.CENTER, 0, 0);
	        LinearLayout toastView = (LinearLayout) toast.getView();
	        ImageView imageAccess = new ImageView(getApplicationContext());
	        Drawable image = v.getResources().getDrawable(R.drawable.freefalse);
	        autoScale(image);
	        imageAccess.setImageDrawable(image);
	        toastView.addView(imageAccess, 0);
	        toast.show();
		}
	}
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
		
	public void addButtons() {
		
		LinearLayout levelslay = (LinearLayout)findViewById(R.id.levelsLayout);
		
		TextView newnameOfLvl;
		ImageButton newbutton;
		LinearLayout newbulllay;

		ImageButton startButton = (ImageButton) findViewById(R.id.imageButtonChoose);
		
		ViewGroup.LayoutParams buttonparams = (ViewGroup.LayoutParams)startButton.getLayoutParams();
		
		if (Settings.getAutoScale()) {
			
			buttonparams.height = (int)(Settings.getScaleFactorY() * buttonparams.height);
			buttonparams.width  = (int)(Settings.getScaleFactorX() * buttonparams.width);
			
			HorizontalScrollView hscroll = (HorizontalScrollView)findViewById(R.id.horizontalScrollView1);
			
			RelativeLayout parent = (RelativeLayout)hscroll.getParent();
			
			parent.removeAllViews();
			
			RelativeLayout.LayoutParams rlparams = (RelativeLayout.LayoutParams)hscroll.getLayoutParams();
			rlparams.topMargin = (int)(Settings.getScaleFactorY() * rlparams.topMargin);
			rlparams.bottomMargin = (int)(Settings.getScaleFactorY() * rlparams.bottomMargin);
			rlparams.leftMargin = (int)(Settings.getScaleFactorX() * rlparams.leftMargin);
			rlparams.rightMargin = (int)(Settings.getScaleFactorX() * rlparams.rightMargin);
			rlparams.height = (int)(Settings.getScaleFactorY() * rlparams.height);
			rlparams.width = (int)(Settings.getScaleFactorX() * rlparams.width);
			
			parent.addView(hscroll,rlparams);
		}

		levelslay.removeAllViews();
		
		for (int i = 0; i < names.size(); i++){
			newnameOfLvl = new TextView(this);
			newbutton = new ImageButton(this);
			newbulllay = new LinearLayout(this);
			
			newbulllay.setOrientation(LinearLayout.VERTICAL);
			
			access.addElement(lvlstor.isFree(names.get(i)));
			newbutton.setId(id + i);
			newbutton.setOnClickListener(this);
			newbutton.setBackgroundDrawable(lvlstor.getPrevPictireByName(names.get(i)));
			
			newnameOfLvl.setText(names.get(i));
			newnameOfLvl.setGravity(Gravity.CENTER_HORIZONTAL);
			
			newbulllay.removeAllViews();
			newbulllay.addView(newnameOfLvl);
			newbulllay.addView(newbutton,buttonparams);
			
			levelslay.addView(newbulllay);
		}
	}
	
	private void autoScale(Drawable image){
		int Width = image.getIntrinsicWidth();
        int Height = image.getIntrinsicHeight();
    	int newX = (int)(Settings.getScaleFactorX() * Width);
    	int newY = (int)(Settings.getScaleFactorY() * Height);
    	Bitmap bmp = ((BitmapDrawable)image).getBitmap();
    	Bitmap tmp = Bitmap.createScaledBitmap(bmp, newX, newY, true);
        bmp = tmp;
        image = new BitmapDrawable(bmp);		
	}

}

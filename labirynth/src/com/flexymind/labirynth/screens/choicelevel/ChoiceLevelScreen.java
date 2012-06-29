package com.flexymind.labirynth.screens.choicelevel;

import java.util.Vector;

import com.flexymind.labirynth.R;
import com.flexymind.labirynth.screens.game.LoadingScreen;
import com.flexymind.labirynth.screens.start.StartScreen;
import com.flexymind.labirynth.storage.LevelStorage;
import com.flexymind.labirynth.storage.ScoreStorage;
import com.flexymind.labirynth.storage.Settings;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
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

		Display display = getWindowManager().getDefaultDisplay();

		Settings.GenerateSettings(display.getWidth(), display.getHeight());
		
		lvlstor = new LevelStorage(this);
		
		names = lvlstor.getLevelNames();
		
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
	}
	
	protected void onResume(){
		super.onResume();
		access.clear();
		setContentView(R.layout.choicelevel);
		addButtons();
	}
	
	public void onClick(View v) {
		
		int buttnum = v.getId() - id;
		if (access.get(buttnum))
		{
			Intent intent = new Intent(this, LoadingScreen.class);
			Bundle bundle = new Bundle();
			bundle.putInt(LoadingScreen.LEVELID, buttnum);
			intent.putExtras(bundle);
			intent.setAction(LoadingScreen.LEVELCHOOSEACTION);
			if (StartScreen.startActivity != null){
				StartScreen.startActivity.startActivityForResult(intent, StartScreen.ID_GAMESCREEN);
			}
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
		ViewGroup.LayoutParams levelparams = levelslay.getLayoutParams();
		
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
		
		ScoreStorage scorstor = new ScoreStorage(this);
		
		for (int i = 0; i < names.size(); i++){
			newnameOfLvl = new TextView(this);
			newbutton = new ImageButton(this);
			newbulllay = new LinearLayout(this);
			
			newbulllay.setOrientation(LinearLayout.VERTICAL);
			
			Bitmap bOriginal = ((BitmapDrawable)lvlstor.getPrevPictireByName(names.get(i))).getBitmap();
			Bitmap miniature = bOriginal.copy(Bitmap.Config.ARGB_8888, true);
			
			Canvas canv = new Canvas(miniature);
			ScoreStorage scorStor = new ScoreStorage(this);
			Drawable text = null;
			if (scorStor.isOpen(i)){
				// draw 1 or 2 or 3 or null stars
				if (scorStor.getNumOfStars(i) == 1){
					// draw 1 star
					text = getResources().getDrawable(R.drawable.level1star);
					text.setBounds(	0,
									0,
									miniature.getWidth(),
									miniature.getHeight());
					text.draw(canv);
				}
				if (scorStor.getNumOfStars(i) == 2){
					// draw 2 star
					text = getResources().getDrawable(R.drawable.level2star);
					text.setBounds(	0,
									0,
									miniature.getWidth(),
									miniature.getHeight());
					text.draw(canv);
				}
				if (scorStor.getNumOfStars(i) == 3){
					// draw 3 star
					text = getResources().getDrawable(R.drawable.level3star);
					text.setBounds(	0,
									0,
									miniature.getWidth(),
									miniature.getHeight());
					text.draw(canv);
				}
			}else{
				// draw locked texture
				text = getResources().getDrawable(R.drawable.levelblocked);
				text.setBounds(	0,
								0,
								miniature.getWidth(),
								miniature.getHeight());
				text.draw(canv);
			}
			
			access.addElement(scorstor.isOpen(i));
			newbutton.setId(id + i);
			newbutton.setOnClickListener(this);
			newbutton.setBackgroundDrawable(new BitmapDrawable(miniature));
			
			newnameOfLvl.setText(names.get(i));
			newnameOfLvl.setGravity(Gravity.CENTER_HORIZONTAL);
			
			newbulllay.removeAllViews();
			newbulllay.addView(newnameOfLvl);
			newbulllay.addView(newbutton,buttonparams);
			
			levelslay.addView(newbulllay, levelparams);
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

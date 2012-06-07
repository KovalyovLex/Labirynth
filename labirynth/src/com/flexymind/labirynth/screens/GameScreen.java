package com.flexymind.labirynth.screens;


import com.flexymind.labirynth.R;
import com.flexymind.labirynth.storage.LevelStorage;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.FrameLayout;

public class GameScreen extends Activity {
    /** Called when the activity is first created. */
	
	public static final String LEVELNAME = "name of level";
	public static final String LEVELID = "id of level";
	public static final String LEVELCHOOSEACTION = "levelchoose Action";
	
	GameView gameView;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        if (getIntent() == null){
        	finish();
        	return;
        }
        
        setContentView(R.layout.main);
        
        FrameLayout rlay = (FrameLayout)findViewById(R.id.gameLayout);
        
        gameView = (GameView)rlay.getChildAt(0);
        
        LevelStorage lvlstor = new LevelStorage(this);
        
        Bundle b = getIntent().getExtras();
        if (   LEVELCHOOSEACTION.equals(getIntent().getAction()) 
        	&& b != null 
        	&& b.containsKey(LEVELNAME)) {
        	
        	gameView.setGameLevel(lvlstor.loadGameLevelbyName(b.getString(LEVELNAME)));
        }
    }
	
	@Override
    protected void onPause() {
		gameView.onPause();
		super.onPause();
	}

	
	@Override
    protected void onResume() {
		gameView.onResume();
		super.onResume();
	}
        
    @Override
    public void onConfigurationChanged(Configuration newConfig) { 
    	super.onConfigurationChanged(newConfig);
    }

}

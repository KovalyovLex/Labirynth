package com.flexymind.labirynth.screens;


import com.flexymind.labirynth.R;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.FrameLayout;

public class GameScreen extends Activity {
    /** Called when the activity is first created. */
	
	GameView gameView;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        
        FrameLayout rlay = (FrameLayout)findViewById(R.id.gameLayout);
        
        gameView = (GameView)rlay.getChildAt(0);
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
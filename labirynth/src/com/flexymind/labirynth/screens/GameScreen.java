package com.flexymind.labirynth.screens;


import com.flexymind.labirynth.R;
import com.flexymind.labirynth.objects.Ball;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Display;

public class GameScreen extends Activity {
    /** Called when the activity is first created. */
    
	public Display display;
    public int ScreenHeight;
    public int ScreenWidth;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Display display = getWindowManager().getDefaultDisplay();
        final int ScreenHeight = display.getHeight();
    	final int ScreenWidth = display.getWidth();
    	ScreenSettings.GenerateSettings(ScreenWidth, ScreenHeight);
    }
	
	@Override
    protected void onStop() {
		Ball.unregisterListeners();
		super.onStop();
	}
	
	@Override
    protected void onResume() {
		Ball.registerListeners();
		super.onStop();
	}
        
    @Override
    public void onConfigurationChanged(Configuration newConfig) { 
    	super.onConfigurationChanged(newConfig);
    }
}
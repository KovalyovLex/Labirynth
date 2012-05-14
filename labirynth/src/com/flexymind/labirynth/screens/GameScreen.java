package com.flexymind.labirynth.screens;


import com.flexymind.labirynth.R;
import com.flexymind.labirynth.objects.Ball;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;

public class GameScreen extends Activity {
    /** Called when the activity is first created. */
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
	
	@Override
    protected void onStop() {
		Ball.unregisterListeners();
		super.onStop();
	}
	
	@Override
    protected void onResume() {
		Ball.registerListeners();
		super.onResume();
	}
        
    @Override
    public void onConfigurationChanged(Configuration newConfig) { 
    	super.onConfigurationChanged(newConfig);
    }
}
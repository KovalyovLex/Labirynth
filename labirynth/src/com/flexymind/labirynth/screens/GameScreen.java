package com.flexymind.labirynth.screens;

import com.android.pingpong.R;
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
    public void onConfigurationChanged(Configuration newConfig) { }
}
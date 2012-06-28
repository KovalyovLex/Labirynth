package com.flexymind.labirynth.screens.game;

import com.flexymind.labirynth.R;
import com.flexymind.labirynth.screens.game.OpenGL.GLGameView;

import android.app.Activity;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.Bundle;
import android.widget.FrameLayout;

public class GameScreen extends Activity {
    /** Called when the activity is first created. */
	
	private GLGameView gameView = null;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        
        FrameLayout rlay = (FrameLayout)findViewById(R.id.gameLayout);
        
        gameView = (GLGameView)rlay.getChildAt(0);
        
        if (LoadingScreen.gameID != LoadingScreen.NULLID){
            gameView.setGameLevelID(LoadingScreen.gameID);
        }
        
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }
	
	@Override
    protected void onPause() {
		if (gameView != null){
			gameView.onPause();
		}
		super.onPause();
	}

	
	@Override
    protected void onResume() {
		if (gameView != null){
			gameView.onResume();
		}
		super.onResume();
	}
    
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
	
	protected void onStop(){
		if (gameView != null){
			gameView.onDestroy();
			gameView = null;
		}
		super.onStop();
	}
	
    @Override
    public void onConfigurationChanged(Configuration newConfig) { 
    	super.onConfigurationChanged(newConfig);
    }

}

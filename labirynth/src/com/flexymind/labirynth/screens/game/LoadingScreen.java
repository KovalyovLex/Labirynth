package com.flexymind.labirynth.screens.game;

import com.flexymind.labirynth.R;
import com.flexymind.labirynth.screens.start.StartScreen;
import com.flexymind.labirynth.storage.LevelStorage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class LoadingScreen extends Activity{

	public static final String LEVELID = "id of level";
	public static final String LEVELCHOOSEACTION = "levelchoose Action";
	
	protected static final int NULLID = -666;
	protected static int gameID = NULLID;
	private static Context context = null;
	private static final int SPLASH_TIME = 1000; // 1 sec
	
	private SplashTask loader = null;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        
        setContentView(R.layout.loadingsplash);
        
        Bundle b = getIntent().getExtras();
        if (   LoadingScreen.LEVELCHOOSEACTION.equals(getIntent().getAction()) 
        	&& b != null 
        	&& b.containsKey(LoadingScreen.LEVELID)) {
        	gameID = b.getInt(LoadingScreen.LEVELID);
        }
        
        if (gameID == NULLID){
        	this.finish();
        	Log.e("LoadingScreen","Not known gameID");
        }
        
        context = this.getApplicationContext();
        
        loader = new SplashTask();
		loader.execute();
	}
	
	public void onBackPressed(){
		super.onBackPressed();
		finish();
		if (loader.getStatus().equals(AsyncTask.Status.RUNNING)){
			loader.cancel(true);
		}
	}
	
    /**
     * возвращает ID последнего запущенного уровня
     * @return ID
     */
    public static int getLastGameID(){
    	return LoadingScreen.gameID;
    }
    
    /**
     * Перезапускает уровень игры
     */
    public static void restartLevel(){
    	if (gameID != NULLID){
    		if(StartScreen.startActivity != null){
    			StartScreen.startActivity.finishActivity(StartScreen.ID_GAMESCREEN);
    			if (context != null){
    				Intent intent = new Intent();
        			intent.setClass(context, LoadingScreen.class);
        			StartScreen.startActivity.startActivityForResult(intent, StartScreen.ID_GAMESCREEN);
    			}
    		}
    	}
    }
    
    /**
     * Запускает следующий уровень в игре, если был запущен последний, то будет запущен первый
     */
    public static void startNextLevel(){
    	if (gameID != NULLID){
    		gameID++;
    		if (gameID >= LevelStorage.getNumOfLevels()){
    			gameID = 0;
    		}
    		if(StartScreen.startActivity != null){
    			StartScreen.startActivity.finishActivity(StartScreen.ID_GAMESCREEN);
    			if (context != null){
    				Intent intent = new Intent();
        			intent.setClass(context, LoadingScreen.class);
        			StartScreen.startActivity.startActivityForResult(intent, StartScreen.ID_GAMESCREEN);
    			}
    		}
    	}
    }
    
    private class SplashTask extends AsyncTask<Void,Void,Void>{
		
		@Override
		protected Void doInBackground(Void... params) {
	        try {
				Thread.sleep(SPLASH_TIME);
			} catch (InterruptedException e) {
				Log.e("LoadingScreen","can't load game");
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			finish();
			startActivity(new Intent(getApplicationContext(), GameScreen.class));
		}
	}
}

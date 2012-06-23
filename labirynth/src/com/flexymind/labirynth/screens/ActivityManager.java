package com.flexymind.labirynth.screens;

import java.util.ArrayList;

import com.flexymind.labirynth.screens.choicelevel.ChoiceLevelScreen;
import com.flexymind.labirynth.screens.game.GameScreen;
import com.flexymind.labirynth.screens.settings.SettingsScreen;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class ActivityManager {

	public static final String SETTINGS		= "settingsActivity";
	public static final String CHOISELEVEL	= "choiseLevelActivity";
	public static final String GAME			= "gameActivity";
	
	
	private static LocalActivityManager manager = null;
	private static Context context = null;
	private static ActivityGroup baseActivity = null;
	private static ArrayList<String> mIdList;

	/**
	 * Initialize ActivityManager
	 * @param base - main activity
	 */
	public static void Initialize(ActivityGroup base){
		baseActivity = base;
		manager = base.getLocalActivityManager();
		context = base.getApplicationContext();
		mIdList = new ArrayList<String>();
	}
	
	/** 
	 * return activity, that currently running
	 * @return running activity
	 */
	public static Activity getRunningActivity(){
		if (manager != null){
			return manager.getCurrentActivity();
		}
		return null;
	}
	
	/**
	 * start settings activity
	 */
	public static void runSettingsActivity(){
		if (manager != null){
			Intent intent = new Intent();
			intent.setClass(context, SettingsScreen.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			Window newWindow = manager.startActivity(SETTINGS, intent);
			mIdList.add(SETTINGS);
			baseActivity.setContentView(newWindow.getDecorView());
		}
	}
	
	/**
	 * start choiselevel activity
	 */
	public static void runChoiseLevelActivity(){
		if (manager != null){
			Intent intent = new Intent();
			intent.setClass(context, ChoiceLevelScreen.class);
			Window newWindow = manager.startActivity(CHOISELEVEL, intent);
			mIdList.add(CHOISELEVEL);
			baseActivity.setContentView(newWindow.getDecorView());
		}
	}
	
	/**
	 * run game activity with no id of game
	 */
	public static void runGameActivity(){
		if (manager != null){
			Intent intent = new Intent();
			intent.setClass(context, GameScreen.class);
			Window newWindow = manager.startActivity(GAME, intent);
			mIdList.add(GAME);
			baseActivity.setContentView(newWindow.getDecorView());
		}
	}
	
	/**
	 * run game activity with id
	 * @param id - ID of level
	 */
	public static void runGameActivity(int id){
		if (manager != null){
			Intent intent = new Intent();
			intent.setClass(context, GameScreen.class);
			Bundle bundle = new Bundle();
			bundle.putInt(GameScreen.LEVELID, id);
			intent.putExtras(bundle);
			intent.setAction(GameScreen.LEVELCHOOSEACTION);
			Window newWindow = manager.startActivity(GAME, intent);
			mIdList.add(GAME);
			baseActivity.setContentView(newWindow.getDecorView());
		}
	}
	
	/**
	 * go to back activity
	 */
	public static void onBackPressed(){
		int index = mIdList.size() - 1;
		if (index > 0){
			manager.getActivity(mIdList.get(index)).finish();
			
			Intent lastIntent = manager.getActivity(mIdList.get(index - 1)).getIntent();
			Window newWindow = manager.startActivity(mIdList.get(index - 1), lastIntent);
			baseActivity.setContentView(newWindow.getDecorView());
			
			manager.destroyActivity(mIdList.get(index), true);
			mIdList.remove(index);
		}else{
			if (index == 0){
				//manager.getActivity(mIdList.get(index)).finish();
				manager.destroyActivity(mIdList.get(index), true);
				mIdList.remove(index);
				Window newWindow = manager.startActivity("main", baseActivity.getIntent());
				baseActivity.setContentView(newWindow.getDecorView());
			}else{
				baseActivity.finish();
			}
		}
	}
}

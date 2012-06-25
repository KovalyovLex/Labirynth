package com.flexymind.labirynth.storage;

import android.content.Context;
import android.content.SharedPreferences;

public class ScoreStorage {
	//////////////////////////////////////////////////
	/////		Константы для настроек			//////
	//////////////////////////////////////////////////
	private static final String STORAGE		= "Labyrinth scores";
	private static final String LASTGAME	= "lastGameID";
	private static final String SCORE		= "gameScore";
	private static final String STARS		= "StarS";
	
	private static Context context = null;
	private static SharedPreferences settings = null;
	
	public ScoreStorage(Context initContext){
		context = initContext;
		settings = context.getSharedPreferences(STORAGE, 0);
	}
	
	/** Возвращает ID уровня, пройденного в последний раз (-1 если не пройдено ни одного) */
	public int getLastId(){
		return settings.getInt(LASTGAME, -1);
	}
	
	/** Сохраняет ID уровня, пройденного в последний раз */
	public void saveLastId(int lastID){
		SharedPreferences.Editor edit = settings.edit();
		edit.putInt(LASTGAME, lastID);
		edit.commit();
	}
	
	/**
	 * Возвращает доступность уровня для игры
	 * @param levelID
	 * @return true если доступен
	 */
	public boolean isOpen(int levelID){
		int last = getLastId() + 1;
		return last >= levelID;
	}
	
	/**
	 * Сохраняет колическтво заработанных очков
	 * @param score - колическтво очков
	 * @param levelId - номер уровня
	 */
	public void saveScore(int score, int levelId){
		String scorename = SCORE + Integer.toString(levelId);
		SharedPreferences.Editor edit = settings.edit();
		edit.putInt(scorename, score);
		edit.commit();
	}
	
	/**
	 * Сохраняет колическтво заработанных очков
	 * @param stars - колическтво очков
	 * @param levelId - номер уровня
	 */
	public void saveNumOfStars(int stars, int levelId){
		String starsname = STARS + Integer.toString(levelId);
		SharedPreferences.Editor edit = settings.edit();
		edit.putInt(starsname, stars);
		edit.commit();
	}
	
	/**
	 * Возвращает колическтво звёзд, заработанных за уровень
	 * @param levelId - id уровня
	 * @return 0 если информации о заработанных звездах нет
	 */
	public int getNumOfStars(int levelId){
		String starsname = STARS + Integer.toString(levelId);
		return settings.getInt(starsname, 0);
	}
	
	/**
	 * Возвращает колическтво очков, заработанных за уровень
	 * @param levelId - id уровня
	 * @return 0 если информации о заработанных очках нет
	 */
	public int getScore(int levelId){
		String scorename = SCORE + Integer.toString(levelId);
		return settings.getInt(scorename, 0);
	}
}

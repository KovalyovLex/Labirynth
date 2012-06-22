package com.flexymind.labirynth.storage;

import android.content.Context;
import android.content.SharedPreferences;

public class ScoreStorage {
	//////////////////////////////////////////////////
	/////		Константы для настроек			//////
	//////////////////////////////////////////////////
	private static final String STORAGE = "Labyrinth scores";
	private static final String LASTGAME = "lastGameID";
	private static final String SCORE = "gameScore";
	
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
	public void seveScore(int score, int levelId){
		String scorename = SCORE + Integer.toString(levelId);
		SharedPreferences.Editor edit = settings.edit();
		edit.putInt(scorename, score);
		edit.commit();
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

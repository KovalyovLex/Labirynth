package com.flexymind.labirynth.storage;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsStorage {
	
	//////////////////////////////////////////////////
	/////		Константы для настроек			//////
	//////////////////////////////////////////////////
	private static final String STORAGE = "Labirynth settings";
	private static final String ACCELX = "accelX";
	private static final String ACCELY = "accelY";
	private static final String ACCELZ = "accelZ";
	private static final String ACCELSENSIVITY = "accelSens";
	
	private static Context context = null;
	private static SharedPreferences settings = null;
	
	public SettingsStorage(Context initContext){
		context = initContext;
		settings = context.getSharedPreferences(STORAGE, 0);
	}
	
	/**
	 * Инициализация хранилища.
	 * @param initContext - контекст приложения
	 */
	public static void initialize(Context initContext){
		context = initContext;
		settings = context.getSharedPreferences(STORAGE, 0);
	}
	
	/**
	 * Сохраняет состояние акселерометра в настройках
	 * @param pos 3-d вектор состояния акселерометра
	 * @return true если сохранено успешно
	 */
	public static boolean saveAcellPosition(float[] pos){
		if (pos.length < 3)
			return false;
		SharedPreferences.Editor edit = settings.edit();
		edit.putFloat(ACCELX, pos[0]);
		edit.putFloat(ACCELY, pos[1]);
		edit.putFloat(ACCELZ, pos[2]);
		edit.commit();
		return true;
	}
	
	/**
	 * Загружает состояние акселерометра из настроек, или возвращает нулевой вектор (не ссылку на null !)
	 */
	public static float[] getAcellPosition(){
		float[] pos = new float[]{0,0,0};
		pos[0] = settings.getFloat(ACCELX, 0);
		pos[1] = settings.getFloat(ACCELY, 0);
		pos[2] = settings.getFloat(ACCELZ, 0);
		return pos;
	}
	
	/**
	 * Сохраняет значение чувствительности акселерометра
	 * @param sens - зничение
	 */
	public static void saveSensivity(float sens){
		SharedPreferences.Editor edit = settings.edit();
		edit.putFloat(ACCELSENSIVITY, sens);
		edit.commit();
	}
	
	/**
	 * Возвращает значение чувствительности акселерометра (или 1 если нет в настройках)
	 * @return значение
	 */
	public static float getSensivity(){
		return settings.getFloat(ACCELSENSIVITY, 1);
	}
}

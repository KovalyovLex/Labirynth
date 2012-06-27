package com.flexymind.labirynth.FXs;

import com.flexymind.labirynth.storage.SettingsStorage;

import android.content.Context;
import android.os.Vibrator;

public class Vibration {

	private  Vibrator mVibrator;
	private  int vibroTime = 300; // 300 milliseconds per vibration
	private  long[] pattern; // for vibration
	private  int nSilent = 15; // num of silent intervals
	
	public Vibration(Context context){
		mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		pattern = new long[2 * (nSilent + 1)];
		
		setIntensity(SettingsStorage.getVibroInt());
	}
	
	/**
	 * set intensity of vibration
	 * @param intens - 0..1
	 */
	public void setIntensity(float intens){
		// vibration intervals
		long vibrint	= (long)(vibroTime * intens); // vibration time
		long vib		= (long)(vibrint / (nSilent + 1)); // vibration time in 1 tick
		long silent		= (long)(vibroTime - vibrint) / nSilent; // silent time in 1 tick
		
		pattern[0] = 0;
		for (int i = 1; i < pattern.length - 1; i++){
			pattern[i]   = vib;
			pattern[i++] = silent;
		}
		pattern[pattern.length - 1] = vib;
	}
	
	/**
	 * starts single vibration
	 */
	public void singleVibration(){
		mVibrator.cancel();
		// Only perform this pattern one time (-1 means "do not repeat", 0 - repeat all time)
		mVibrator.vibrate(pattern, -1);

	}
}

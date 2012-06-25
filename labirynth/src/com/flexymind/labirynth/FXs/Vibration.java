package com.flexymind.labirynth.FXs;

import android.content.Context;
import android.os.Vibrator;

public class Vibration {

	private  Vibrator mVibrator;
	private  int vibroTime = 300; // 300 milliseconds per vibration
	
	public Vibration(Context context){
		mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
	}
	
	/**
	 * starts single vibration
	 */
	public void singleVibration(){
		mVibrator.cancel();
		mVibrator.vibrate(vibroTime);
	}
}

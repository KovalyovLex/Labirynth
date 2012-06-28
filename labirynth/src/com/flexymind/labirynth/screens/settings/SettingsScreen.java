package com.flexymind.labirynth.screens.settings;

import com.flexymind.labirynth.R;
import com.flexymind.labirynth.FXs.Vibration;
import com.flexymind.labirynth.storage.Settings;
import com.flexymind.labirynth.storage.SettingsStorage;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class SettingsScreen extends Activity implements OnClickListener{

	private final int MAXSENS = 100;
	private Vibration mVibration;
	private static SensorManager sensMan = null;
	
	// Для калибровки акселерометра
	private static int imeasure = 0;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.settings);
		
		sensMan = (SensorManager) this.getSystemService(SENSOR_SERVICE);
		
		addButtons();
		
		mVibration = new Vibration(this);
		
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
	}
	
	private class AccelSensBarChangeListener implements SeekBar.OnSeekBarChangeListener {

		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			if (progress > 0){
				SettingsStorage.saveSensivity(progress / 20f);
			}else{
				SettingsStorage.saveSensivity(1 / 20f);
			}
		}

		public void onStartTrackingTouch(SeekBar seekBar) { }

		public void onStopTrackingTouch(SeekBar seekBar) { }
		
	}
	
	private class VolumeChangeListener implements SeekBar.OnSeekBarChangeListener {

		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			if (progress > 0){
				SettingsStorage.saveSoundVolume((float)progress / MAXSENS);
			}else{
				SettingsStorage.saveSoundVolume(1f / MAXSENS);
			}
			// play sound
		}

		public void onStartTrackingTouch(SeekBar seekBar) { }

		public void onStopTrackingTouch(SeekBar seekBar) { }
		
	}
	
	private class VibroIntBarChangeListener implements SeekBar.OnSeekBarChangeListener {

		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			if (progress > 0){
				SettingsStorage.saveVibroInt((float)progress / MAXSENS);
				mVibration.setIntensity((float)progress / MAXSENS);
			}else{
				SettingsStorage.saveVibroInt(1f / MAXSENS);
				mVibration.setIntensity(1f / MAXSENS);
			}
			mVibration.singleVibration();
		}

		public void onStartTrackingTouch(SeekBar seekBar) { }

		public void onStopTrackingTouch(SeekBar seekBar) { }
		
	}
	
	public void addButtons() {
		
		Button calibrButton = (Button) findViewById(R.id.accelCalibr);
		SeekBar bar = (SeekBar)findViewById(R.id.seekBar1);
		SeekBar barvibr = (SeekBar)findViewById(R.id.SeekBar01);
		SeekBar barvol = (SeekBar)findViewById(R.id.SeekBar02);
		TextView text = (TextView)findViewById(R.id.sensText);
		TextView textvibr = (TextView)findViewById(R.id.vibrIntText);
		TextView textvol = (TextView)findViewById(R.id.volText);
		
		barvibr.setMax(MAXSENS);
		barvibr.setProgress((int)(SettingsStorage.getVibroInt() * MAXSENS));
		barvol.setMax(MAXSENS);
		barvol.setProgress((int)(SettingsStorage.getSoundVolume() * MAXSENS));
		bar.setMax(MAXSENS);
		bar.setProgress((int)(SettingsStorage.getSensivity() * 20));
		
		bar.setOnSeekBarChangeListener(new AccelSensBarChangeListener());
		barvibr.setOnSeekBarChangeListener(new VibroIntBarChangeListener());
		barvol.setOnSeekBarChangeListener(new VolumeChangeListener());
		calibrButton.setOnClickListener(this);
		
		LinearLayout lay = (LinearLayout)findViewById(R.id.settButtLayout);
		
		LinearLayout.LayoutParams buttonparams = (LinearLayout.LayoutParams)calibrButton.getLayoutParams();
		ViewGroup.LayoutParams barparams = (ViewGroup.LayoutParams)bar.getLayoutParams();
		ViewGroup.LayoutParams barvibrparams = (ViewGroup.LayoutParams)barvibr.getLayoutParams();
		ViewGroup.LayoutParams barvolparams = (ViewGroup.LayoutParams)barvol.getLayoutParams();
		ViewGroup.LayoutParams textparams = (ViewGroup.LayoutParams)text.getLayoutParams();
		ViewGroup.LayoutParams textvibrparams = (ViewGroup.LayoutParams)textvibr.getLayoutParams();
		ViewGroup.LayoutParams textvolparams = (ViewGroup.LayoutParams)textvol.getLayoutParams();
		
		if (Settings.getAutoScale()) {
			
			buttonparams.height = (int)(Settings.getScaleFactorY() * buttonparams.height);
			buttonparams.width  = (int)(Settings.getScaleFactorX() * buttonparams.width);
			buttonparams.topMargin = (int)(Settings.getScaleFactorY() * buttonparams.topMargin);
			buttonparams.bottomMargin = (int)(Settings.getScaleFactorY() * buttonparams.bottomMargin);
			buttonparams.leftMargin = (int)(Settings.getScaleFactorX() * buttonparams.leftMargin);
			buttonparams.rightMargin = (int)(Settings.getScaleFactorX() * buttonparams.rightMargin);
			
			barparams.height = (int)(Settings.getScaleFactorY() * barparams.height);
			barparams.width  = (int)(Settings.getScaleFactorX() * barparams.width);
			barvibrparams.height = (int)(Settings.getScaleFactorY() * barvibrparams.height);
			barvibrparams.width  = (int)(Settings.getScaleFactorX() * barvibrparams.width);
			barvolparams.height = (int)(Settings.getScaleFactorY() * barvolparams.height);
			barvolparams.width  = (int)(Settings.getScaleFactorX() * barvolparams.width);
			
			textparams.height = (int)(Settings.getScaleFactorY() * textparams.height);
			textparams.width  = (int)(Settings.getScaleFactorX() * textparams.width);
			textvibrparams.height = (int)(Settings.getScaleFactorY() * textvibrparams.height);
			textvibrparams.width  = (int)(Settings.getScaleFactorX() * textvibrparams.width);
			textvolparams.height = (int)(Settings.getScaleFactorY() * textvolparams.height);
			textvolparams.width  = (int)(Settings.getScaleFactorX() * textvolparams.width);
			
			RelativeLayout parent = (RelativeLayout)lay.getParent();
			RelativeLayout.LayoutParams rlparams = (RelativeLayout.LayoutParams)lay.getLayoutParams();
			rlparams.topMargin = (int)(Settings.getScaleFactorY() * rlparams.topMargin);
			rlparams.bottomMargin = (int)(Settings.getScaleFactorY() * rlparams.bottomMargin);
			rlparams.leftMargin = (int)(Settings.getScaleFactorX() * rlparams.leftMargin);
			rlparams.rightMargin = (int)(Settings.getScaleFactorX() * rlparams.rightMargin);
			rlparams.height = (int)(Settings.getScaleFactorY() * rlparams.height);
			rlparams.width = (int)(Settings.getScaleFactorX() * rlparams.width);
			
			lay.removeAllViews();
			
			lay.addView(bar, barparams);
			lay.addView(barvibr, barvibrparams);
			lay.addView(barvol, barvolparams);
			lay.addView(text, textparams);
			lay.addView(textvibr, textvibrparams);
			lay.addView(textvol, textvolparams);
			lay.addView(calibrButton, buttonparams);
			
			parent.removeAllViews();
			parent.addView(lay, rlparams);
		}
		
	}
	
	public static final SensorEventListener accelerometerListener = new SensorEventListener() {
		
		public void onSensorChanged(SensorEvent event) {
			if (imeasure++ > 10){
				SettingsStorage.saveAcellPosition(event.values);
				sensMan.unregisterListener(accelerometerListener);
				imeasure = 0;
			}
		}
		
		public void onAccuracyChanged(Sensor sensor, int accuracy) { }

	};
	
	public void onClick(View arg0) {
		switch(arg0.getId()){
		case R.id.accelCalibr:
			sensMan.registerListener(accelerometerListener, sensMan.getDefaultSensor(SensorManager.SENSOR_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
			break;
		}
		
	}
	
}

package com.flexymind.labirynth.FXs;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;

public class SoundEngine {

	private  SoundPool mSoundPool;
	private  HashMap<Integer,Integer> mSoundPoolMap;
	private  AudioManager  mAudioManager;
	private  MediaPlayer mMediaPlay;
	private  Context mContext;
	private  HashMap<Integer,Float> mVolumeMap;
	private  boolean delayPlay = false;
	private  boolean delayLooped = false;
	
	public SoundEngine(Context context){
		mContext = context;
		mSoundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
	    mSoundPoolMap = new HashMap<Integer,Integer>();
	    mAudioManager = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);
	    mVolumeMap = new HashMap<Integer,Float>();
	    mSoundPool.setOnLoadCompleteListener(new OnLoadCompleteListener(){

			public void onLoadComplete(SoundPool soundPool, int sampleId,
					int status) {
				if (delayPlay){
					if (delayLooped){
						playLoopedSound(sampleId);
					}else{
						playSound(sampleId);
					}
				}
			}
			
	    });
		delayPlay = false;
		delayLooped = false;
		
	}
	
	public void initialize(Context context)
	{
		mContext = context;
		mSoundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
	    mSoundPoolMap = new HashMap<Integer,Integer>();
	    mAudioManager = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);
	    mVolumeMap = new HashMap<Integer,Float>();
	    mSoundPool.setOnLoadCompleteListener(new OnLoadCompleteListener(){

			public void onLoadComplete(SoundPool soundPool, int sampleId,
					int status) {
				if (delayPlay){
					if (delayLooped){
						playLoopedSound(sampleId);
					}else{
						playSound(sampleId);
					}
				}
			}
			
	    });
	    delayPlay = false;
		delayLooped = false;
	}
	
	/**
	 * load sound from resources
	 * @param index - individual index of loading sound
	 * @param SoundID - identificator in the system resources R.raw.sound
	 */
	public void addSound(int index, int SoundID)
	{
	    mSoundPoolMap.put(index, mSoundPool.load(mContext, SoundID, 1));
	}
	
	/**
	 * set volume of sound index
	 * @param index - individual index of loading sound
	 * @param volume - volume of sound 0..1
	 */
	public void setPlaySoundVolume(int index, float volume){
		mVolumeMap.put(index, volume);
	}
	
	/**
	 * set volume of played stream
	 * @param volume - new volume of sound 0..1
	 */
	public void setPlayedStreamVolume(float volume){
		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 
				(int)(volume * mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)),
				AudioManager.FLAG_VIBRATE);
	}
	
	/**
	 * play preveously loaded sound
	 * @param index - individual index of loading sound
	 */
	public void playSound(int index)
	{
		delayPlay	= true;
		delayLooped	= false;
		if (mSoundPoolMap.containsKey(index)){
			float streamVolume = 1;
			if (mVolumeMap.containsKey(index)){
				streamVolume = mVolumeMap.get(index);
			}
		    mSoundPool.play(mSoundPoolMap.get(index), streamVolume, streamVolume, 1, 0, 1f);
		}
	}
	
	/**
	 * play preveously loaded sound
	 * @param index - individual index of loading sound
	 */
	public void playLoopedSound(int index)
	{
		delayPlay	= true;
		delayLooped	= true;
		if (mSoundPoolMap.containsKey(index)){
			float streamVolume = 1;
			if (mVolumeMap.containsKey(index)){
				streamVolume = mVolumeMap.get(index);
			}
			mSoundPool.play(mSoundPoolMap.get(index), streamVolume, streamVolume, 1, -1, 1f);
		}
	}
	
	/**
	 * play preveously loaded sound
	 * @param index - individual index of loading sound
	 */
	public void releaseSounds()
	{
		if (mSoundPool != null){
			Collection<Integer> sounds = mSoundPoolMap.values();
			Iterator<Integer> iter = sounds.iterator();
			Integer id;
			for (int i = 0; i < sounds.size(); i++){
				id = iter.next();
				mSoundPool.stop(id);
				mSoundPool.unload(id);
			}
			mSoundPool.release();
			mSoundPool = null;
		}
	}
}

package com.flexymind.labirynth.FXs;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;

public class SoundEngine {

	private  SoundPool mSoundPool;
	private  HashMap<Integer,Integer> mSoundPoolMap;
	private  Context mContext;
	private  HashMap<Integer,Float> mVolumeMap;
	private  HashMap<Integer,Boolean> mLoadComplete;
	private  boolean delayPlay = false;
	private  boolean delayLooped = false;
	private  boolean stopPlaying = false;
	private  int playTime = 200; // 200 msec. to play sound effect
	
	public SoundEngine(Context context){
		mContext = context;
		mSoundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
	    mSoundPoolMap = new HashMap<Integer,Integer>();
	    mVolumeMap = new HashMap<Integer,Float>();
	    mLoadComplete = new HashMap<Integer,Boolean>();
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
	    mVolumeMap = new HashMap<Integer,Float>();
	    mLoadComplete = new HashMap<Integer,Boolean>();
	    mSoundPool.setOnLoadCompleteListener(new OnLoadCompleteListener(){

			public void onLoadComplete(SoundPool soundPool, int sampleId,
					int status) {
				if (mLoadComplete.containsKey(sampleId)){
					mLoadComplete.remove(sampleId);
				}
				mLoadComplete.put(sampleId, true);
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
		mLoadComplete.put(index, false);
	    mSoundPoolMap.put(index, mSoundPool.load(mContext, SoundID, 1));
	}
	
	/**
	 * set volume of sound index
	 * @param index - individual index of loading sound
	 * @param volume - volume of sound 0..1
	 */
	public void setPlaySoundVolume(int index, float volume){
		if (mVolumeMap.containsKey(index)){
			mVolumeMap.remove(index);
		}
		mVolumeMap.put(index, volume);
	}
	
	/**
	 * set volume of played stream
	 * @param volume - new volume of sound 0..1
	 */
	/*
	public void setPlayedStreamVolume(float volume){
		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 
				(int)(volume * mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)),
				AudioManager.FLAG_VIBRATE);
	}
	*/
	
	/**
	 * play preveously loaded sound
	 * @param index - individual index of loading sound
	 */
	public void playSound(int index)
	{
		delayPlay	= true;
		delayLooped	= false;
		if (mLoadComplete.containsKey(index) && mLoadComplete.get(index)){
			if (mSoundPoolMap.containsKey(index)){
				float streamVolume = 1;
				if (mVolumeMap.containsKey(index)){
					streamVolume = mVolumeMap.get(index);
				}
		    	mSoundPool.play(mSoundPoolMap.get(index), streamVolume, streamVolume, 1, 0, 1f);
			}
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
		if (mLoadComplete.containsKey(index) && mLoadComplete.get(index)){
			final int ind = index;
			
			Runnable muzPlay = new Runnable(){
	
				public void run() {
					float streamVolume;
					while (!stopPlaying){
						if (mSoundPoolMap.containsKey(ind)){
							streamVolume = 1;
							if (mVolumeMap.containsKey(ind)){
								streamVolume = mVolumeMap.get(ind);
							}
							mSoundPool.stop(mSoundPoolMap.get(ind));
							mSoundPool.play(mSoundPoolMap.get(ind), streamVolume, streamVolume, 1, 0, 1f);
						}
						try {
							Thread.sleep(playTime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				
			};
			
			muzPlay.run();
		}
	}
	
	/**
	 * play preveously loaded sound
	 * @param index - individual index of loading sound
	 */
	public void releaseSounds()
	{
		stopPlaying = true;
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

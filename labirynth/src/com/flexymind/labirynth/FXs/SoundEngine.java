package com.flexymind.labirynth.FXs;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.AsyncTask;

public class SoundEngine {

	private  SoundPool mSoundPool;
	private  HashMap<Integer,Integer> mSoundPoolMap;
	private  Context mContext;
	private  HashMap<Integer,Float> mVolumeMap;
	private  HashMap<Integer,Boolean> mLoadComplete;
	private  boolean delayPlay = false;
	private  boolean delayLooped = false;
	private  boolean stopPlaying = false;
	private  int playTime = 300; // 300 msec. to play sound effect
	private  PlayTask muzPlay;
	
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
		int sampleId = mSoundPool.load(mContext, SoundID, 1);
	    mSoundPoolMap.put(index, sampleId);
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
	 * play preveously loaded sound
	 * @param index - individual index of loading sound
	 */
	public void playSound(int index)
	{
		delayPlay	= true;
		delayLooped	= false;
		if (mSoundPoolMap.containsKey(index)){
			if (	mLoadComplete.containsKey(mSoundPoolMap.get(index))
				 && mLoadComplete.get(mSoundPoolMap.get(index))){
				
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
		
		if ( mSoundPoolMap.containsKey(index) ){
			
			muzPlay = new PlayTask(index);
			
			muzPlay.execute();
		}
	}
	
	private class PlayTask extends AsyncTask<Void,Void,Void>{
		private int ind = 0;
		
		public PlayTask(int index){
			ind = index;
		}
		
		protected Void doInBackground(Void... params) {
			float streamVolume;
			
			while (!stopPlaying){
				try {
					Thread.sleep(playTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
						
				streamVolume = 1;
				if (mVolumeMap.containsKey(ind)){
					streamVolume = mVolumeMap.get(ind);
				}
				if (mSoundPool != null){
					mSoundPool.stop(mSoundPoolMap.get(ind));
					mSoundPool.play(mSoundPoolMap.get(ind), streamVolume, streamVolume, 2, 0, 1f);
					
					//Log.v("streamVolume",Integer.toString((int)(100 * streamVolume)));
				}
			}
			return null;
		}
		
		protected void onPostExecute(Void result) { }
	}
	
	/**
	 * play preveously loaded sound
	 * @param index - individual index of loading sound
	 */
	public void releaseSounds()
	{
		stopPlaying = true;
		if (muzPlay != null && muzPlay.getStatus().equals(AsyncTask.Status.RUNNING)){
			muzPlay.cancel(true);
		}
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

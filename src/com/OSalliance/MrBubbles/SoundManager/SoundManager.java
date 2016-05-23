package com.OSalliance.MrBubbles.SoundManager;

import java.util.HashMap;

import com.OSalliance.MrBubbles.R;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundManager {
		private static SoundPool mSoundPool;
		private static HashMap<Integer, Integer> mSoundPoolMap;
		private static AudioManager  mAudioManager;
		private static Context mContext;
		private static boolean isMuted;
	 
		private SoundManager() {
		
		}
	 
		/**
		 * Initialises the storage for the sounds
		 *
		 * @param theContext The Application context
		 */
		public static void initSounds(Context theContext)
		{
			 mContext = theContext;
		     mSoundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
		     mSoundPoolMap = new HashMap<Integer, Integer>();
		     mAudioManager = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);
		     isMuted = false;
		} 
		
		public static void muteSound() {
			isMuted = true;
		}
		
		public static void unmuteSound() {
			isMuted = false;
		}
		
		/**
		 * Add a new Sound to the SoundPool
		 *
		 * @param Index - The Sound Index for Retrieval
		 * @param SoundID - The Android ID for the Sound asset.
		 */
		public static void addSound(int Index,int SoundID)
		{
			mSoundPoolMap.put(Index, mSoundPool.load(mContext, SoundID, 1));
		}
	 
		/**
		 * Loads the various sound assets
		 * Currently hardcoded but could easily be changed to be flexible.
		 */
		public static void loadSounds()
		{
			mSoundPoolMap.put(0, mSoundPool.load(mContext, R.raw.drop, 1));
			mSoundPoolMap.put(1, mSoundPool.load(mContext, R.raw.pop0, 1));
			mSoundPoolMap.put(2, mSoundPool.load(mContext, R.raw.pop1, 1));
		}
	 
		/**
		 * Plays a Sound
		 *
		 * @param index - The Index of the Sound to be played
		 * @param speed - The Speed to play not, not currently used but included for compatibility
		 */
		public static void playSound(int index, float speed)
		{
			if (!isMuted) {
				float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
				streamVolume = streamVolume / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
				mSoundPool.play(mSoundPoolMap.get(index), streamVolume, streamVolume, 1, 0, speed);
			}
		}
	 
		/**
		 * Stop a Sound
		 * @param index - index of the sound to be stopped
		 */
		public static void stopSound(int index)
		{
			mSoundPool.stop(mSoundPoolMap.get(index));
		}
	 
		/**
		 * Deallocates the resources and Instance of SoundManager
		 */
		public static void cleanup()
		{
			mSoundPool.release();
			mSoundPool = null;
		    mSoundPoolMap.clear();
		    mAudioManager.unloadSoundEffects();
	 
		}

}

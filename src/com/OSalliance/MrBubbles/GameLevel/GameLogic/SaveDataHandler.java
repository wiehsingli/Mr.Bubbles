package com.OSalliance.MrBubbles.GameLevel.GameLogic;

import com.OSalliance.MrBubbles.MainMenu.MainActivity;

import android.content.SharedPreferences;
import android.util.Log;

public class SaveDataHandler {
	
	private static final String TAG = SaveDataHandler.class.getSimpleName();
	
	public static final int NUM_LEVELS = 20;
	
	private static MainActivity activity;
	private static boolean firstTime;
	private static int[] levelDataArray;
	private static boolean sound;
	private static boolean music;
	
	private static boolean dirty;

	public static void openSaveData(MainActivity context){
		activity = context;
		
		SharedPreferences saveData = activity.getPreferences(MainActivity.MODE_PRIVATE);
		
		if (saveData != null){
			levelDataArray = new int[20];
			
			// The first level must always be active, so the score should default to 0
			levelDataArray[0] = saveData.getInt("level" + 1 + "data", -1);
			if (levelDataArray[0] == -1){
				firstTime = true;
				levelDataArray[0] = 0;
			}else{
				firstTime = false;
			}
			
			for (int i = 1; i < 20; i++){
				int levelData = saveData.getInt("level" + (i + 1) + "data", -1);
				
				levelDataArray[i] = levelData;
			}
			
			sound = saveData.getBoolean("sound", true);
			music = saveData.getBoolean("music", true);
		}else{
			Log.e(TAG, "Error loading saveData!");
		}
		
		dirty = false;
	}
	
	public static boolean isFirstTime(){
		return firstTime;
	}
	
	public static int[] loadAllLevelData(){
		return levelDataArray;
	}
	
	public static int loadLevelData(int levelID){
		//Log.d(TAG, "LevelID: " + levelID + ", Score: " + levelDataArray[levelID - 1] + ". LOADED");
		
		return levelDataArray[levelID - 1];
	}
	
	public static boolean loadOptionsSound(){
		return sound;
	}
	
	public static boolean loadOptionsMusic(){
		return music;
	}
	
	public static void storeLevelData(int levelID, int score){
		levelDataArray[levelID - 1] = score;
		
		SharedPreferences saveData = activity.getPreferences(MainActivity.MODE_PRIVATE);
		
		if (saveData != null){
			SharedPreferences.Editor editor = saveData.edit();
		
			editor.putInt("level" + levelID + "data", score);
		
			editor.commit();
			
			Log.d(TAG, "LevelID: " + levelID + ", Score: " + score + ". SAVED");
		}else{
			Log.e(TAG, "Error storing saveData!");
		}
		
		dirty = true;
	}
	
	public static void storeOptionsData(boolean soundOn, boolean musicOn){
		SharedPreferences saveData = activity.getPreferences(MainActivity.MODE_PRIVATE);
		
		sound = soundOn;
		music = musicOn;
		
		if (saveData != null){
			SharedPreferences.Editor editor = saveData.edit();
			
			editor.putBoolean("sound", sound);
			editor.putBoolean("music", music);
		
			editor.commit();
		}else{
			Log.e(TAG, "Error storing saveData!");
		}
	}
	
	public static void clearSaveData(){
		SharedPreferences saveData = activity.getPreferences(MainActivity.MODE_PRIVATE);
		
		if (saveData != null){
			SharedPreferences.Editor editor = saveData.edit();
			
			editor.clear();
			
			for (int i = 0; i < levelDataArray.length; i++){
				levelDataArray[i] = -1;
			}
		
			editor.commit();
			
			firstTime = true;
			
			Log.d(TAG, "SaveData cleared.");
		}else{
			Log.e(TAG, "Error clearing saveData!");
		}
	}
	
	public static boolean isDirty(){
		return dirty;
	}
	
	public static void clean(){
		dirty = false;
	}
	
	/*public static void closeSaveData(){
		if (activity != null){
			SharedPreferences saveData = activity.getPreferences(MainActivity.MODE_PRIVATE);
	
			SharedPreferences.Editor editor = saveData.edit();
			
			for (int i = 0; i < 20; i++){
				editor.putInt("level" + (i + 1) + "data", levelDataArray[i]);
			}
			
			editor.commit();
		}else{
			Log.e(TAG, "Activity is null!");
		}
	}*/
	
}

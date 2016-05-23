package com.OSalliance.MrBubbles.MainMenu.Level;

import com.OSalliance.MrBubbles.GameLevel.GameActivity;
import com.OSalliance.MrBubbles.SoundManager.SoundManager;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class LevelActivity extends Activity{
	
	private static final String TAG = LevelActivity.class.getSimpleName();
	
	public static final int RETURN_MENU = 0;
	
	private static final int MAX_LEVELS = 20;
	
	private LevelPanel panel;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        
        panel = new LevelPanel(this);
        
        setContentView(panel);
	}
	
	/**
	 * Called by LevelPanel to change the menu to specific level.
	 * 
	 * @param levelID	The ID of the level to go to. Use RETURN_MENU to go to main menu.
	 */
	public void chooseLevel(int levelID){
		Log.d(TAG, "Go to level " + levelID);
		
		if (levelID == RETURN_MENU || levelID > MAX_LEVELS){
			finish();
		}else{
			Intent intent = new Intent(this, GameActivity.class);
			intent.putExtra("levelID", levelID);
			startActivity(intent);
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		panel.checkLevelData();
	}
	
    /**
     * Takes care of the functionality of the back button.
     */
    @Override
    public void onBackPressed(){
    	SoundManager.playSound(0, 1);
    	super.onBackPressed();
    }
}

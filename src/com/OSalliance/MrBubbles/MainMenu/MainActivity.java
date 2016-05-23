package com.OSalliance.MrBubbles.MainMenu;

import com.OSalliance.MrBubbles.R;
import com.OSalliance.MrBubbles.GameLevel.GameLogic.SaveDataHandler;
import com.OSalliance.MrBubbles.MainMenu.Credits.CreditsActivity;
import com.OSalliance.MrBubbles.MainMenu.Intro.IntroActivity;
import com.OSalliance.MrBubbles.MainMenu.Level.LevelActivity;
import com.OSalliance.MrBubbles.MainMenu.Options.OptionsActivity;
import com.OSalliance.MrBubbles.SoundManager.SoundManager;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * THIS IS WHERE THE PROGRAM STARTS UP
 */
public class MainActivity extends Activity{
	private static final String TAG = MainActivity.class.getSimpleName();
	
	public static final int LEVEL_MENU = 0;
	public static final int OPTIONS_MENU = 1;
	public static final int CREDITS_MENU = 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		// Initializes the sound manager
		SoundManager.initSounds(this);
        SoundManager.loadSounds();
		
        // Removes the gray title and the status bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        
        // Create the MainPanel and display it on the screen
        setContentView(new MainPanel(this));
        
        /*this.setContentView(R.layout.activity_main);
        
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.bubble);
        ((ImageView)findViewById(R.id.menu_main_button_play)).startAnimation(animation);*/
        
        Log.d(TAG, "Construction complete.");
	}
	
	/**
	 * Called by MainPanel to change the menu.
	 * Use the constants of this class.
	 * 
	 * @param activityID	The specified menu.
	 */
	public void changeActivity(int activityID){
		Log.d(TAG, "ChangeActivity has been called.");
		if (activityID == LEVEL_MENU){
			Intent intent;
			/*if (SaveDataHandler.isFirstTime()){
				intent = new Intent(this, IntroActivity.class);
			}else{*/
				intent = new Intent(this, LevelActivity.class);
			//}
    		startActivity(intent);
		}else if (activityID == OPTIONS_MENU){
			Intent intent = new Intent(this, OptionsActivity.class);
    		startActivity(intent);
		}else if (activityID == CREDITS_MENU){
			Intent intent = new Intent(this, CreditsActivity.class);
    		startActivity(intent);
		}
	}
	
	 @Override
    public void onPause(){
    	super.onPause();
    }
    
    @Override
    public void onResume(){
    	super.onResume();
    	
    	SaveDataHandler.openSaveData(this);
    	
    	if (!SaveDataHandler.loadOptionsSound()){
    		SoundManager.muteSound();
    	}
    	
    	if (!SaveDataHandler.loadOptionsMusic()){
    		// mute music
    	}
    }
    
    @Override
    public void onDestroy(){
    	super.onDestroy();
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

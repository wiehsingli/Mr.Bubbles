package com.OSalliance.MrBubbles.MainMenu.Options;

import com.OSalliance.MrBubbles.SoundManager.SoundManager;

import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

/**
 * Handles construction of the OptionsPanel
 */
public class OptionsActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        
        setContentView(new OptionsPanel(this));
	}
	
	/**
	 * Called by OptionsPanel to go back to previous menu.
	 */
	public void returnActivity(){
		finish();
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

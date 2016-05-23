package com.OSalliance.MrBubbles.GameLevel;

import com.OSalliance.MrBubbles.R;
import com.OSalliance.MrBubbles.GameLevel.AmmoBar.AmmoBar;
import com.OSalliance.MrBubbles.MainMenu.MainActivity;
import com.OSalliance.MrBubbles.SoundManager.SoundManager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class GameActivity extends Activity{
	private static final String TAG = GameActivity.class.getSimpleName();
	
	GamePanel gamePanel;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.d(TAG, "onCreate() called");
    	
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        int width = getWindowManager().getDefaultDisplay().getWidth();
        int height = getWindowManager().getDefaultDisplay().getHeight();

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new LinearLayout.LayoutParams(width, height));
        layout.setGravity(Gravity.TOP);
        layout.setBackgroundResource(R.drawable.background);
        
        int ammoBarHeight = height / 8;
        
        AmmoBar ammoBar = new AmmoBar(this, width, ammoBarHeight);

        Bundle extras = getIntent().getExtras();
        int levelID = extras.getInt("levelID");
        
        gamePanel = new GamePanel(this, ammoBar, width, height - ammoBarHeight, levelID);
        gamePanel.setLayoutParams(new LayoutParams(width, height - ammoBarHeight));
        
        gamePanel.setZOrderOnTop(true);
        gamePanel.getHolder().setFormat(PixelFormat.TRANSPARENT);
        
        layout.addView(gamePanel);
        layout.addView(ammoBar);
        
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        
        setContentView(layout);
    }
    
    /**
     * Called by GamePanel to go back to LevelSelect
     */
    public void gotoLevelActivity(){
    	finish();
    }
    
    /**
     * Called by GamePanel to go back to MainMenu
     */
    public void gotoMainActivity(){
    	Intent intent = new Intent(this, MainActivity.class);
    	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	startActivity(intent);
    }
    
    /**
     * When the activity is paused, let the GamePanel know
     * that it should bring up the pause menu.
     */
    @Override
    public void onPause(){
    	gamePanel.pauseGame();
    	
    	super.onPause();
    }
    
    @Override
    public void onResume(){
    	super.onResume();
    }
    
    @Override
    public void onDestroy(){
    	super.onDestroy();
    }
    
    @Override
    public final boolean onCreateOptionsMenu(Menu menu) {
    	SoundManager.playSound(0, 1);
        //getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        //return true;
    	Log.d(TAG, "OptionsMenu");
    	
    	if (gamePanel.gamePlaying()){
    		gamePanel.pauseGame();
    	}
    	
    	else if (gamePanel.gamePaused()) {
    		gamePanel.resumeGame();
    	}
    	
    	return false;
    }
    
    /**
     * Takes care of the functionality of the back button.
     * If the pause menu is already open in gamePanel, return
     * to the LevelSelect menu.
     */
    @Override
    public void onBackPressed(){
    	SoundManager.playSound(0, 1);
    	if (gamePanel.gamePlaying()){
    		gamePanel.pauseGame();
    	}else{
    		super.onBackPressed();
    	}
    }
    
    
}
package com.OSalliance.MrBubbles.MainMenu.Options;

import com.OSalliance.MrBubbles.R;
import com.OSalliance.MrBubbles.GameLevel.GameLogic.SaveDataHandler;
import com.OSalliance.MrBubbles.MainMenu.CustomPanel;
import com.OSalliance.MrBubbles.MainMenu.MenuButton;
import com.OSalliance.MrBubbles.SoundManager.SoundManager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Handles creation of the options menu.
 */
public class OptionsPanel extends CustomPanel{
	
	private static final String TAG = OptionsPanel.class.getSimpleName();
	
	private OptionsActivity activity;
	
	private MenuButton title;
	
	private MenuButton soundButton;
	private MenuButton musicButton;
	private MenuButton resetButton;
	private MenuButton backButton;
	
	private boolean soundOn;
	private boolean musicOn;
	
	private DeleteDataMenu menu;
	
	private boolean deletingData;
	
	public OptionsPanel(OptionsActivity activity) {
		super(activity);
		
		this.activity = activity;
		
		// Set up the background stuff
		setBackgroundResource(R.drawable.background);
		getHolder().setFormat(PixelFormat.TRANSPARENT);
		setZOrderOnTop(true);
		
		int width = activity.getWindowManager().getDefaultDisplay().getWidth();
		int height = activity.getWindowManager().getDefaultDisplay().getHeight();
		
		int xPos = width / 3;
		int yPos = height / 5;
		
		soundOn = SaveDataHandler.loadOptionsSound();
		musicOn = SaveDataHandler.loadOptionsMusic();
		
		Log.d(TAG, "Sound: " + soundOn + ", Music: " + musicOn);
		
		title = new MenuButton(this.getContext(), R.drawable.title_options, 0, 0);
		title.setX(width / 2 - title.getWidth() / 2);
		title.setY(height / 8 - title.getHeight() / 2);
		
		int drawable = R.drawable.menu_options_buttonsoundon;
		
		if (!soundOn){
			drawable = R.drawable.menu_options_buttonsoundoff;
			Log.d(TAG, "Initialized sound to soundOff");
		}
		
		soundButton = new MenuButton(this.getContext(), drawable, xPos * 2, yPos * 2);
		soundButton.setX(soundButton.getX() - soundButton.getWidth() / 2);
		soundButton.setY(soundButton.getY() - soundButton.getHeight() / 2);
		
		if (musicOn){
			drawable = R.drawable.menu_options_buttonmusicon;
		}else{
			drawable = R.drawable.menu_options_buttonmusicoff;
			Log.d(TAG, "Initialized sound to musicOff");
		}
		
		musicButton = new MenuButton(this.getContext(), drawable, xPos, yPos * 3);
		musicButton.setX(musicButton.getX() - musicButton.getWidth() / 2);
		musicButton.setY(musicButton.getY() - musicButton.getHeight() / 2);
		resetButton = new MenuButton(this.getContext(), R.drawable.menu_options_buttonresetgame, xPos * 2, yPos * 4);
		resetButton.setX(resetButton.getX() - resetButton.getWidth() / 2);
		resetButton.setY(resetButton.getY() - resetButton.getHeight() / 2);
		backButton = new MenuButton(this.getContext(), R.drawable.menu_main_buttonback, xPos / 2, yPos * 4.5);
		backButton.setX(backButton.getX() - backButton.getWidth() / 2);
		backButton.setY(backButton.getY() - backButton.getHeight() / 2);
		
		menu = new DeleteDataMenu(activity, 0, 0);
		menu.setX(width / 2 - menu.getWidth() / 2);
		menu.setY(height / 2 - menu.getHeight() / 2);
		
		deletingData = false;
	}
	
	/**
	 * Update each menu button.
	 */
	@Override
	public void update(){
		super.update();
		
		if (!deletingData){
			title.update();
			
			soundButton.update();
			musicButton.update();
			resetButton.update();
			backButton.update();
		}
	}
	
	/**
	 * Draw each menu button.
	 */
	@Override
	public void render(Canvas canvas){
		super.render(canvas);
		
		canvas.drawColor(0, PorterDuff.Mode.CLEAR);
		
		title.draw(canvas);
		
		soundButton.draw(canvas);
		musicButton.draw(canvas);
		resetButton.draw(canvas);
		backButton.draw(canvas);
		
		if (deletingData){
			menu.draw(canvas);
		}
	}
	
	/**
	 * Does the appropriate action when one of the buttons is clicked on.
	 */
	@Override
	protected void MotionEventDown(MotionEvent event){
		super.MotionEventDown(event);
		
		if (deletingData){
			menu.handleActionDown((int)event.getX(), (int)event.getY());
			
			if (menu.isTouched()){
				int selection = menu.getSelection();
				
				if (selection == DeleteDataMenu.YES){
					SoundManager.playSound(0, 1);
					SaveDataHandler.clearSaveData();
					deletingData = false;
					startThread();
				}else if (selection == DeleteDataMenu.NO){
					SoundManager.playSound(0, 1);
					deletingData = false;
					startThread();
				}
				
				menu.setTouched(false);
			}
		}else{
			soundButton.handleActionDown((int)event.getX(), (int)event.getY());
			musicButton.handleActionDown((int)event.getX(), (int)event.getY());
			resetButton.handleActionDown((int)event.getX(), (int)event.getY());
			backButton.handleActionDown((int)event.getX(), (int)event.getY());
			
			if (soundButton.isTouched()){
				SoundManager.playSound(0, 1);
				soundButton.setTouched(false);
				if (soundOn){
					soundOn = false;
					SoundManager.muteSound();
					soundButton.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.menu_options_buttonsoundoff));
					Log.d(TAG, "Turned off sound");
				}else{
					soundOn = true;
					SoundManager.unmuteSound();
					soundButton.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.menu_options_buttonsoundon));
					Log.d(TAG, "Turned on sound");
				}
				SaveDataHandler.storeOptionsData(soundOn, musicOn);
			}else if (musicButton.isTouched()){
				SoundManager.playSound(0, 1);
				musicButton.setTouched(false);
				if (musicOn){
					musicOn = false;
					musicButton.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.menu_options_buttonmusicoff));
					Log.d(TAG, "Turned off music");
				}else{
					musicOn = true;
					musicButton.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.menu_options_buttonmusicon));
					Log.d(TAG, "Turned on music");
				}
				SaveDataHandler.storeOptionsData(soundOn, musicOn);
			}else if (resetButton.isTouched()){
				SoundManager.playSound(0, 1);
				resetButton.setTouched(false);
				deletingData = true;
			}else if (backButton.isTouched()){
				SoundManager.playSound(0, 1);
				backButton.setTouched(false);
				activity.returnActivity();
			}
		}
		
		
	}
}












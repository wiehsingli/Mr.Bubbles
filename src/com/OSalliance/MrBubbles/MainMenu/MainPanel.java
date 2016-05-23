package com.OSalliance.MrBubbles.MainMenu;

import com.OSalliance.MrBubbles.R;
import com.OSalliance.MrBubbles.SoundManager.SoundManager;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.MotionEvent;

/**
 * The main menu stuff is taken care of here.
 */
public class MainPanel extends CustomPanel{
	private static final String TAG = MainPanel.class.getSimpleName();
	
	private MainActivity activity;
	
	private MenuButton title;
	
	private MenuButton startButton;
	private MenuButton optionsButton;
	private MenuButton creditsButton;
	
	public MainPanel(MainActivity activity){
		super(activity);
		
		this.activity = activity;
		
		// Set up the background stuff
		setBackgroundResource(R.drawable.background);
		getHolder().setFormat(PixelFormat.TRANSPARENT);
		setZOrderOnTop(true);
		
		int width = activity.getWindowManager().getDefaultDisplay().getWidth();
		int height = activity.getWindowManager().getDefaultDisplay().getHeight();
		Log.d(TAG, "WIDTH: " + width + ", HEIGHT: " + height);
		int xPos = width / 3;
		int yPos = height / 5;
		
		// Create the menu buttons.
		title = new MenuButton(this.getContext(), R.drawable.title_mrbubbles, 0, 0);
		title.setX(width / 2 - title.getWidth() / 2);
		title.setY(height / 8 - title.getHeight() / 2);
		startButton = new MenuButton(this.getContext(), R.drawable.menu_main_buttonplay, xPos, yPos * 2);
		startButton.setX(startButton.getX() - startButton.getWidth() / 2);
		startButton.setY(startButton.getY() - startButton.getHeight() / 2);
		optionsButton = new MenuButton(this.getContext(), R.drawable.menu_main_buttonoptions, xPos * 2, yPos * 3);
		optionsButton.setX(optionsButton.getX() - optionsButton.getWidth() / 2);
		optionsButton.setY(optionsButton.getY() - optionsButton.getHeight() / 2);
		creditsButton = new MenuButton(this.getContext(), R.drawable.menu_main_buttoncredits, xPos, yPos * 4);
		creditsButton.setX(creditsButton.getX() - creditsButton.getWidth() / 2);
		creditsButton.setY(creditsButton.getY() - creditsButton.getHeight() / 2);
	}

	/**
	 * Update each menu button.
	 */
	@Override
	public void update(){
		super.update();
		
		title.update();
		startButton.update();
		optionsButton.update();
		creditsButton.update();
	}
	
	/**
	 * Draw each menu button.
	 */
	@Override
	public void render(Canvas canvas){
		super.render(canvas);
		
		canvas.drawColor(0, PorterDuff.Mode.CLEAR);
		
		title.draw(canvas);
		startButton.draw(canvas);
		optionsButton.draw(canvas);
		creditsButton.draw(canvas);
	}
	
	/**
	 * Goes to the appropriate menu when one of the buttons is clicked on.
	 */
	@Override
	protected void MotionEventDown(MotionEvent event){
		super.MotionEventDown(event);
		
		startButton.handleActionDown((int)event.getX(), (int)event.getY());
		optionsButton.handleActionDown((int)event.getX(), (int)event.getY());
		creditsButton.handleActionDown((int)event.getX(), (int)event.getY());

		// If a button is touched, tell MainActivity to switch to appropriate menu.
		if (startButton.isTouched()){
			SoundManager.playSound(0, 1);
			startButton.setTouched(false);
			activity.changeActivity(MainActivity.LEVEL_MENU);
		}else if (optionsButton.isTouched()){
			SoundManager.playSound(0, 1);
			optionsButton.setTouched(false);
			activity.changeActivity(MainActivity.OPTIONS_MENU);
		}else if (creditsButton.isTouched()){
			SoundManager.playSound(0, 1);
			creditsButton.setTouched(false);
			activity.changeActivity(MainActivity.CREDITS_MENU);
		}
	}
}

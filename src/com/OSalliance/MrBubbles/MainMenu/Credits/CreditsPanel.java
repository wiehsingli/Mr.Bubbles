package com.OSalliance.MrBubbles.MainMenu.Credits;

import com.OSalliance.MrBubbles.R;
import com.OSalliance.MrBubbles.MainMenu.CustomPanel;
import com.OSalliance.MrBubbles.MainMenu.MenuButton;
import com.OSalliance.MrBubbles.SoundManager.SoundManager;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.view.MotionEvent;

/**
 * Handles creation of the credits menu.
 */
public class CreditsPanel extends CustomPanel{

	private CreditsActivity activity;
	
	private CreditsSprite[] sprites;
	
	private MenuButton backButton;
	
	public CreditsPanel(CreditsActivity activity) {
		super(activity);
		
		this.activity = activity;
		
		// Set up the background stuff.
		setBackgroundResource(R.drawable.background);
		getHolder().setFormat(PixelFormat.TRANSPARENT);
		setZOrderOnTop(true);
		
		int width = activity.getWindowManager().getDefaultDisplay().getWidth();
		int height = activity.getWindowManager().getDefaultDisplay().getHeight();
		
		// Create the buttons
		backButton = new MenuButton(this.getContext(), R.drawable.menu_main_buttonback, width / 6, height * 9 / 10);
		backButton.setX(backButton.getX() - backButton.getWidth() / 2);
		backButton.setY(backButton.getY() - backButton.getHeight() / 2);
		
		sprites = new CreditsSprite[3];
		sprites[0] = new CreditsSprite(this.getContext(), R.drawable.menu_credits_kevin, 0, 0);
		sprites[0].setX(randomInt(0, width - sprites[0].getWidth()));
		sprites[0].setYV(-5);
		sprites[1] = new CreditsSprite(this.getContext(), R.drawable.menu_credits_raymond, 0, 0);
		sprites[1].setX(randomInt(0, width - sprites[1].getWidth()));
		sprites[1].setYV(-5);
		sprites[2] = new CreditsSprite(this.getContext(), R.drawable.menu_credits_wilson, 0, 0);
		sprites[2].setX(randomInt(0, width - sprites[2].getWidth()));
		sprites[2].setYV(-5);
		
		// Randomize order in which credits appear
		for (int i = 0; i < 3; i++){
			int index = randomInt(0, 2);
			if (index != i){
				CreditsSprite sprite = sprites[i];
				sprites[i] = sprites[index];
				sprites[index] = sprite;
			}
		}
		sprites[0].setY(height);
		sprites[1].setY(height + height / 3);
		sprites[2].setY(height + height * 2 / 3);
	}
	
	/**
	 * Update each asset.
	 */
	@Override
	public void update(){
		super.update();
		
		backButton.update();
		
		for (int i = 0; i < sprites.length; i++){
			sprites[i].update();
			if (sprites[i].getY() < 0 - sprites[i].getHeight()){
				sprites[i].setY(getHeight());
				sprites[i].setX(randomInt(0, getWidth() - sprites[i].getWidth()));
			}
		}
	}
	
	/**
	 * Draw each asset.
	 */
	@Override
	public void render(Canvas canvas){
		super.render(canvas);
		
		canvas.drawColor(0, PorterDuff.Mode.CLEAR);
		
		sprites[0].draw(canvas);
		sprites[1].draw(canvas);
		sprites[2].draw(canvas);

		backButton.draw(canvas);
	}
	
	/**
	 * Does the appropriate action when one of the buttons is clicked on.
	 */
	@Override
	protected void MotionEventDown(MotionEvent event){
		super.MotionEventDown(event);
		
		backButton.handleActionDown((int)event.getX(), (int)event.getY());
		sprites[0].handleActionDown((int)event.getX(), (int)event.getY());
		sprites[1].handleActionDown((int)event.getX(), (int)event.getY());
		sprites[2].handleActionDown((int)event.getX(), (int)event.getY());
		
		if (backButton.isTouched()){
			SoundManager.playSound(0, 1);
			backButton.setTouched(false);
			activity.returnActivity();
			
			return;
		}
		
		for (int i = 2; i >= 0; i--){
			if (sprites[i].isTouched()){
				SoundManager.playSound(2, 1);
				sprites[i].setY(getHeight());
				sprites[i].setX(randomInt(0, getWidth() - sprites[i].getWidth()));
				sprites[0].setTouched(false);
				sprites[1].setTouched(false);
				sprites[2].setTouched(false);
				break;
			}
		}
	}
	
	private int randomInt(int min, int max){
		return min + (int)(Math.random() * ((max - min) + 1));
	}
	
}

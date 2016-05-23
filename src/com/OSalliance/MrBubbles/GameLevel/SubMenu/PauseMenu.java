package com.OSalliance.MrBubbles.GameLevel.SubMenu;

import com.OSalliance.MrBubbles.R;
import com.OSalliance.MrBubbles.SoundManager.SoundManager;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class PauseMenu extends MenuItem{
	
	public static final int NO_SELECTION = 0;
	public static final int RESUME = 1;
	public static final int RESTART = 2;
	public static final int LEVEL_SELECT = 3;
	public static final int MAIN_MENU = 4;
	
	private MenuItem resumeB;
	private MenuItem restartB;
	private MenuItem levelSelectB;
	private MenuItem mainMenuB;
	
	public PauseMenu(Context context, double x, double y) {
		super(context, R.drawable.menu_item_back_paused, x, y);
		
		resumeB = new MenuItem(context, R.drawable.menu_item_button_resume, 0, 0);
		restartB = new MenuItem(context, R.drawable.menu_item_button_restart, 0, 0);
		levelSelectB = new MenuItem(context, R.drawable.menu_item_button_levelselect, 0, 0);
		mainMenuB = new MenuItem(context, R.drawable.menu_item_button_mainmenu, 0, 0);
	}
	
	@Override
	public void setX(double x){
		super.setX(x);
		
		int xPad = getWidth() / 6;
		
		resumeB.setX(x + xPad / 2);
		restartB.setX(x + xPad / 2);
		levelSelectB.setX(x + xPad / 2);
		mainMenuB.setX(x + xPad / 2);
	}
	
	@Override
	public void setY(double y){
		super.setY(y);
		
		int yPad = getHeight() / 6;
		
		resumeB.setY(y + yPad * 2 - resumeB.getHeight() / 2);
		restartB.setY(y + yPad * 3 - restartB.getHeight() / 2);
		levelSelectB.setY(y + yPad * 4 - levelSelectB.getHeight() / 2);
		mainMenuB.setY(y + yPad * 5 - mainMenuB.getHeight() / 2);
	}
	
	@Override
	public void handleActionDown(int eventX, int eventY){
		super.handleActionDown(eventX, eventY);
		
		if (isTouched()){
			resumeB.handleActionDown(eventX, eventY);
			restartB.handleActionDown(eventX, eventY);
			levelSelectB.handleActionDown(eventX, eventY);
			mainMenuB.handleActionDown(eventX, eventY);
		}
	}
	
	public int getSelection(){
		if (isTouched()){
			if (resumeB.isTouched()){
				SoundManager.playSound(0, 1);
				resumeB.setTouched(false);
				return RESUME;
			}else if (restartB.isTouched()){
				SoundManager.playSound(0, 1);
				restartB.setTouched(false);
				return RESTART;
			}else if (levelSelectB.isTouched()){
				SoundManager.playSound(0, 1);
				levelSelectB.setTouched(false);
				return LEVEL_SELECT;
			}else if (mainMenuB.isTouched()){
				SoundManager.playSound(0, 1);
				mainMenuB.setTouched(false);
				return MAIN_MENU;
			}
		}
		
		return NO_SELECTION;
	}
	
	@Override
	public void draw(Canvas canvas){
		super.draw(canvas);
		
		resumeB.draw(canvas);
		restartB.draw(canvas);
		levelSelectB.draw(canvas);
		mainMenuB.draw(canvas);
	}

}








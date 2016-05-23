package com.OSalliance.MrBubbles.GameLevel.SubMenu;

import com.OSalliance.MrBubbles.R;
import com.OSalliance.MrBubbles.SoundManager.SoundManager;

import android.content.Context;
import android.graphics.Canvas;

public class FailMenu extends MenuItem{
	
	public static final int NO_SELECTION = 0;
	public static final int REPLAY = 1;
	public static final int LEVEL_SELECT = 2;
	public static final int MAIN_MENU = 3;
	
	private MenuItem replayB;
	private MenuItem levelSelectB;
	private MenuItem mainMenuB;
	
	public FailMenu(Context context, double x, double y) {
		super(context, R.drawable.menu_item_back_failed, x, y);
		
		int xPad = getWidth() / 6;
		int yPad = getHeight() / 5;
		
		replayB = new MenuItem(context, R.drawable.menu_item_button_replay, 0, 0);
		replayB.setX(xPad / 2);
		replayB.setY(yPad * 2 - replayB.getHeight() / 2);
		levelSelectB = new MenuItem(context, R.drawable.menu_item_button_levelselect, 0, 0);
		levelSelectB.setX(xPad / 2);
		levelSelectB.setY(yPad * 3 - levelSelectB.getHeight() / 2);
		mainMenuB = new MenuItem(context, R.drawable.menu_item_button_mainmenu, 0, 0);
		mainMenuB.setX(xPad / 2);
		mainMenuB.setY(yPad * 4 - mainMenuB.getHeight() / 2);
	}
	
	@Override
	public void setX(double x){
		super.setX(x);
		
		int xPad = getWidth() / 6;
		
		replayB.setX(x + xPad / 2);
		levelSelectB.setX(x + xPad / 2);
		mainMenuB.setX(x + xPad / 2);
	}
	
	@Override
	public void setY(double y){
		super.setY(y);
		
		int yPad = getHeight() / 5;
		
		replayB.setY(y + yPad * 2 - replayB.getHeight() / 2);
		levelSelectB.setY(y + yPad * 3 - levelSelectB.getHeight() / 2);
		mainMenuB.setY(y + yPad * 4 - mainMenuB.getHeight() / 2);
	}
	
	@Override
	public void handleActionDown(int eventX, int eventY){
		super.handleActionDown(eventX, eventY);
		
		if (isTouched()){
			replayB.handleActionDown(eventX, eventY);
			levelSelectB.handleActionDown(eventX, eventY);
			mainMenuB.handleActionDown(eventX, eventY);
		}
	}
	
	public int getSelection(){
		if (isTouched()){
			if (replayB.isTouched()){
				SoundManager.playSound(0, 1);
				replayB.setTouched(false);
				return REPLAY;
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
		
		replayB.draw(canvas);
		levelSelectB.draw(canvas);
		mainMenuB.draw(canvas);
	}

}









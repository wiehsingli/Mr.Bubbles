package com.OSalliance.MrBubbles.GameLevel.SubMenu;

import com.OSalliance.MrBubbles.R;
import com.OSalliance.MrBubbles.SoundManager.SoundManager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

public class VictoryMenu extends MenuItem{
	

	private static final String TAG = VictoryMenu.class.getSimpleName();
	
	public static final int NO_SELECTION = 0;
	public static final int REPLAY = 1;
	public static final int NEXT_LEVEL = 2;
	public static final int LEVEL_SELECT = 3;
	public static final int MAIN_MENU = 4;
	
	private MenuItem replayB;
	private MenuItem nextLevelB;
	private MenuItem levelSelectB;
	private MenuItem mainMenuB;
	
	private Context context;
	
	private MenuItem scoreItem;
	
	private int score = 0;
	
	public VictoryMenu(Context context, double x, double y) {
		super(context, R.drawable.menu_item_back_victory, x, y);
		
		this.context = context;
		
		replayB = new MenuItem(context, R.drawable.menu_item_button_replay, 0, 0);
		nextLevelB = new MenuItem(context, R.drawable.menu_item_button_nextlevel, 0, 0);
		levelSelectB = new MenuItem(context, R.drawable.menu_item_button_levelselect, 0, 0);
		mainMenuB = new MenuItem(context, R.drawable.menu_item_button_mainmenu, 0, 0);
	}
	
	@Override
	public void setX(double x){
		super.setX(x);
		
		int xPad = getWidth() / 6;
		
		replayB.setX(x + xPad / 2);
		nextLevelB.setX(x + xPad / 2);
		levelSelectB.setX(x + xPad / 2);
		mainMenuB.setX(x + xPad / 2);
	}
	
	@Override
	public void setY(double y){
		super.setY(y);
		
		int yPad = getHeight() / 6;
		
		replayB.setY(y + yPad * 2 - replayB.getHeight() / 2);
		nextLevelB.setY(y + yPad * 3 - nextLevelB.getHeight() / 2);
		levelSelectB.setY(y + yPad * 4 - levelSelectB.getHeight() / 2);
		mainMenuB.setY(y + yPad * 5 - mainMenuB.getHeight() / 2);
	}
	
	@Override
	public void handleActionDown(int eventX, int eventY){
		super.handleActionDown(eventX, eventY);
		
		if (isTouched()){
			replayB.handleActionDown(eventX, eventY);
			nextLevelB.handleActionDown(eventX, eventY);
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
			}else if (nextLevelB.isTouched()){
				SoundManager.playSound(0, 1);
				nextLevelB.setTouched(false);
				return NEXT_LEVEL;
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
	
	public void setLives(int numLives){
		if (score != numLives){
			score = numLives;
			
			Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.menu_item_score).copy(Bitmap.Config.ARGB_8888, true);
			
			int width = bitmap.getWidth();
			Log.d(TAG, "Width before: " + width);
			if (numLives == 1){
				width /= 5;
			}else if (numLives == 2){
				width *= (3.0 / 5.0);
			}
			Log.d(TAG, "Width after: " + width);
			
			Bitmap scoreBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, bitmap.getHeight());
			
			scoreItem = new MenuItem(context, scoreBitmap, getX() + getWidth() / 2 - scoreBitmap.getWidth() / 2, getY() + getHeight() / 6);
		}
	}
	
	@Override
	public void draw(Canvas canvas){
		super.draw(canvas);
		
		replayB.draw(canvas);
		nextLevelB.draw(canvas);
		levelSelectB.draw(canvas);
		mainMenuB.draw(canvas);
		
		scoreItem.draw(canvas);
	}

}
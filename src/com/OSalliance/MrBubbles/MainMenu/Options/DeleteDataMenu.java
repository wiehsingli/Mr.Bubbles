package com.OSalliance.MrBubbles.MainMenu.Options;

import com.OSalliance.MrBubbles.R;
import com.OSalliance.MrBubbles.GameLevel.SubMenu.MenuItem;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class DeleteDataMenu extends MenuItem{
	
	public static final int NO_SELECTION = 0;
	public static final int YES = 1;
	public static final int NO = 2;
	
	private MenuItem yesB;
	private MenuItem noB;
	
	public DeleteDataMenu(Context context, double x, double y) {
		super(context, R.drawable.menu_item_back_deletedata, x, y);
		
		yesB = new MenuItem(context, R.drawable.menu_item_button_yes, 0, 0);
		noB = new MenuItem(context, R.drawable.menu_item_button_no, 0, 0);
	}
	
	@Override
	public void setX(double x){
		super.setX(x);
		
		int xPad = getWidth() / 6;
		
		yesB.setX(x + xPad / 2);
		noB.setX(x + xPad / 2);
	}
	
	@Override
	public void setY(double y){
		super.setY(y);
		
		int yPad = getHeight() / 5;
		
		yesB.setY(y + yPad * 3 - yesB.getHeight() / 2);
		noB.setY(y + yPad * 4 - noB.getHeight() / 2);
	}
	
	@Override
	public void handleActionDown(int eventX, int eventY){
		super.handleActionDown(eventX, eventY);
		
		if (isTouched()){
			yesB.handleActionDown(eventX, eventY);
			noB.handleActionDown(eventX, eventY);
		}
	}
	
	public int getSelection(){
		if (isTouched()){
			if (yesB.isTouched()){
				yesB.setTouched(false);
				return YES;
			}else if (noB.isTouched()){
				noB.setTouched(false);
				return NO;
			}
		}
		
		return NO_SELECTION;
	}
	
	@Override
	public void draw(Canvas canvas){
		super.draw(canvas);
		
		yesB.draw(canvas);
		noB.draw(canvas);
	}

}








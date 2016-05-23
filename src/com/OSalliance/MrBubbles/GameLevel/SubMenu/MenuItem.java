package com.OSalliance.MrBubbles.GameLevel.SubMenu;

import android.content.Context;
import android.graphics.Bitmap;

import com.OSalliance.MrBubbles.GameLevel.Sprites.Sprite;

public class MenuItem extends Sprite{

	private boolean touched;

	public MenuItem(Context context, int drawableID, double x, double y) {
		super(context, drawableID, x, y);
	}
	
	public MenuItem(Context context, Bitmap bitmap, double x, double y){
		super(context, bitmap, x, y);
	}
	
	/**
	 * Checks to see if it is clicked on.
	 * 
	 * @param eventX	The x position of the click.
	 * @param eventY	The y position of the click.
	 */
	public void handleActionDown(int eventX, int eventY){
		if (eventX >= getX() && eventX <= (getX() + getWidth())){
			if (eventY >= getY() && eventY <= (getY() + getHeight())){
				setTouched(true); // droid touched
			}else{
				setTouched(false);
			}
		}else{
			setTouched(false);
		}
	}
	
	public boolean isTouched(){
		return touched;
	}
	
	public void setTouched(boolean touched){
		this.touched = touched;
	}

}

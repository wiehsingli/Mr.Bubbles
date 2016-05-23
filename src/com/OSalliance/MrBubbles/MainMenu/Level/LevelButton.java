package com.OSalliance.MrBubbles.MainMenu.Level;

import android.content.Context;
import android.graphics.Bitmap;

import com.OSalliance.MrBubbles.GameLevel.Sprites.Sprite;

public class LevelButton extends Sprite{

	private boolean touched;
	
	public LevelButton(Context context, Bitmap bitmap, double x, double y) {
		super(context, bitmap, x, y);
		
		touched = false;
	}
	
	@Override
	protected void updateHitbox() {
		double radius = getWidth() / 2;
		double halfLength = radius * Math.sin(Math.PI/4); // half the length of a square
		
		hitbox.left = (int)(centerX() - halfLength);
		hitbox.right = (int)(centerX() + halfLength);
		hitbox.top = (int)(centerY() - halfLength);
		hitbox.bottom = (int)(centerY() + halfLength);
	}
	
	/**
	 * Checks to see if it is clicked on.
	 * 
	 * @param eventX	The x position of the click.
	 * @param eventY	The y position of the click.
	 */
	public void handleActionDown(int eventX, int eventY){
		if (eventX >= getHitbox().left && eventX <= getHitbox().right){
			if (eventY >= getHitbox().top && eventY <= getHitbox().bottom){
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

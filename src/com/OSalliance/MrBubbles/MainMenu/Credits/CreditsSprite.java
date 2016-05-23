package com.OSalliance.MrBubbles.MainMenu.Credits;

import android.content.Context;
import android.graphics.Bitmap;

import com.OSalliance.MrBubbles.GameLevel.Sprites.Sprite;

public class CreditsSprite extends Sprite{
	
	private boolean touched;
	
	public CreditsSprite(Context context, int drawableID, double x, double y) {
		super(context, drawableID, x, y);
		
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
				setTouched(true);
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

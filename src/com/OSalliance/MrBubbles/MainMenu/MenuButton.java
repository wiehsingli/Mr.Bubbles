package com.OSalliance.MrBubbles.MainMenu;

import android.content.Context;
import android.graphics.Bitmap;

import com.OSalliance.MrBubbles.GameLevel.Sprites.Sprite;

/**
 * A menu button that takes care of its own floating motion.
 */
public class MenuButton extends Sprite{
	
	private boolean touched;
	
	public MenuButton(Context context, int drawableID, double x, double y) {
		super(context, drawableID, x, y);
		touched = false;
		setYA(0.0125);
		
		double vel = (int)(Math.random() * 50) + 1;
		vel = (int)(vel / 5) * 5;
		vel /= 100;
		setYV(vel);
		
		int dir = (int)(Math.random() * 100) + 1;
		if (dir > 50){
			toggleYDirection();
		}
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
	
	@Override
	public void update(){
		double yv = getYV();
		
		if (yv >= 0.5 || yv <= -0.5){
			toggleYDirection();
		}
		
		
		super.update();
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
}

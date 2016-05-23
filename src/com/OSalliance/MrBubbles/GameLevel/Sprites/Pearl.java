package com.OSalliance.MrBubbles.GameLevel.Sprites;

import com.OSalliance.MrBubbles.R;
import com.OSalliance.MrBubbles.GameLevel.Sprites.Enemies.Enemy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Pearl class applies pearl image to the bitmap
 * Lives of the game
 * @author OSAlliance 
 *
 */
public class Pearl extends Sprite{
	
	private double initialY;
	private Enemy enemy;
	private boolean caught;
	private boolean invulnerable;
	
	public Pearl(Context context, double x, double y) {
		super(context, R.drawable.gold_pearl, x, y);
		initialY = y;
		enemy = null;
		caught = false;
		invulnerable = false;
	}
	
	/**
	 * Gets the width of the bitmap.
	 * 
	 * @param context The context of the bitmap.
	 * @return The width of the bitmap.
	 */
	public static int bitmapWidth(Context context) {
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.pearl);
		
		return bitmap.getWidth();
	}
	
	/**
	 * Gets the height of the bitmap.
	 * 
	 * @param context The context of the bitmap.
	 * @return The height of the bitmap.
	 */
	public static int bitmapHeight(Context context) {
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.pearl);
		
		return bitmap.getHeight();
	}
	
	public double getInitialY() {
		return initialY;
	}
	
	public boolean isCaught(){
		return caught;
	}
	
	public boolean isInvulnerable() {
		return invulnerable;
	}
	
	public void setCaught(Enemy enemy) {
		caught = true;
		this.enemy = enemy;
	}
	
	public void setUncaught() {
		caught = false;
		enemy = null;
		invulnerable = true;
	}
	
	public void setVulnerable() {
		invulnerable = false;
	}
	
	@Override
	public void update() {
		if (caught) {
			setX(enemy.centerX() - getWidth()/2);
			setY(enemy.getHitbox().bottom);
		}
		
		else if (getY() < initialY) {
			setYA(0.07);
		}
		
		else {
			setYA(0);
			setYV(0);
			setY(initialY);
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

package com.OSalliance.MrBubbles.GameLevel.Sprites.Enemies;

import com.OSalliance.MrBubbles.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * Applies jellyfish image to bitmap;
 * applies appropriate image when caught in bubble
 * Sets any unique abilities
 * @author OSAlliance
 *
 */
public class Jellyfish extends Enemy {
	private static final String TAG = Jellyfish.class.getSimpleName();
	
	private Context context;
	
	public Jellyfish(Context context, double x, double y, double fallSpeed) {
		super(context, R.drawable.enemy_jelly, x, y, fallSpeed);
		
		this.context =  context;
	}
	
	public Jellyfish(Jellyfish copy, double fallSpeed) {
		super(copy, fallSpeed);
		
		context = copy.context;
	}
	
	/**
	 * Detects if enemy is captured and applies appropriate image
	 * 
	 * @param ammoType string to identify specific projectile
	 */
	@Override
	public void setCaptured(String ammoType) {
		if (ammoType.equals("B_Bubble")) {
			setBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy_jelly_blue));
		}
		
		else if (ammoType.equals("R_Bubble")) {
			setBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy_jelly_red));
		}
		
		else if (ammoType.equals("P_Bubble")) {
			setBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy_jelly_purple));
		}
		else if (ammoType.equals("G_Bubble")) {
			setBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy_jelly_green));
		}
		else if (ammoType.equals("W_Bubble")) {
			setBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy_jelly_blue));
		}
		
		else {
			Log.e(TAG, "Cannot determine ammo type");
		}
		
		super.setCaptured(ammoType);
	}
	/**
	 * updates and sets attributes of jellyfish
	 */
	@Override
	public void update(){
		if (!isDead()) {
			setYV(getFallSpeed());
		}
		
		super.update();
	}
	
	@Override
	protected void updateHitbox() {
		hitbox.left = (int)(getX() + 2);
		hitbox.right = (int)(getX() + getWidth() - 2);
		hitbox.top = (int)(getY() + 2);
		hitbox.bottom = (int)(getY() + getHeight() - 8);
	}
}

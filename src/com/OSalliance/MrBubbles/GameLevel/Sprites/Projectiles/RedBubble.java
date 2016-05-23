package com.OSalliance.MrBubbles.GameLevel.Sprites.Projectiles;

import com.OSalliance.MrBubbles.R;
import com.OSalliance.MrBubbles.GameLevel.GameLogic.CollisionDetector;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
/**
 * Red bubble class applies the image to the bitmap
 * Provides any unique behavior
 * @author OSAlliance
 *
 */
public class RedBubble extends Projectile {
	
	private static final String TAG = CollisionDetector.class.getSimpleName();
	
	private int bounceCount;
	
	public RedBubble(Context context, double bx, double by, double px, double py) {
		super(context, R.drawable.red_bubble, "R_Bubble", bx, by, px, py);
		// TODO Auto-generated constructor stub
		bounceCount = 3;
		nx = vectorX/(mag/5);			
		ny = vectorY/(mag/5);			//dividing vector to a certain length for animating
		setXV(nx); setYV(ny);
	}

	public int getBounceCount() {
		return bounceCount;
	}
	
	public void decrementBounceCount() {
		bounceCount--;
	}
	
	@Override
	public void update() {
		super.update();
	}
}
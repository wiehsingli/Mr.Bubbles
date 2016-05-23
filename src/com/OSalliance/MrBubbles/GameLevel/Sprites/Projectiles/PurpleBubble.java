package com.OSalliance.MrBubbles.GameLevel.Sprites.Projectiles;

import com.OSalliance.MrBubbles.R;

import android.content.Context;
import android.graphics.BitmapFactory;
/**
 * Purple bubble class applies the image to the bitmap
 * Provides unique behavior
 * Shoots at a higher Velocity
 * @author OSAlliance
 *
 */
public class PurpleBubble extends Projectile {

	public PurpleBubble(Context context, double bx, double by, double px, double py) {
		super(context, R.drawable.purple_bubble, "P_Bubble", bx, by, px, py);
		// TODO Auto-generated constructor stub
		
		nx = vectorX/(mag/20);			//
		ny = vectorY/(mag/20);			//dividing vector to a certain length for animating
		setXV(nx); setYV(ny);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
		super.update();
		
	}
}
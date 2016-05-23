package com.OSalliance.MrBubbles.GameLevel.Sprites.Projectiles;

import com.OSalliance.MrBubbles.R;

import android.content.Context;
import android.graphics.BitmapFactory;

/**
 * Blue bubble class applies the image to the bitmap;
 * No unique abilities; Considered as default projectile
 * @author OSAlliance
 *
 */
public class BlueBubble extends Projectile {

	public BlueBubble(Context context, double bx, double by, double px, double py) {
		super(context, R.drawable.default_projectile, "B_Bubble", bx, by, px, py);
		// TODO Auto-generated constructor stub
		nx = vectorX/(mag/5);			//
		ny = vectorY/(mag/5);			//dividing vector to a certain length for animating
		setXV(nx); setYV(ny);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		super.update();
	}
}
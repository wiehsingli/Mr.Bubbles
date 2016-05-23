package com.OSalliance.MrBubbles.GameLevel.Sprites.Projectiles;

import com.OSalliance.MrBubbles.R;
import android.content.Context;
import android.graphics.BitmapFactory;

public class WhiteBubble extends Projectile {
	private boolean isGhost;
	private double targetHeight;
	
	public WhiteBubble(Context context, double bx, double by, double px, double py) {
		super(context, R.drawable.default_projectile, "W_Bubble", bx, by, px, py);
		nx = vectorX/(mag/5);			//
		ny = vectorY/(mag/5);			//dividing vector to a certain length for animating
		setXV(nx); setYV(ny);
		
		isGhost = true;
		targetHeight = by;
	}
	
	public boolean isGhost() {
		return isGhost;
	}
	
	public void setGhost(boolean ghost) {
		isGhost = ghost;
	}
	
	public double getTargetHeight() {
		return targetHeight;
	}

	@Override
	public void update() {
		super.update();
	}
}

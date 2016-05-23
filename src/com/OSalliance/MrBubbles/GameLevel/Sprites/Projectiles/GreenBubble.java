package com.OSalliance.MrBubbles.GameLevel.Sprites.Projectiles;

import com.OSalliance.MrBubbles.R;

import android.content.Context;
import android.graphics.BitmapFactory;

public class GreenBubble extends Projectile {

	private int distance;
	
	public GreenBubble(Context context, double bx, double by, double px, double py) {
		super(context, R.drawable.green_bubble, "G_Bubble", bx, by, px, py);
		// TODO Auto-generated constructor stub
		
		distance = 100;
		nx = vectorX/(mag/7);			//
		ny = vectorY/(mag/7);			//dividing vector to a certain length for animating
		setXV(nx); setYV(ny);
	}

	public int getDistance(){
		return distance;
	}
	
	@Override
	public void update() {
		// TODO Auto-generated method stub
		super.update();
	}
}

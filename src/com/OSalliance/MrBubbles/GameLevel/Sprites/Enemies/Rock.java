package com.OSalliance.MrBubbles.GameLevel.Sprites.Enemies;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.OSalliance.MrBubbles.R;

public class Rock extends Enemy {
	private static final String TAG = Rock.class.getSimpleName();
	
	private Context context;
	
	public Rock(Context context, double x, double y, double fallSpeed) {
		super(context, R.drawable.rock, x, y, fallSpeed);
		
		setYV(getFallSpeed());
		this.context =  context;
	}
	
	public Rock(Rock copy, double fallSpeed) {
		super(copy, fallSpeed);
		
		context = copy.context;
	}
	
	@Override
	protected void updateHitbox() {
		hitbox.left = (int)(getX() + 2);
		hitbox.right = (int)(getX() + getWidth() - 2);
		hitbox.top = (int)(getY() + 2);
		hitbox.bottom = (int)(getY() + getHeight() - 8);
	}
}

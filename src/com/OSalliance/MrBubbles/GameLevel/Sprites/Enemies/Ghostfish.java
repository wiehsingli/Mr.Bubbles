package com.OSalliance.MrBubbles.GameLevel.Sprites.Enemies;


import com.OSalliance.MrBubbles.R;
import com.OSalliance.MrBubbles.GameLevel.GameLogic.CollisionDetector;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class Ghostfish extends Enemy {
	
	private static final String TAG = Seahorse.class.getSimpleName();


	private Context context;
	private Bitmap ghostfish;
	
	
	public Ghostfish(Context context, int drawableID, double x, double y,
			double fallSpeed) {
		super(context, drawableID, x, y, fallSpeed);
		// TODO Auto-generated constructor stub
		
		this.context =  context;
		
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
		
		else {
			Log.e(TAG, "Cannot determine ammo type");
		}
		
		super.setCaptured(ammoType);
	}
	
	@Override
	public void update(){
		//checks if object collides with screen boundaries
				//sets attributes
		if (!isDead()) {
			setYV(getFallSpeed());

			if((CollisionDetector.hitTestLeftWall(this))){		//resets velocity once object contacts left wall
//				setX(0);
//				toggleXDirection();
				toggleXVelocity();
//				setXV(0);
			}
			else if(CollisionDetector.hitTestRightWall(this)){		//resets velocity once object contacts right wall
				setX(CollisionDetector.getScreenWidth()- getWidth());
//				toggleXDirection();
				toggleXVelocity();
//				setXV(0);
			}

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

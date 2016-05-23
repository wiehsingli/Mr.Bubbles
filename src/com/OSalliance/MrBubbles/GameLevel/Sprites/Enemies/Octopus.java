package com.OSalliance.MrBubbles.GameLevel.Sprites.Enemies;

import com.OSalliance.MrBubbles.R;
import com.OSalliance.MrBubbles.GameLevel.GameLogic.CollisionDetector;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * Applies octopus image to bitmap;
 * applies appropriate image when caught in bubble
 * Sets any unique abilities
 * 
 * @author OSAlliance
 *
 */
public class Octopus extends Enemy {
	private static final String TAG = Jellyfish.class.getSimpleName();

	private Context context;

	public Octopus(Context context, double x, double y, double fallSpeed) {
		super(context, R.drawable.oct, x, y, fallSpeed);
		
		if (Math.random() >= 0.5) {
			setXV(1);	
		}
		
		else {
			setXV(-1);
			toggleXDirection();
		}
		
		this.context = context;
	}

	public Octopus(Octopus copy, double fallSpeed) {
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
			setBitmap(BitmapFactory.decodeResource(context.getResources(),
					R.drawable.oct_caught));
		} 
		
		else if (ammoType.equals("R_Bubble")) {
			setBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.oct_caught_rbubble));
		}
		
		else if (ammoType.equals("P_Bubble")) {
			setBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.oct_caught_pbubble));
		}
		
		else if (ammoType.equals("G_Bubble")) {
			setBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.oct_caught_gbubble));
		}
		else if (ammoType.equals("W_Bubble")) {
			setBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.oct_caught));
		}
		else {
			Log.e(TAG, "Cannot determine ammo type");
		}

		super.setCaptured(ammoType);
	}

	/**
	 * updates and sets attributes of octopus
	 */
	@Override
	public void update() {

		//checks if object collides with screen boundaries
		//sets attributes
		if (!isDead()) {
			setYV(getFallSpeed());
			if (getXV() > 2 || getXV() < -2) {		//reverses x direction once a certain velocity is reached
				toggleXDirection();
			}
			else if((CollisionDetector.hitTestLeftWall(this))){		//resets velocity once object contacts left wall
				setX(0);
				toggleXDirection();
				setXV(0);
			}
			else if(CollisionDetector.hitTestRightWall(this)){		//resets velocity once object contacts right wall
				setX(CollisionDetector.getScreenWidth()- getWidth());
				toggleXDirection();
				setXV(0);
			}
			else {							
				setXA(0.06);		//sets x acceleration
			}
		}
		super.update();
	}
	
	@Override
	protected void updateHitbox() {
		hitbox.left = (int)(getX() + 5);
		hitbox.right = (int)(getX() + getWidth() - 5);
		hitbox.top = (int)(getY() + 2);
		hitbox.bottom = (int)(getY() + getHeight() - 3);
	}
}

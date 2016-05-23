package com.OSalliance.MrBubbles.GameLevel.Sprites.Enemies;

import com.OSalliance.MrBubbles.GameLevel.GamePanel;
import com.OSalliance.MrBubbles.GameLevel.Sprites.Pearl;
import com.OSalliance.MrBubbles.GameLevel.Sprites.Sprite;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

/**
 * Class that all unqiue enemies will follow;
 * extends Sprite class
 * @author OSAlliance
 *
 */
public class Enemy extends Sprite {
	private static final String TAG = GamePanel.class.getSimpleName();
	private Pearl life;
	private boolean isDead;
	private boolean hasLife;
	private boolean hasTarget;
	private boolean isSpawned;
	private boolean noMoreLife;
	private double nx, ny;
	private double fallSpeed;
	
	public Enemy(Context context, int drawableID, double x, double y, double fallSpeed) {
		super(context, drawableID, x, y);
		
		life = null;
		isDead = false;
		hasLife = false;
		hasTarget = false;
		isSpawned = false;
		noMoreLife = false;
		nx = 0;
		ny = 0;
		this.fallSpeed = fallSpeed;
	}
	
	public Enemy(Enemy copy, double fallSpeed) {
		super(copy);
		
		life = copy.life;
		isDead = copy.isDead;
		hasLife = copy.hasLife;
		hasTarget = copy.hasTarget;
		isSpawned = copy.isSpawned;
		noMoreLife = copy.noMoreLife;
		nx = copy.nx;
		ny = copy.ny;
		this.fallSpeed = fallSpeed;
	}
	
	public boolean hasLife() {
		return hasLife;
	}
	
	public boolean isDead() {
		return isDead;
	}
	
	public boolean isSpawned() {
		return isSpawned;
	}
	
	public void setCaptured(String ammoType) {
		isDead = true;
		
		if (life != null) {
			life.setUncaught();
		}
	}
	
	public void setSpawned() {
		isSpawned = true;
	}

	public void setTarget(double lx, double ly) {
		double vecX, vecY, mag;
		
		hasTarget = true;
		vecX = lx - centerX();
		vecY = ly - getHitbox().bottom;
		mag = Math.sqrt((vecX*vecX + vecY*vecY));
		nx = vecX/(mag/4);			//
		ny = vecY/(mag/4);			//dividing vector to a certain length for animating
	}
	
	public void setNoTarget() {
		hasTarget = false;
	}
	
	public void setHasLife(Pearl life) {
		this.life = life;
		hasLife = true;
		hasTarget = false;
	}
	
	public void setNoMoreLife( boolean noMoreLife){
		this.noMoreLife = noMoreLife;
	}
	
	public double getFallSpeed() {
		return fallSpeed;
	}
	
	/**
	 * updates attributes of all enemies
	 */
	@Override
	public void update(){

		if (isDead) {
			setXA(0);
			setXV(0);
			setYA(-0.07);
		}
		
		else if (hasTarget) {
			setYV(ny);
			setXV(nx);
		}
		
		else if (hasLife) {
			
			setXA(0);
			setXV(0);
			setYA(0);
			setYV(-2);
		}
		
		else if (noMoreLife) {
			toggleYDirection();
			toggleYVelocity();
		}
		
		super.update();
	}
}

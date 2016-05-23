package com.OSalliance.MrBubbles.GameLevel.Sprites.Projectiles;

import com.OSalliance.MrBubbles.GameLevel.Sprites.Sprite;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Class that all projectiles to follow; extends sprite class
 * @author OSAlliance
 *
 */
public class Projectile extends Sprite {
	
	private boolean touched;
	private String ammoType;
	protected double vectorX, vectorY, mag, nx, ny;
	
	public Projectile(Context context, int drawableID, String ammoType, double bx, double by, double px, double py) {
		super(context, drawableID, 0, 0);
		this.setX(px - this.getBitmap().getWidth() / 2);
		this.setY(py - this.getBitmap().getHeight() / 2);
		
		this.ammoType = ammoType;
		touched = false;
		vectorX = (bx - px);
		vectorY = (by - py);
		mag = Math.sqrt((vectorX*vectorX + vectorY*vectorY));
//		nx = vectorX/(mag/5);			//
//		ny = vectorY/(mag/5);			//dividing vector to a certain length for animating
//		setXV(nx); setYV(ny);
	}
	
	public double getNX(){
		return nx;
	}
	public double getNY(){
		return ny;
	}
	
	public String getAmmoType() {
		return ammoType;
	}

	public boolean getTouched(){
		return touched;
	}
	
	public void setTouched(boolean touched){
		this.touched = touched;
	}
	
	public void setNX( double nx ){
		this.nx = nx;
	}
	
	public void setNY( double ny ){
		this.ny = ny;
	}
	
	/**
	 * updates attributes of all projectiles
	 */
	@Override
	public void update() {
		super.update();		//calls sprite.update
	}
	
	@Override
	protected void updateHitbox() {
		double radius = getWidth() / 2;
		double halfLength = radius * Math.sin(Math.PI/4); // half the length of a square
		
		hitbox.left = (int)(centerX() - halfLength);
		hitbox.right = (int)(centerX() + halfLength);
		hitbox.top = (int)(centerY() - halfLength);
		hitbox.bottom = (int)(centerY() + halfLength);
	}
	
	@Override
	public void draw(Canvas canvas){
		super.draw(canvas);
	}
}

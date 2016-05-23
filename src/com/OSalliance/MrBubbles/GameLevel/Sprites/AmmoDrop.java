package com.OSalliance.MrBubbles.GameLevel.Sprites;

import com.OSalliance.MrBubbles.R;

import android.content.Context;
import android.graphics.BitmapFactory;
/**
 * Applies ammo drop image to the bitmap
 * sets velocity of the ammo drop
 * @author OSAlliance
 *
 */
public class AmmoDrop extends Sprite {
	private String ammoType;
	private double fallSpeed;
	
	public AmmoDrop(Context context, int x, int y, String type, double fallSpeed) {
		super(context, R.drawable.ammo_drop_blue, x, y);
		
		ammoType = type;
		this.fallSpeed = fallSpeed;
		
		if (ammoType.equals("P_Bubble")){
			setBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.ammo_drop_purple));
		}else if (ammoType.equals("R_Bubble")){
			setBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.ammo_drop_red));
		}
	}
	
	/**
	 * copies the ammo drop object
	 * @param copy copy of the sprite
	 */
	public AmmoDrop(AmmoDrop copy, double fallSpeed) {
		super(copy);
		
		ammoType = copy.ammoType;
		this.fallSpeed = fallSpeed;
	}
	
	public String getAmmoType() {
		return ammoType;
	}
	
	public double getFallSpeed() {
		return fallSpeed;
	}
	
	/**
	 * updates position of the ammo drop as well as setting y velocity
	 */
	public void update(){
		setYV(fallSpeed);
		super.update();
	}

}

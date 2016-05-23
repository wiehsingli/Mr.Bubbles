package com.OSalliance.MrBubbles.GameLevel.AmmoBar;

import android.content.Context;
import android.widget.ImageView;

public class AmmoSlot extends ImageView{

	String ammoType;
	int ammoCount;
	
	public AmmoSlot(Context context) {
		super(context);
		
		ammoType = "";
		ammoCount = 0;
	}
	
	public String getAmmoType(){
		return ammoType;
	}
	
	public int getAmmoCount(){
		return ammoCount;
	}
	
	public void setAmmoType(String ammoType){
		this.ammoType = ammoType;
	}
	
	public void setAmmoCount(int ammoCount){
		this.ammoCount = ammoCount;
	}
	

}

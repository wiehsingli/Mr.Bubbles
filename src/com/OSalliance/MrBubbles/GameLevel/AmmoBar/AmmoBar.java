package com.OSalliance.MrBubbles.GameLevel.AmmoBar;

import com.OSalliance.MrBubbles.R;
import com.OSalliance.MrBubbles.SoundManager.SoundManager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Takes care of all ammobar stuff.
 * It is a linearlayout that can handle clicks.
 */
public class AmmoBar extends LinearLayout implements View.OnClickListener{

	// An identification string for use in the logger.
	private static final String TAG = AmmoBar.class.getSimpleName();
	
	// An array of AmmoSlots.
	private AmmoSlot[] slots;
	
	// Keeps track of the currently selected AmmoSlot in the array.
	private int currentSelect;
	
	private float textDensity;
	
	private boolean paused;
	
	public AmmoBar(Context context, int width, int height) {
		super(context);
		
		Log.d(TAG, "Width: " + width + ", Height: " + height);
		
		// We define the inner workings of the layout.
		setOrientation(LinearLayout.HORIZONTAL);
		setLayoutParams(new LinearLayout.LayoutParams(width, height));
		setGravity(Gravity.CENTER);
		this.setBackgroundResource(R.drawable.ammo_bar_back);
		
		// Initialize the array with objects,
		// set up their positioning,
		// and add clicking listeners to them.
		// This is what it's supposed to display on screen:
		// [padding][object][padding][object][padding][object][padding] ...
		slots = new AmmoSlot[5];
		for (int i = 0; i < slots.length; i++){
			LinearLayout layout = new LinearLayout(context); // padding for in-between the objects
			layout.setLayoutParams(new LinearLayout.LayoutParams(width, height, 1));
			
			slots[i] = new AmmoSlot(context);
			slots[i].setImageBitmap(getAmmoPic("", 0, false));
			slots[i].setOnClickListener(this);
			
			addView(layout); // add padding and object to this layout
			addView(slots[i]);
		}
		
		LinearLayout layout = new LinearLayout(context); // add the final padding
		layout.setLayoutParams(new LinearLayout.LayoutParams(width, height, 1));
		addView(layout);
		
		currentSelect = -1; // initialize the value to -1, NO SELECTION
		
		textDensity = context.getApplicationContext().getResources().getDisplayMetrics().density;
		
		paused = false;
	}
	
	public void reset(){
		for (int i = 0; i < slots.length; i++){
			if (!slots[i].getAmmoType().equals("")){
				slots[i].setAmmoType("");
				slots[i].setAmmoCount(0);
				slots[i].setImageBitmap(getAmmoPic("", 0, false));
			}
		}
		
		currentSelect = -1;
	}
	
	public void addStartingAmmo(int levelID){
		StartingAmmoHandler.getStartingAmmo(this, levelID);
	}

	/**
	 * This method is called when one of the AmmoSlot objects is clicked on.
	 * It loops through the AmmoSlots array until if finds the slot that
	 * was clicked on. If possible, it makes that slot the newly selected slot.
	 * 
	 * @param		Irrelevant; Not used.
	 */
	public void onClick(View view) {
		if (!paused){
			for (int i = 0; i < slots.length; i++){
				if (view == slots[i]){
					Log.d(TAG, "Slot #" + i);
					// If the object that was clicked on is not an empty slot...
					if (currentSelect != i && !slots[i].getAmmoType().equals("")){
						SoundManager.playSound(0, 1);
						
						deselectCurrentSlot();
						currentSelect = i; // Change to new slot
						selectCurrentSlot();
					}
					
					break; // We found the object that was clicked on, why check the rest?
				}
			}
		}
		
	}
	
	private void setPaused(boolean paused){
		this.paused = paused;
	}
	
	/**
	 * Calls the getAmmoPic() method to display a highlight around the currently selected slot.
	 */
	private void selectCurrentSlot(){
		String ammoType = slots[currentSelect].getAmmoType();
		int ammoCount = slots[currentSelect].getAmmoCount();
		
		Bitmap ammoPic = getAmmoPic(ammoType, ammoCount, true);
		
		slots[currentSelect].setImageBitmap(ammoPic);
	}
	
	/**
	 * Calls the getAmmoPic() method to remove the highlight around the currently selected slot.
	 */
	private void deselectCurrentSlot(){
		String ammoType = slots[currentSelect].getAmmoType();
		int ammoCount = slots[currentSelect].getAmmoCount();
		
		Bitmap ammoPic = getAmmoPic(ammoType, ammoCount, false);
		
		slots[currentSelect].setImageBitmap(ammoPic);
	}
	
	/**
	 * Used whenever the image in a slot needs to change.
	 * Creates the appropriate image for the specified ammoType and return it.
	 * 
	 * @param ammoType	The type of ammo we want.
	 * @param ammoCount	The number that should display on the image.
	 * @param select	If true, will add a highlight box around the image.
	 * @return			Returns a bitmap with the specified attributes.
	 */
	private Bitmap getAmmoPic(String ammoType, int ammoCount, boolean select){
		Bitmap ammoPic;
		
		//// HARD CODED START
		
		// Create the bitmap according to ammoType. If ammoType is equal to "", return a default bitmap.
		if (ammoType.equals("B_Bubble")){
			ammoPic = BitmapFactory.decodeResource(getResources(), R.drawable.ammo_slot_blue).copy(Bitmap.Config.ARGB_8888, true);
		}else if (ammoType.equals("R_Bubble")){
			ammoPic = BitmapFactory.decodeResource(getResources(), R.drawable.ammo_slot_red).copy(Bitmap.Config.ARGB_8888, true);
		}else if (ammoType.equals("P_Bubble")){
			ammoPic = BitmapFactory.decodeResource(getResources(), R.drawable.ammo_slot_purple).copy(Bitmap.Config.ARGB_8888, true);
		}else if (ammoType.equals("G_Bubble")){
			ammoPic = BitmapFactory.decodeResource(getResources(), R.drawable.ammo_slot_green).copy(Bitmap.Config.ARGB_8888, true);
		}else if (ammoType.equals("W_Bubble")){
			ammoPic = BitmapFactory.decodeResource(getResources(), R.drawable.ammo_slot_blue).copy(Bitmap.Config.ARGB_8888, true);
		}else{
			ammoPic = BitmapFactory.decodeResource(getResources(), R.drawable.ammo_slot_empty).copy(Bitmap.Config.ARGB_8888, true);
			return ammoPic;
		}
		
		//// HARD CODED END
		
		Paint tPaint = new Paint();
		tPaint.setTextAlign(Paint.Align.RIGHT);
		tPaint.setTextSize(20 * textDensity);
		tPaint.setAntiAlias(true);
		tPaint.setColor(Color.BLACK);
		
		Canvas canvas = new Canvas(ammoPic);
		
		if (select){ // Add highlight if select is true.
			/*Paint pPaint = new Paint();
			pPaint.setStrokeWidth(6);
			pPaint.setStyle(Paint.Style.STROKE);
			pPaint.setColor(Color.YELLOW);*/
			
			Bitmap highlight = BitmapFactory.decodeResource(getResources(), R.drawable.ammo_slot_highlight).copy(Bitmap.Config.ARGB_8888, true);
			
			canvas.drawBitmap(highlight, 0, 0, null);
			//canvas.drawRect(0, 0, ammoPic.getWidth(), ammoPic.getHeight(), pPaint);
			
		}
		
		canvas.drawText("" + ammoCount, ammoPic.getWidth() - 10, 20 * textDensity, tPaint); // Draw ammo count onto bitmap.
		
		return ammoPic;
	}
	
	/**
	 * Called by GamePanel when an ammo is collected.
	 * If the ammo exists in one of the slots, increment
	 * its ammo counter by 1. If not, activate an empty slot.
	 * If both cases are false, replace the currently selected
	 * slot with the collected ammo.
	 * 
	 * @param ammoType	The ID of the ammo that was collected.
	 * @param ammoCount	The amount the ammo counter should be increased by.
	 */
	public void incrementAmmo(String ammoType, int ammoCount){
		// Look for an ammo slot that already has the ammo.
		boolean found = false;
		for (int i = 0; i < slots.length; i++){
			if (slots[i].getAmmoType().equals(ammoType)){ // If the slot contains the ammo type...
				slots[i].setAmmoCount(slots[i].getAmmoCount() + ammoCount);
				
				Boolean select = false;
				if (i == currentSelect){ // Is this slot the currently selected one?
					select = true; // If yes, make sure it will be highlighted when redrawn.
				}
				
				Bitmap ammoPic = getAmmoPic(ammoType, slots[i].getAmmoCount(), select);
				slots[i].setImageBitmap(ammoPic);
				
				found = true;
				
				break; // Already found it, why check the rest?
			}
		}
		
		// If we found an ammo slot that already has the ammo,
		// then there is nothing else we need to do. Return.
		if (found){
			return;
		}
		
		// If we did not find said ammo slot, try to find an inactivated slot instead.
		found = false;
		for (int i = 0; i < slots.length; i++){
			if (slots[i].getAmmoType().equals("")){ // If this slot is empty...
				slots[i].setAmmoType(ammoType);
				slots[i].setAmmoCount(ammoCount);
				
				Boolean select = false;
				// If all slots are inactive, make this the new currently selected slot.
				if (currentSelect == -1){
					select = true;
					currentSelect = i; // i should always be 0, since we start at 0
				}
				
				Bitmap ammoPic = getAmmoPic(ammoType, ammoCount, select);
				slots[i].setImageBitmap(ammoPic);
				
				found = true;
				
				break; // Already found the slot, why check the rest?
			}
		}
		
		if (found){ // If the slot was found, there's nothing left to do, so return.
			return;
		}
		
		// If no slot contains the ammo type
		// and there are no empty slots,
		// replace the currently selected slot with the new ammotype.
		slots[currentSelect].setAmmoType(ammoType);
		slots[currentSelect].setAmmoCount(ammoCount);
		
		Bitmap ammoPic = getAmmoPic(ammoType, ammoCount, true);
		
		slots[currentSelect].setImageBitmap(ammoPic);
	}
	
	
	/**
	 * Called by GamePanel when an ammo should be fired.
	 * Decrements the currently selected slot by 1 and
	 * changes the selected slot to a new one if it runs
	 * out of ammo.
	 * 
	 * @return	The ID of the ammo in the currently selected slot. Returns "" if no ammo should be fired.
	 */
	public String useAmmo(){
		if (currentSelect != -1){ // If there is a currently selected slot...
			String ammoType = slots[currentSelect].getAmmoType();
			int ammoCount = slots[currentSelect].getAmmoCount() - 1; // Decrement ammo by 1.
			slots[currentSelect].setAmmoCount(ammoCount);
			
			if (slots[currentSelect].getAmmoCount() == 0){ // If we run out of ammo...
				// Change slot to an inactivated slot.
				slots[currentSelect].setAmmoType("");
				slots[currentSelect].setAmmoCount(0);
				slots[currentSelect].setImageBitmap(getAmmoPic("", 0, false));
				
				int c = 1; // We want to loop through all slots until we find the next active slot.
				do{
					currentSelect++;
					if (currentSelect == slots.length){
						currentSelect = 0;
					}
					
					// If slot is active, make it the currently selected slot.
					if (!slots[currentSelect].getAmmoType().equals("")){
						Bitmap ammoPic = getAmmoPic(slots[currentSelect].getAmmoType(),
								slots[currentSelect].getAmmoCount(), true);
						slots[currentSelect].setImageBitmap(ammoPic);
						
						break; // We found the next active slot, so why check the rest?
					}
					
					c++;
				}while(c < slots.length);
				
				// If we did not find an active slot, set currentSelect to NO SELECTION
				if (c >= slots.length){
					currentSelect = -1;
				}
			}else{ // If we still have some ammo in the currently selected slot, update ammo counter.
				Bitmap ammoPic = getAmmoPic(ammoType, ammoCount, true);
				slots[currentSelect].setImageBitmap(ammoPic);
			}
			
			return ammoType;
		}
		
		// If a slot is not currently selected...
		return "";
	}
	
	

}










package com.OSalliance.MrBubbles.GameLevel.AmmoBar;

import android.util.Log;

public class StartingAmmoHandler {
	private static final String TAG = StartingAmmoHandler.class.getSimpleName();
	
	public static void getStartingAmmo(AmmoBar ammoBar, int levelID) {
		switch (levelID) {

		case 1:
			ammoBar.incrementAmmo("B_Bubble", 50);
			ammoBar.incrementAmmo("P_Bubble", 50);
			ammoBar.incrementAmmo("R_Bubble", 50);
			ammoBar.incrementAmmo("G_Bubble", 50);
			ammoBar.incrementAmmo("W_Bubble", 50);
			
			break;

		case 2:
			ammoBar.incrementAmmo("P_Bubble", 4);

			break;

		case 3:
			ammoBar.incrementAmmo("B_Bubble", 2);
			ammoBar.incrementAmmo("P_Bubble", 2);
			ammoBar.incrementAmmo("R_Bubble", 6);

			break;

		case 4:
			ammoBar.incrementAmmo("B_Bubble", 10);
			ammoBar.incrementAmmo("P_Bubble", 10);
			ammoBar.incrementAmmo("R_Bubble", 10);
			ammoBar.incrementAmmo("G_Bubble", 10);
			
			break;
			
		case 5:
			ammoBar.incrementAmmo("B_Bubble", 6);
			ammoBar.incrementAmmo("P_Bubble", 6);
			ammoBar.incrementAmmo("R_Bubble", 6);
			ammoBar.incrementAmmo("G_Bubble", 6);
			
			break;
			
		case 20:
			ammoBar.incrementAmmo("B_Bubble", 50);
			ammoBar.incrementAmmo("P_Bubble", 50);
			ammoBar.incrementAmmo("R_Bubble", 50);
			ammoBar.incrementAmmo("G_Bubble", 50);
			
			break;
			
		default:

			Log.e(TAG, "Invalid levelID");
		}
	}
}

package com.OSalliance.MrBubbles.GameLevel.GameLogic;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

public class Maths {

	/**
	 * Returns the distance between 2 points
	 * @param x1 - x coord of first object
	 * @param y1 - y coord of first object
	 * @param x2 - x coord of second object 
	 * @param y2 - y coord of second object
	 * 
	 * @return distance between the two object
	 */
	public static double distance(double x1, double y1, double x2, double y2){
		return Math.sqrt((x1 - x2)*(x1 - x2) + (y1 - y2)*(y1 - y2));
	}
	
	/**
	 * Rotates a point around an origin according to a distance angle.
	 * Returns a double array containing the new x and y values of the rotated point.
	 * 
	 * @param originX	The x value of the origin
	 * @param originY	The y value of the origin
	 * @param pointX	The x value of the point
	 * @param pointY	The y value of the point
	 * @param theta		The rotation of the point in radians
	 * @return			A double array containing two values
	 */
	public static double[] rotatePoint(double originX, double originY, double pointX, double pointY, double theta){
		double diffX = pointX - originX;
		double diffY = pointY - originY;
		
		double newX = diffX * Math.cos(theta) - diffY * Math.sin(theta);
		double newY = diffX * Math.sin(theta) + diffY * Math.cos(theta);
		
		return new double[]{newX, newY};
	}
	

	/**
	 * This method converts dp unit to equivalent pixels, depending on device density. 
	 * 
	 * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
	 * @param context Context to get resources and device specific display metrics
	 * @return A float value to represent px equivalent to dp depending on device density
	 */
	public static float convertDpToPixel(float dp, Context context){
	    Resources resources = context.getResources();
	    DisplayMetrics metrics = resources.getDisplayMetrics();
	    float px = dp * (metrics.densityDpi / 160f);
	    return px;
	}

	/**
	 * This method converts device specific pixels to density independent pixels.
	 * 
	 * @param px A value in px (pixels) unit. Which we need to convert into db
	 * @param context Context to get resources and device specific display metrics
	 * @return A float value to represent dp equivalent to px value
	 */
	public static float convertPixelsToDp(float px, Context context){
	    Resources resources = context.getResources();
	    DisplayMetrics metrics = resources.getDisplayMetrics();
	    float dp = px / (metrics.densityDpi / 160f);
	    return dp;
	}
	
}
















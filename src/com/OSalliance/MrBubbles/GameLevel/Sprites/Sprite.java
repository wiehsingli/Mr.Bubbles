package com.OSalliance.MrBubbles.GameLevel.Sprites;

import com.OSalliance.MrBubbles.MainMenu.OnSpriteClickListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
/**
 * An abstract class that provides the basic attributes to each object. 
 * Such as movement and hitbox.
 * @author OSAlliance
 *
 */
public abstract class Sprite {
	
	public static final double DIRECTION_RIGHT = 1.0;
	public static final double DIRECTION_LEFT = -1.0;
	public static final double DIRECTION_UP = -1.0;
	public static final double DIRECTION_DOWN = 1.0;
	
	private Context context;
	
	private Bitmap bitmap;
	protected Rect hitbox;
	private double x;					//x position
	private double y;					//y position
	private double xv;					//x velocity
	private double yv;					//y velocity
	private double xa;					//x acceleration
	private double ya;					//y acceleration
	private double xDirection;			//x default direction
	private double yDirection;			//y default direction
	
	private OnSpriteClickListener clickListener = null;
	
	public Sprite(Context context, int drawableID, double x, double y){
		this.context = context;
		
		hitbox = new Rect();
		
		bitmap = BitmapFactory.decodeResource(context.getResources(), drawableID);
		
		this.x = x;
		this.y = y;
		
		updateHitbox();
		
		this.xv = 0;
		this.yv = 0;
		this.xa = 0;
		this.ya = 0;
		
		xDirection = DIRECTION_RIGHT;
		yDirection = DIRECTION_DOWN;
	}
	
	public Sprite(Context context, Bitmap bitmap, double x, double y){
		this.context = context;
		
		hitbox = new Rect();
		
		this.bitmap = bitmap;
		
		this.x = x;
		this.y = y;
		
		updateHitbox();
		
		this.xv = 0;
		this.yv = 0;
		this.xa = 0;
		this.ya = 0;
		
		xDirection = DIRECTION_RIGHT;
		yDirection = DIRECTION_DOWN;
	}
	
	/**
	 * Copies the object
	 * @param copy Sprite object to copy
	 */
	public Sprite(Sprite copy) {
		this.context = copy.getContext();
		
		hitbox = new Rect(copy.hitbox);
		
		bitmap = Bitmap.createBitmap(copy.bitmap);
		
		x = copy.x;
		y = copy.y;
		
		updateHitbox();
		
		xv = copy.xv;
		yv = copy.yv;
		xa = copy.xa;
		ya = copy.ya;
		
		xDirection = copy.xDirection;
		yDirection = copy.yDirection;
	}
	
	// getters
	public Context getContext(){
		return context;
	}
	
	public Bitmap getBitmap(){
		return bitmap;
	}
	
	public double getX(){
		return x;
	}
	
	public double getY(){
		return y;
	}
	public int getWidth(){
		return bitmap.getWidth();
	}
	public int getHeight(){
		return bitmap.getHeight();
	}
	
	public double getXV(){
		return xv;
	}
	public double getYV(){
		return yv;
	}
	public double getXA(){
		return xa;
	}
	public double getYA(){
		return ya;
	}
	public double getYDirection(){
		return yDirection;
	}
	
	public double getXDirection(){
		return yDirection;
	}
	public Rect getHitbox(){
		return hitbox;
	}
	public double centerX(){
		return x + (getWidth()/2); 
	}
	public double centerY(){
		return y + (getHeight()/2);
	}
	
	//setters
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	
	public void setX(double x){
		this.x = x;
		updateHitbox();
	}
	public void setY(double y){
		this.y = y;
		updateHitbox();
	}
	public void setXV(double xv){
		this.xv = xv;
	}
	public void setYV(double yv){
		this.yv = yv;
	}
	public void setXA(double xa){
		this.xa = xa;
	}
	public void setYA(double ya){
		this.ya = ya;
	}

	/**
	 * reverses y driection
	 */
	public void toggleYDirection(){
		this.yDirection = -yDirection;
	}
	
	
	/**
	 * Reverses x direction
	 */
	public void toggleXDirection(){
		this.xDirection = -xDirection;
	}
	
	/**
	 * reverses x velocity
	 */
	public void toggleXVelocity(){
		this.xv = -xv;
	}
	
	/**
	 * reverses y velocity
	 */
	public void toggleYVelocity(){
		this.yv = -yv;
	}
	
	/**
	 * Draws the the bitmap onto the canvas
	 * @param canvas Grabs canvas from the thread and draws bitmap
	 */
	public void draw(Canvas canvas){
		canvas.drawBitmap(bitmap, (float)x, (float)y, null);
	}
	
	/**
	 * Updates position of each object.
	 * Can update objects by acceleration or velocity making it more realistic, or user friendly.
	 */
	public void update(){
		
		xv += xa * xDirection;			//x direction determines which direction in the x axis to accelerate 
		x += xv;
		yv += ya * yDirection;			//y direction determines which direction in the y axis to accelerate
		y += yv;						
		
		updateHitbox();
	}
	
	/**
	 * aligns the hitboxes as the objects are being updated
	 */
	protected void updateHitbox(){
		hitbox.left = (int) (x);							//aligns left part of hitbox to left part of bitmap
		hitbox.right = (int) (x + bitmap.getWidth());		//aligns right part of hitbox to right part of bitmap	
		hitbox.top = (int) (y);								//aligns top part of hitbox to top part of bitmap
		hitbox.bottom = (int) (y + bitmap.getHeight());		//aligns left bottom of hitbox to left bottom of bitmap
	}
	
	/*public void handleActionDown(int eventX, int eventY){
		if (clickListener == null){
			return;
		}
		if (eventX >= getHitbox().left && eventX <= getHitbox().right){
			if (eventY >= getHitbox().top && eventY <= getHitbox().bottom){
				setTouched(true); // droid touched
			}else{
				setTouched(false);
			}
		}else{
			setTouched(false);
		}
	}*/
	
	public void addOnSpriteClickListener(OnSpriteClickListener clickListener){
		this.clickListener = clickListener;
	}
}

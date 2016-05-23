package com.OSalliance.MrBubbles.GameLevel.Sprites.Enemies;

import com.OSalliance.MrBubbles.R;
import com.OSalliance.MrBubbles.GameLevel.GameLogic.CollisionDetector;
import com.OSalliance.MrBubbles.GameLevel.GameLogic.Maths;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;

/**
 * Applies starfish image to bitmap.
 * Applies appropriate image when caught in bubble.
 * Sets any unique abilities.
 * 
 * @author OSAlliance
 *
 */
public class Starfish extends Enemy{
	private static final String TAG = Starfish.class.getSimpleName();
	
	private static final int MAX_ROTATE = 10;
	private static final int MIN_ROTATE = 2;
	private static final int DIS_ROTATE = 2;
	private static final int COOL_TIME = 20; // 9 = full spin animation
	
	private Context context;
	private int rotation;
	private Matrix matrix;
	private int curRotate = MIN_ROTATE;
	private int curTime = 0;
	private boolean agitated = false;
	private boolean dangerous = true;

	public Starfish(Context context, double x, double y, double fallSpeed){
		super(context, R.drawable.enemy_star, x, y, fallSpeed);
		
		if (Math.random() >= 0.5){
			setXV(1);
		}else{
			setXV(-1);
			toggleXDirection();
		}
		
		this.context = context;
		rotation = 0;
		matrix = new Matrix();
	}
	
	/**
	 * Creates a copy of starfish for faster coding ;)
	 * 
	 * @param copy The starfish to copy attributes from.
	 */
	public Starfish(Starfish copy, double fallSpeed){
		super(copy, fallSpeed);
		
		context = copy.context;
	}
	
	/**
	 * Detects if enemy is captured and applies appropriate image.
	 * 
	 * @param ammoType string to identify specific projectile
	 */
	@Override
	public void setCaptured(String ammoType){
		if (ammoType.equals("B_Bubble")) {
			setBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy_jelly_blue));
		}else if (ammoType.equals("R_Bubble")) {
			setBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy_jelly_red));
		}else if (ammoType.equals("P_Bubble")) {
			setBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy_jelly_purple));
		}else if (ammoType.equals("G_Bubble")) {
			setBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy_jelly_green));
		}else if (ammoType.equals("W_Bubble")) {
			setBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy_jelly_blue));
		}else {
			Log.e(TAG, "Cannot determine ammo type");
		}
		super.setCaptured(ammoType);
	}
	
	/**
	 * Causes the starfish to start spinning and starts the cool down timer.
	 */
	public void agitate(){
		if (curTime == 0){
			//Log.d(TAG, "Agitate");
			curTime = COOL_TIME;
			agitated = true;
			dangerous = true;
		}
	}
	
	/**
	 * Checks to see if the starfish is still dangerous and whether
	 * it is spinning fast enough to deflect a bubble.
	 * 
	 * @return Whether the starfish can still deflect a bubble.
	 */
	public boolean canDeflectBubble(){
		if (dangerous && (agitated || curRotate > 6)){
			//Log.d(TAG, "candeflectbubble");
			return true;
		}
		return false;
	}
	
	/**
	 * Lets the starfish know that it has deflected a bubble.
	 * Sets its dangerous property to false.
	 */
	public void registerBubbleDeflect(){
		//Log.d(TAG, "registerbubbledeflect");
		dangerous = false;
	}
	
	/**
	 * Updates and sets attributes of starfish.
	 */
	@Override
	public void update(){
		if (!isDead()){
			setYV(getFallSpeed());
			if (CollisionDetector.hitTestLeftWall(this)){
				setX(0);
				toggleXDirection();
				setXV(1);
			}else if (CollisionDetector.hitTestRightWall(this)){
				setX(CollisionDetector.getScreenWidth() - getWidth());
				toggleXDirection();
				setXV(-1);
			}
			
			if (!hasLife()){
				updateMatrix();
				
				if (curTime > 0){
					curTime--;
				}
			}
		}
		
		super.update();
	}
	
	/**
	 * Updates the matrix used to rotate the bitmap image of the starfish.
	 */
	private void updateMatrix(){
		Matrix m = new Matrix();
		m.postRotate(rotation, getWidth() / 2, getHeight() / 2);
		m.postTranslate(Maths.convertDpToPixel((float)getX(), context), Maths.convertDpToPixel((float)getY(), context));
		matrix.set(m);
		updateCurRotate();
		if (getXV() > 0){
			rotation += curRotate;
		}else{
			rotation -= curRotate;
		}
		
	}
	
	/**
	 * Updates the speed at which the starfish rotates.
	 */
	private void updateCurRotate(){
		if (curRotate < MAX_ROTATE && agitated){
			curRotate += DIS_ROTATE;
		}else if (curRotate > MIN_ROTATE && !agitated){
			curRotate -= DIS_ROTATE;
		}else if (agitated){
			Log.d(TAG, "calming");
			agitated = false;
		}
	}
	
	@Override
	public void draw(Canvas canvas){
		if (hasLife() || isDead()){
			super.draw(canvas);
		}else{
			canvas.drawBitmap(getBitmap(), matrix, null);
		}
	}
	
	@Override
	protected void updateHitbox() {
		hitbox.left = (int)(getX() + 5);
		hitbox.right = (int)(getX() + getWidth() - 5);
		hitbox.top = (int)(getY() + 2);
		hitbox.bottom = (int)(getY() + getHeight() - 4);
	}
}

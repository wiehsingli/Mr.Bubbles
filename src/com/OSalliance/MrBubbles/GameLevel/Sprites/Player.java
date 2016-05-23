package com.OSalliance.MrBubbles.GameLevel.Sprites;

import com.OSalliance.MrBubbles.R;
import com.OSalliance.MrBubbles.GameLevel.GameLogic.CollisionDetector;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * Player class applies image to bitmap 
 * controls movement of the player object
 * checks collision between screen boundaries
 * 
 * @author OSAlliance
 *
 */
public class Player extends Sprite implements SensorEventListener{
	private static final String TAG = Player.class.getSimpleName();
	
	private SensorManager sensorManager;
	private float xVelocity;
	private Bitmap player;
	int width, height;
	int currentFrame = 0;
	private long unstunTime;
	
	public Player(Context context, int x, int y) {
		super(context, R.drawable.player, x, y);
		
		Log.d(TAG, "Player constructed");
		
		//Sprite sheet for player
		player = BitmapFactory.decodeResource(context.getResources(), R.drawable.player_spritesheet2);
		width = player.getWidth()/8;
		height = player.getHeight();
		
		// Store the time until unstunned.
		unstunTime = 0;
		
		//uses sensor manager for the accelerometer 
		sensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
		sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);	
	}
	
	/**
	 * Updates position of the player.
	 * Checks collision with screen boundaries
	 */
	public void update(){
		if (System.currentTimeMillis() < unstunTime) {
			return;
		}
		
		setXV(xVelocity);		// change to accelerometer

		//once player contacts either wall, set all movement to 0
		if (xVelocity < 0){
			if(CollisionDetector.hitTestLeftWall(this)){
				setXV(0); setXA(0);
				xVelocity = 0;
			}
		}else{
			if(CollisionDetector.hitTestRightWall(this)){
				setXV(0); setXA(0);
				xVelocity = 0;
			}
		}

		if(xVelocity != 0){
			currentFrame = ++currentFrame % 8;
		}
		super.update();
	}
	
	@Override
	public void draw(Canvas canvas){
	
		int srcX = currentFrame * width;
		Rect src = new Rect(srcX, 0, srcX + width, height);
		Rect dst = new Rect((int)getX(),(int)getY(),(int)getX()+width,(int)getY()+height);
		
		canvas.drawBitmap(player, src, dst, null);
//		canvas.drawBitmap(player, src, dst, null);
//		canvas.drawBitmap(player, (float)getX(), (float)getY(), null);
//		super.draw(canvas);
	}
	
	@Override
	protected void updateHitbox() {
		hitbox.left = (int)(getX() + 5);
		hitbox.right = (int)(getX() + getWidth() - 5);
		hitbox.top = (int)(getY() + 2);
		hitbox.bottom = (int)(getY() + getHeight() - 2);
	}
	
	public void stun() {
		unstunTime = System.currentTimeMillis() + 2000;
	}
	
	public boolean isStunned() {
		return System.currentTimeMillis() < unstunTime;
	}
	
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * Detects any changes in the accelerometer within the device
	 * sets the acceleration of the player object
	 */
	public void onSensorChanged(SensorEvent event) {
		
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
			//x = event.values[0];
			
			if (event.values[0]> 1){		//if device is tilted to the right, set acceleration appropriately
				xVelocity = (float) -6;
			}else if(event.values[0] < -1){		//if devices is tiled to the left, set accerleration appropriately
				xVelocity = (float) 6;
			}else{
				xVelocity = 0;
			}
				
		}
	}

	public void keyDownLeft() {
		xVelocity = -4f;
	}
	
	public void keyDownRight() {
		xVelocity = 4f;
	}
	
	public void keyUpLeft() {
		xVelocity = 0;
	}
	
	public void keyUpRight() {
		xVelocity = 0;
	}
}

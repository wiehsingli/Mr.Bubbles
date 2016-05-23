package com.OSalliance.MrBubbles.MainMenu;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * A general class that handles the creation and destruction of the MenuLoop.
 * It also handles all taps on the surface of the phone.
 * This class is meant to be extended and with some methods overriden by the subclass.
 */
public class CustomPanel extends SurfaceView implements SurfaceHolder.Callback{

	private Thread thread;
	private MenuLoop loop;
	
	private boolean threadRunning;
	
	public CustomPanel(Context context) {
		super(context);
		
		getHolder().addCallback(this);
		
		setFocusable(true);
		
		threadRunning = false;
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// do nothing
		
	}

	/**
	 * Start the thread when the panel is created.
	 */
	public void surfaceCreated(SurfaceHolder holder) {
		startThread();
	}

	/**
	 * Stop the thread when the panel is destroyed.
	 */
	public void surfaceDestroyed(SurfaceHolder holder) {
		stopThread();
	}
	
	/**
	 * Called by the thread to do update stuff.
	 * Meant to be overriden.
	 */
	public void update(){
		
	}
	
	protected void startThread(){
		if (!threadRunning){
			loop = new MenuLoop(getHolder(), this);
			thread = new Thread(loop);
			loop.setRunning(true);
			thread.start();
			
			threadRunning = true;
		}
	}
	
	protected void stopThread(){
		if (threadRunning){
			boolean retry = true;
			loop.setRunning(false);
			while(retry){
				try{
					thread.join();
					retry = false;
				}catch(InterruptedException e){
					e.printStackTrace();
				}
			}
			
			threadRunning = false;
		}
	}
	
	/**
	 * Called by thread to draw stuff onto the canvas.
	 * Meant to be overriden.
	 * 
	 * @param canvas	Where everything is drawn on.
	 */
	public void render(Canvas canvas){
		//canvas.drawColor(Color.BLACK);
	}
	
	/**
	 * Is called when the user does something on the android surface.
	 */
	@Override
	public final boolean onTouchEvent(MotionEvent event){
		// If there was a press, call MotionEventDown().
		if (event.getAction() == MotionEvent.ACTION_DOWN){
			MotionEventDown(event);
		}
		
		return true;
	}
	
	/**
	 * Called when there was a touch on the screen.
	 * Meant to be overriden.
	 * 
	 * @param event
	 */
	protected void MotionEventDown(MotionEvent event){
		// extend
	}

}

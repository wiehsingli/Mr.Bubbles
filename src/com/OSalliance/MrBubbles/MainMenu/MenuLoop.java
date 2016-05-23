package com.OSalliance.MrBubbles.MainMenu;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * A runnable that calls the update() and render() methods of a class
 * extending the CustomPanel class.
 */
public class MenuLoop implements Runnable {
	// desired fps
	private final static int 	MAX_FPS = 75;	
	// maximum number of frames to be skipped
	private final static int	MAX_FRAME_SKIPS = 5;	
	// the frame period
	private final static int	FRAME_PERIOD = 1000 / MAX_FPS;	
	
	private CustomPanel panel;
	private SurfaceHolder surfaceHolder;
	private boolean running;
	
	public MenuLoop(SurfaceHolder surfaceHolder, CustomPanel panel){
		super();
		
		this.surfaceHolder = surfaceHolder;
		this.panel = panel;
	}
	
	public void setRunning(boolean running){
		this.running = running;
	}
	
	public void run() {
		Canvas canvas;

		long beginTime;		// the time when the cycle begun
		long timeDiff;		// the time it took for the cycle to execute
		int sleepTime;		// ms to sleep (<0 if we're behind)
		int framesSkipped;	// number of frames being skipped 

		sleepTime = 0;
		
		while (running) {
			canvas = null;
			
			// try locking the canvas for exclusive pixel editing
			// in the surface
			try {
				canvas = this.surfaceHolder.lockCanvas();
				
				if (canvas == null){
					continue;
				}
				
				synchronized (surfaceHolder) {
					beginTime = System.currentTimeMillis();
					framesSkipped = 0;	// resetting the frames skipped
					
					// update game state 
					panel.update();
					
					// render state to the screen
					// draws the canvas on the panel
					panel.render(canvas);				
					
					// calculate how long did the cycle take
					timeDiff = System.currentTimeMillis() - beginTime;
					
					// calculate sleep time
					sleepTime = (int)(FRAME_PERIOD - timeDiff);
					
					if (sleepTime > 0) {
						// if sleepTime > 0 we're OK
						
						try {
							// send the thread to sleep for a short period
							// very useful for battery saving
							Thread.sleep(sleepTime);	
						}
						
						catch (InterruptedException e) {}
					}
					
					while (sleepTime < 0 && framesSkipped < MAX_FRAME_SKIPS) {
						// we need to catch up
						
						panel.update(); // update without rendering
						sleepTime += FRAME_PERIOD;	// add frame period to check if in next frame
						framesSkipped++;
					}
				}
			}
			
			finally {
				// in case of an exception the surface is not left in 
				// an inconsistent state
				if (canvas != null) {
					surfaceHolder.unlockCanvasAndPost(canvas);
				}
			}	// end finally
		}
	}
}

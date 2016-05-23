package com.OSalliance.MrBubbles.MainMenu;

import java.util.ArrayList;

import com.OSalliance.MrBubbles.GameLevel.Sprites.Sprite;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class Stage extends SurfaceView implements SurfaceHolder.Callback {

	private ArrayList<Sprite> children = new ArrayList<Sprite>();
	
	private Thread thread;
	private StageRunnable runnable;
	private boolean threadRunning = false;
	
	public Stage(Context context) {
		super(context);
		
		this.getHolder().addCallback(this);
		
		this.setFocusable(true);
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		// DO NOTHING
	}

	public void surfaceCreated(SurfaceHolder holder) {
		startThread();
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		stopThread();
	}
	
	private void startThread(){
		if (!threadRunning){
			runnable = new StageRunnable(this);
			thread = new Thread(runnable);
			thread.start();
			threadRunning = true;
		}
	}
	
	protected void stopThread(){
		if (threadRunning){
			boolean retry = true;
			runnable.finish();
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
	
	public void update(){
		for (int i = 0; i < children.size(); i++){
			children.get(i).update();
		}
	}
	
	public void render(Canvas canvas){
		for (int i = 0; i < children.size(); i++){
			children.get(i).draw(canvas);
		}
	}
	
	public void addChild(Sprite sprite){
		children.add(sprite);
	}
	
	public void removeChild(Sprite sprite){
		children.remove(sprite);
	}
	
	@Override
	public final boolean onTouchEvent(MotionEvent event){
		if (event.getAction() == MotionEvent.ACTION_DOWN){
			MotionEventDown(event);
		}
		
		return true;
	}
	
	protected void MotionEventDown(MotionEvent event){
		// EXTEND
	}

}

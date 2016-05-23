package com.OSalliance.MrBubbles.MainMenu.Intro;

import com.OSalliance.MrBubbles.R;
import com.OSalliance.MrBubbles.MainMenu.MainActivity;
import com.OSalliance.MrBubbles.MainMenu.Level.LevelActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class IntroActivity extends Activity{
	private static final String TAG = IntroActivity.class.getSimpleName();
	
	private Handler handler;
	private ImageView imageView;
	private int[] introImages;
	private int index = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		introImages = new int[]{R.drawable.intro_image0, R.drawable.intro_image1, R.drawable.intro_image2}; // initialize the array with image ids
		
		imageView = new ImageView(this);
		imageView.setScaleType(ScaleType.FIT_XY);
		setContentView(imageView);
		
		handler = new Handler(){
        	@Override
			public void handleMessage(Message msg){
        		if (msg.what == 0){
        			if (index != introImages.length){
        				imageView.setImageBitmap(getIntroImage());
        				index++;
        				handler.sendEmptyMessageDelayed(0, 2000);
        			}else{
        				nextActivity();
        			}
        		}else{
        			throw new Error("UNKNOWN HANDLER MESSAGE");
        		}
        	}
        };
        handler.sendEmptyMessage(0);
        
        Log.d(TAG, "Was created!");
	}
	
	/**
	 * Called in time intervals. It takes the image drawable id from the current index in the
	 * image array and creates a Bitmap. It then appropriately crops the bitmap so that it fills
	 * the entire screen without uneven scaling.
	 * 
	 * @return The cropped bitmap of the indexed image.
	 */
	private Bitmap getIntroImage(){
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), introImages[index]);
		
		int width = getWindowManager().getDefaultDisplay().getWidth();
        int height = getWindowManager().getDefaultDisplay().getHeight();
        
        double rW = (double)width / bitmap.getWidth();
        double rH = (double)height / bitmap.getHeight();
        if (rW > rH){ // scale to width
        	int newHeight = (int) ((double) height / width * bitmap.getWidth());
        	bitmap = Bitmap.createBitmap(bitmap, 0, Math.abs(height - newHeight) / 2, bitmap.getWidth(), newHeight);
        }else if (rW < rH){ // scale to height
        	int newWidth = (int) ((double) width / height * bitmap.getHeight());
        	bitmap = Bitmap.createBitmap(bitmap, Math.abs(width - newWidth) / 2, 0, newWidth, bitmap.getHeight());
        	//bitmap = Bitmap.createBitmap(bitmap, 0, bitmap.getHeight(), Math.abs(width - newWidth) / 2, newWidth);
        }
        
        return bitmap;
	}
	
	 @Override
    public boolean onTouchEvent(MotionEvent event){
    	if (event.getAction() == MotionEvent.ACTION_DOWN){
    		nextActivity();
    	}
    	return super.onTouchEvent(event);
    }
	 
	 private void nextActivity(){
		 if (!isFinishing()){ // In the process of finishing?
			 Intent intent = new Intent(this, LevelActivity.class);
			 startActivity(intent);
			 finish();
		 }
	 }
}

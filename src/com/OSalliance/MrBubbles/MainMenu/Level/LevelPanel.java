package com.OSalliance.MrBubbles.MainMenu.Level;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.MotionEvent;

import com.OSalliance.MrBubbles.R;
import com.OSalliance.MrBubbles.GameLevel.GameLogic.SaveDataHandler;
import com.OSalliance.MrBubbles.MainMenu.CustomPanel;
import com.OSalliance.MrBubbles.MainMenu.MenuButton;
import com.OSalliance.MrBubbles.SoundManager.SoundManager;

public class LevelPanel extends CustomPanel{

	private static final String TAG = LevelPanel.class.getSimpleName();
	
	private LevelActivity activity;
	
	private MenuButton title;
	
	private LevelButton[] levelButtons;
	
	private MenuButton backButton;
	
	private int[] levelDataArray;
	
	public LevelPanel(LevelActivity activity) {
		super(activity);
		
		this.activity = activity;
		
		// Set up the background stuff
		setBackgroundResource(R.drawable.background);
		getHolder().setFormat(PixelFormat.TRANSPARENT);
		setZOrderOnTop(true);
		
		int width = activity.getWindowManager().getDefaultDisplay().getWidth();
		int height = activity.getWindowManager().getDefaultDisplay().getHeight();
		
		int xPos = width / 6;
		int yPos = height / 8;
		

		title = new MenuButton(this.getContext(), R.drawable.title_levelselect, 0, 0);
		title.setX(width / 2 - title.getWidth() / 2);
		title.setY(height / 8 - title.getHeight() / 2);
		
		float textDensity = activity.getApplicationContext().getResources().getDisplayMetrics().density;
		
		levelDataArray = SaveDataHandler.loadAllLevelData();
		
		levelButtons = new LevelButton[20];
		for (int i = 0; i < levelButtons.length; i++){
			int y = i / 5;
			int x = i % 5;
			
			levelButtons[i] = new LevelButton(this.getContext(), getLevelPic(i + 1, levelDataArray[i], textDensity),
					xPos * (x + 1), yPos * y + (yPos * 5 / 2));
			levelButtons[i].setX(levelButtons[i].getX() - levelButtons[i].getWidth() / 2);
			levelButtons[i].setY(levelButtons[i].getY() - levelButtons[i].getHeight() / 2);
		}
		
		backButton = new MenuButton(this.getContext(), R.drawable.menu_main_buttonback, xPos, height * 9 / 10);
		backButton.setX(backButton.getX() - backButton.getWidth() / 2);
		backButton.setY(backButton.getY() - backButton.getHeight() / 2);
	}
	
	private Bitmap getLevelPic(int levelID, int score, float textDensity){
		// default button image is menu_levelselect_levelon
		Bitmap pic = BitmapFactory.decodeResource(getResources(), R.drawable.menu_levelselect_levelon).copy(Bitmap.Config.ARGB_8888, true);
		
		if (score == -1){ // COMMENT OUT WHEN LOCKING LEVELS, CHECK BELOW
			score++;
		}
		
		Canvas canvas = new Canvas(pic);
		
		if (score == -1){
			pic = BitmapFactory.decodeResource(getResources(),  R.drawable.menu_levelselect_leveloff).copy(Bitmap.Config.ARGB_8888, true);
		}else if (score != 0){
			// default score image is score_pearl_three
			Bitmap scoreBitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.score_pearl_three).copy(Bitmap.Config.ARGB_8888, true);
			
			if (score == 1){
				scoreBitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.score_pearl_one).copy(Bitmap.Config.ARGB_8888, true);
			}else if (score == 2){
				scoreBitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.score_pearl_two).copy(Bitmap.Config.ARGB_8888, true);
			}
			
			canvas.drawBitmap(scoreBitmap,
					pic.getWidth() / 2 - scoreBitmap.getWidth() / 2,
					pic.getHeight() /2 - scoreBitmap.getHeight() / 2, null);
			
		}
		
		Paint tPaint = new Paint();
		tPaint.setTextAlign(Paint.Align.CENTER);
		tPaint.setTextSize(20 * textDensity);
		tPaint.setAntiAlias(true);
		tPaint.setColor(Color.BLACK);
		
		int xPos = canvas.getWidth() / 2;
		int yPos = (int) ((canvas.getHeight() / 2) - ((tPaint.descent() + tPaint.ascent()) / 2));
		
		canvas.drawText("" + levelID, xPos, yPos, tPaint);
		
		return pic;
	}
	
	public void checkLevelData(){
		
		Log.d(TAG, "CheckLevelData");
		if (SaveDataHandler.isDirty()){
			SaveDataHandler.clean();

			float textDensity = activity.getApplicationContext().getResources().getDisplayMetrics().density;

			for (int i = 0; i < levelDataArray.length; i++){
				Bitmap bitmap = getLevelPic(i + 1, levelDataArray[i], textDensity);

				levelButtons[i].setBitmap(bitmap);
			}
		}
	}

	@Override
	public void update(){
		super.update();
		
		title.update();
		
		backButton.update();
	}
	
	@Override
	public void render(Canvas canvas){
		super.render(canvas);
		
		canvas.drawColor(0, PorterDuff.Mode.CLEAR);
		
		title.draw(canvas);
		
		for (int i = 0; i < levelButtons.length; i++){
			levelButtons[i].draw(canvas);
		}
		
		backButton.draw(canvas);
	}
	
	@Override
	protected void MotionEventDown(MotionEvent event){
		super.MotionEventDown(event);
		
		backButton.handleActionDown((int)event.getX(), (int)event.getY());
		if (backButton.isTouched()){
			SoundManager.playSound(0, 1);
			backButton.setTouched(false);
			activity.chooseLevel(LevelActivity.RETURN_MENU);
		}
		
		for (int i = 0; i < levelButtons.length; i++){
			levelButtons[i].handleActionDown((int)event.getX(), (int)event.getY());
			
			/*if (levelButtons[i].isTouched() && levelDataArray[i] != -1){*/ 
			if (levelButtons[i].isTouched()){ // COMMENT OUT WHEN LOCKING LEVELS, CHECK ABOVE
				SoundManager.playSound(0, 1);
				levelButtons[i].setTouched(false);
				activity.chooseLevel(i + 1);
				return;
			}
		}
	}

}
// squid
// octopus
// normal fish, swims across screen
// starfish, bounces off walls, can bat away bubbles by spinning
// puffer fish, one show to inflate it and one shot to kill it
// sea horse, can dodge bubbles

package com.OSalliance.MrBubbles.GameLevel;

import java.util.ArrayList;
import com.OSalliance.MrBubbles.GameLevel.AmmoBar.AmmoBar;
import com.OSalliance.MrBubbles.GameLevel.GameLogic.CollisionDetector;
import com.OSalliance.MrBubbles.GameLevel.GameLogic.Maths;
import com.OSalliance.MrBubbles.GameLevel.GameLogic.SaveDataHandler;
import com.OSalliance.MrBubbles.GameLevel.GameLogic.SpawningAI;
import com.OSalliance.MrBubbles.GameLevel.Sprites.Pearl;
import com.OSalliance.MrBubbles.GameLevel.Sprites.Player;
import com.OSalliance.MrBubbles.GameLevel.Sprites.AmmoDrop;
import com.OSalliance.MrBubbles.GameLevel.Sprites.Enemies.Enemy;
import com.OSalliance.MrBubbles.GameLevel.Sprites.Enemies.Rock;
import com.OSalliance.MrBubbles.GameLevel.Sprites.Enemies.Seahorse;
import com.OSalliance.MrBubbles.GameLevel.Sprites.Enemies.Starfish;
import com.OSalliance.MrBubbles.GameLevel.Sprites.Projectiles.BlueBubble;
import com.OSalliance.MrBubbles.GameLevel.Sprites.Projectiles.GreenBubble;
import com.OSalliance.MrBubbles.GameLevel.Sprites.Projectiles.Projectile;
import com.OSalliance.MrBubbles.GameLevel.Sprites.Projectiles.PurpleBubble;
import com.OSalliance.MrBubbles.GameLevel.Sprites.Projectiles.RedBubble;
import com.OSalliance.MrBubbles.GameLevel.Sprites.Projectiles.WhiteBubble;
import com.OSalliance.MrBubbles.GameLevel.SubMenu.FailMenu;
import com.OSalliance.MrBubbles.GameLevel.SubMenu.PauseMenu;
import com.OSalliance.MrBubbles.GameLevel.SubMenu.VictoryMenu;
import com.OSalliance.MrBubbles.SoundManager.SoundManager;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.graphics.PorterDuff;

/**
 * A SurfaceView that represents the game panel where the gameplay takes place. It
 * handles all the sprites on the screen, as well as handling game logic.
 * 
 * @author OSAlliance
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
	private static final String TAG = GamePanel.class.getSimpleName();
	
	private final int PLAYING = 0;
	private final int PAUSED = 1;
	private final int VICTORY = 2;
	private final int FAILED = 3;
	private final int INTRO = 4;
	
	private int gameStatus;
	
	private AmmoBar ammoBar;
	private Player player;
	private ArrayList<Projectile> projectiles;
	private ArrayList<Enemy> enemies;
	private ArrayList<AmmoDrop> ammoDrops;
	private Pearl[] lives;
	private MainThreadHandler mainHandler; // Handler for the main thread
	private Thread thread;
	private GameLoop loop;
	private SpawningAI ai;
	private boolean enemySpawnFinished;
	
	private boolean threadRunning;
	
	private PauseMenu pauseMenu;
	private VictoryMenu victoryMenu;
	private FailMenu failMenu;
	private int levelID;
	
	public GamePanel(Context context, final AmmoBar ammoBar, int width, int height, int levelID) {
		super(context);
		Log.d(TAG, "GamePanel constructed");
		
		getHolder().addCallback(this);
		setFocusable(true);
		
		this.ammoBar = ammoBar;
		this.levelID = levelID;
		
		CollisionDetector.setScreenDimensions(width, height);
		
		player = new Player(context, 0, 0);
		
		Log.d(TAG, "LevelID: " + levelID);
		
		ai = new SpawningAI(this, width, height, levelID);
		
		/* Our game loop changes some complicated values in ammoBar, which runs in the main thread.
		 * In order to access the main thread from the game loop, we need to define a handler that
		 * allows the game loop to ask the main thread to change those values. */
		mainHandler = new MainThreadHandler();
		
		pauseMenu = new PauseMenu(context, 0, 0);
		victoryMenu = new VictoryMenu(context, 0, 0);
		failMenu = new FailMenu(context, 0, 0);
		
		createGame(context);
	}
	
	private void createGame(Context context){
		int width = CollisionDetector.getScreenWidth();
		int height = CollisionDetector.getScreenHeight();
		
		player.setY(height - player.getHeight());		//places player on top of ammobar
		player.setX(width/2 - player.getWidth() / 2);		//centers player in screen
		
		projectiles = new ArrayList<Projectile>();
		enemies = new ArrayList<Enemy>();	
		ammoDrops = new ArrayList<AmmoDrop>();

		enemySpawnFinished = false;
		ai.reset(levelID);
		
		double w = width / 8;
		
		lives = new Pearl[3];
		lives[0] = new Pearl(context, w, player.getY() - player.getHeight()/2);
		lives[0].setX(lives[0].getX()-lives[0].getWidth()/2);
		lives[1] = new Pearl(context, w * 4, player.getY() - player.getHeight()/2);
		lives[1].setX(lives[1].getX()-lives[1].getWidth()/2);
		lives[2] = new Pearl(context, w * 7, player.getY() - player.getHeight()/2);
		lives[2].setX(lives[2].getX()-lives[2].getWidth()/2);
		threadRunning = false;
		
		gameStatus = PLAYING;
		
		pauseMenu.setX(width / 2 - pauseMenu.getWidth() / 2);
		pauseMenu.setY(height / 2 - pauseMenu.getHeight() / 2);
		
		victoryMenu.setX(width / 2 - victoryMenu.getWidth() / 2);
		victoryMenu.setY(height / 2 - victoryMenu.getHeight() / 2);
		
		failMenu.setX(width / 2 - failMenu.getWidth() / 2);
		failMenu.setY(height / 2 - failMenu.getHeight() / 2);
		
		// Tell main thread to reset ammoBar
		Message message = new Message();
		message.what = 1;
		mainHandler.sendMessage(message);
	}
	
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
	}

	public void surfaceCreated(SurfaceHolder arg0) {
		Log.d(TAG, "surfaceCreated() called");
		
		startThread();
	}

	public void surfaceDestroyed(SurfaceHolder arg0) {
		Log.d(TAG, "surfaceDestroyed() called");
		
		stopThread();
	}
	
	/**
	 * Starts the game loop if it is not already started, and starts the spawning AI
	 * if it is not already started, or resume the spawning AI if it already is started.
	 */
	private void startThread(){
		if (!threadRunning){
			loop = new GameLoop(getHolder(), this);
			thread = new Thread(loop);
			loop.setRunning(true);
			thread.start();
			
			if (ai.isStarted()) {
				ai.resumeSpawning();
			}else {
				ai.startEnemyWaves("random");
			}
			
			threadRunning = true;
		}
		
	}
	
	/**
	 * Stops the game loop if it is running, and if so, also pauses the spawning AI.
	 */
	private void stopThread(){
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
			
			ai.pauseSpawning();
			threadRunning = false;
		}	
	}
	
	/**
	 * Adds an ammo drop to this game panel's list of ammo drops on screen.
	 * 
	 * @param ammoDrop The ammo drop to add.
	 */
	public void addAmmoDrop(AmmoDrop ammoDrop) {
		ammoDrops.add(ammoDrop);
	}
	
	/**
	 * Adds a given list of enemies to this game panel's list of enemies
	 * on screen.
	 * 
	 * @param enemies The list of enemies to add.
	 */
	public void addEnemies(ArrayList<Enemy> enemies) {
		this.enemies.addAll(enemies);
	}
	
	/**
	 * Sets whether enemy spawning has finished.
	 * 
	 * @param isFinished Whether or not enemy spawning is finished yet.
	 */
	public void setEnemySpawnFinished(boolean isFinished) {
		enemySpawnFinished = isFinished;
	}
	
	/**
	 * Updates the location and states of all sprites on the game panel.
	 */
	public void update() {
		if (gameStatus == PLAYING){
			player.update(); 
			
			updateLives();
			updateProjectiles();
			updateEnemies();
			updateAmmoDrops();
			updateEnemyTargeting();
			checkVictory();
			checkFail();
		}
	}
	
	/**
	 * Updates the location and states of the lives on the game panel.
	 */
	private void updateLives() {
		for(int i = 0; i < lives.length; i++) {
			if (lives[i] != null) {
				lives[i].update();
				
				// Remove the life if it is off screen.
				if (lives[i].getX() >= getWidth()
						||lives[i].getX() + lives[i].getWidth() < 0
						|| lives[i].getY() + lives[i].getHeight() < 0
						|| lives[i].getY() >= getHeight()) {
					lives[i] = null;
				}
				
				// Set the life to be able to be captured when it is at its starting location.
				if (lives[i] != null && lives[i].getY() >= lives[i].getInitialY()) {
					lives[i].setVulnerable();

					for (int j = 0; j < enemies.size(); j++) {
						Enemy enemy = enemies.get(j);
						
						enemy.setNoMoreLife(false);
					}
				}
			}
		}
	}
	
	/**
	 * Updates the location and states of the projectiles on the game panel.
	 * Special projectiles that needs access to enemy properties will be updated here.
	 */
	private void updateProjectiles() {
		
		double bx, by, ex, ey;
		double enemyVectorX, enemyVectorY, mag, nx, ny;
		
		for (int i = 0; i < projectiles.size(); i++) {
			Projectile projectile = projectiles.get(i);
			
			projectile.update();
			
			// Removes the projectile if it is off screen.
			if (projectile.getX() >= getWidth()
					|| projectile.getX() + projectile.getWidth() < 0
					|| projectile.getY() + projectile.getHeight() < 0
					|| projectile.getY() >= getHeight()) {
				projectiles.remove(i);

				i--;

				continue;
			}
			
			int n;
			for (n = 0; n < enemies.size(); n++){
				if (enemies.get(n) instanceof Starfish && !enemies.get(n).hasLife()){
					double distance = Maths.distance(enemies.get(n).getX(), enemies.get(n).getY(), projectile.getX(), projectile.getY());
					Log.d(TAG, "Distance: " + distance);
					if (distance < 60){
						Starfish starfish = (Starfish) enemies.get(n);
						starfish.agitate();
						
						if (distance < 35 && starfish.canDeflectBubble()){
							starfish.registerBubbleDeflect();
							
							projectile.setXV(-projectile.getXV());
							projectile.setYV(-projectile.getYV());
							
							double theta;
							if (starfish.getXV() > 0){ // moving to the right, deflecting to the left
								theta = Math.PI / 6;
							}else{ // moving to the left, deflecting to the right
								theta = -Math.PI / 6;
							}
							
							
							double newPos[] = Maths.rotatePoint(0, 0, projectile.getXV(), projectile.getYV(), theta);
							
							projectile.setXV(newPos[0]);
							projectile.setYV(newPos[1]);
							
							break;
						}
					}
				}
			}
			if (n < enemies.size()){
				continue;
			}
			
			//Red Bubble properties
			if (projectile instanceof RedBubble) {
				RedBubble redProjectile = (RedBubble)projectile;
				
				if (redProjectile.getBounceCount() <= 0) {
					projectiles.remove(i);
					SoundManager.playSound(2, 1);
					
					i--;			
					continue;
				}
				
				if((CollisionDetector.hitTestLeftWall(redProjectile) || CollisionDetector.hitTestRightWall(redProjectile))) {
					redProjectile.toggleXVelocity();
					redProjectile.decrementBounceCount();
				}
				else if( (CollisionDetector.hitTestTopWall(redProjectile) || CollisionDetector.hitTestBottomWall(redProjectile))) {
					redProjectile.toggleYVelocity();
					redProjectile.decrementBounceCount();
				}
				
				else {
					for (int j = 0; j < enemies.size(); j++){
						Enemy enemy = enemies.get(j);
						
						if (enemy.isDead()) {
							continue;
						}
						
						if (CollisionDetector.hasCollided(projectile, enemy)) {
							if (!(enemy instanceof Rock)) {
								enemy.setCaptured(redProjectile.getAmmoType());
							}
							
							CollisionDetector.CollisionType type = CollisionDetector.getCollisionType(projectile, enemy);
							
							if (type == CollisionDetector.CollisionType.HORIZONTAL) {
								redProjectile.toggleXVelocity();
							}
							
							else if (type == CollisionDetector.CollisionType.VERTICAL) {
								redProjectile.toggleYVelocity();
							}
							
							else if (type == CollisionDetector.CollisionType.DIAGONAL) {
								redProjectile.toggleXVelocity();
								redProjectile.toggleYVelocity();
							}
							
							redProjectile.decrementBounceCount();
						}
					}
				}
			}
			
			//properies of green bubble, homes onto enemies in proximity
			else if (projectile instanceof GreenBubble){
				
				GreenBubble greenProjectile = (GreenBubble)projectile; 
				for(int j = 0; j < enemies.size(); j++){
					Enemy enemy = enemies.get(j);
					
					if (enemy.isDead()) {
						continue;
					}
					
					else if (CollisionDetector.hasCollided(greenProjectile, enemy)) {

						projectiles.remove(i);
						SoundManager.playSound(2, 1);
						
						if (!(enemy instanceof Rock)) {
							enemy.setCaptured(greenProjectile.getAmmoType());
						}

						i--;			
						break;
					}
					
					else if (enemy instanceof Rock) {
						continue;
					}
					
					//When Green bubbles get close to enemies
					else if(( Math.sqrt( ((enemy.getX() - greenProjectile.getX()) * (enemy.getX() - greenProjectile.getX()) ) + ((enemy.getY() - greenProjectile.getY()) * (enemy.getY() - greenProjectile.getY())) ) < greenProjectile.getDistance()) && !enemy.isDead()){
						
						//obtaining and setting points from bubble and enemy position
						bx = greenProjectile.getX(); 
						by = greenProjectile.getY();
						ex = enemy.getX(); 
						ey = enemy.getY();
						
						//forming vectors from points
						enemyVectorX = ex - bx;
						enemyVectorY = ey - by;
						mag = Math.sqrt((enemyVectorX*enemyVectorX + enemyVectorY*enemyVectorY));
						
						//normalizing vectors and setting it to bubble velocity
						nx = enemyVectorX / (mag/7);
						ny = enemyVectorY / (mag/7);
						
						greenProjectile.setXV(nx);
						greenProjectile.setYV(ny);
						
					}

				}
					
				
			}
			
			// Properties of white bubble, ghosts until reaches target point.
			else if (projectile instanceof WhiteBubble){
				
				WhiteBubble whiteProjectile = (WhiteBubble)projectile; 
				
				if (whiteProjectile.isGhost() && whiteProjectile.getY() <= whiteProjectile.getTargetHeight()) {
					whiteProjectile.setGhost(false);
					
					whiteProjectile.setXV(whiteProjectile.getXV() * 2);
					whiteProjectile.setYV(whiteProjectile.getYV() * 2);
				}
				
				if (!whiteProjectile.isGhost()) {
					// Remove the projectile if it has collided with an enemy.
					for (int j = 0; j < enemies.size(); j++) {
						Enemy enemy = enemies.get(j);

						if (CollisionDetector.hasCollided(projectile, enemy) && !enemy.isDead()) {

							projectiles.remove(i);
							SoundManager.playSound(2, 1);
							
							if (!(enemy instanceof Rock)) {
								enemy.setCaptured(whiteProjectile.getAmmoType());
							}

							i--;			
							break;
						}
					}
				}
			}

			else {
				// Remove the projectile if it has collided with an enemy.
				for (int j = 0; j < enemies.size(); j++) {
					Enemy enemy = enemies.get(j);

					if (CollisionDetector.hasCollided(projectile, enemy) && !enemy.isDead()) {

						projectiles.remove(i);
						SoundManager.playSound(2, 1);
						
						if (!(enemy instanceof Rock)) {
							enemy.setCaptured(projectile.getAmmoType());
						}

						i--;			
						break;
					}
				}
			}
		}
	}
	
	/**
	 * Updates the location and states of the enemies on the game panel.
	 */
	private void updateEnemies() {
		
		// If there are enemies to update, update them.
		if (!enemies.isEmpty()) {
			for (int i = 0; i < enemies.size(); i++) {
				Enemy enemy = enemies.get(i);
				
				enemy.update();
				
				//Spawns enemy off screen
				if (enemy.getY() > 0) {
					enemy.setSpawned();
				}
				
				
				//Seahorse enemies
				if(enemy instanceof Seahorse){
					Seahorse  seahorse = (Seahorse)enemy;
					
					for(int j = 0; j < projectiles.size(); j++){
						Projectile projectile = projectiles.get(j);
						
						if((( Maths.distance(seahorse.getX(), seahorse.getY(), projectile.getX(), projectile.getY()) < 90)) && !seahorse.isDead() && (seahorse.getBoost() > 0)){

							
							if((CollisionDetector.getScreenWidth() - seahorse.getX() < 50) || (seahorse.getX() < 50)){
								Log.d(TAG, "Close to wall");
								seahorse.setYA(-20);
							}
							
							if(projectile.getX() <= seahorse.getX()){
								seahorse.setXV(10);
								seahorse.setBoost(0);
//								seahorse.setYA(3);
							}
							else if(projectile.getX() > seahorse.getX()){
								seahorse.setXV(-10);
								seahorse.setBoost(0);
//								seahorse.setYA(3);
							}
//							seahorse.setYA(3);
						}
						
						else if(( Maths.distance(seahorse.getX(), seahorse.getY(), projectile.getX(), projectile.getY()) > 200)){
							
//							seahorse.setYA(0);
//							seahorse.setXV(seahorse.getFallSpeed());
							//fix this!
							if(seahorse.getXV() > 0){
								seahorse.setXV(seahorse.getXV()- 1);
							}
							else if(seahorse.getXV() < 0){
								seahorse.setXV(seahorse.getXV()+ 1);
							}
						}
						
					}
				}
				
				else if (enemy instanceof Rock) {
					Rock rock = (Rock)enemy;
					
					if (CollisionDetector.hasCollided(player, rock)) {
						player.stun();
						enemies.remove(i);
						i--;
					}
					
					else if (CollisionDetector.hitTestBottomWall(rock)) {
						enemies.remove(i);
						i--;
					}
				}
				
				// Removes the enemy if it is off screen.
				if ( (enemy.getY() >= getHeight()) || 
						((enemy.getX() >= getWidth()
						|| enemy.getX() + enemy.getWidth() < 0
						|| enemy.getY() + 2 * (enemy.getHeight() + Pearl.bitmapHeight(getContext())) < 0)
						&& enemy.isSpawned()) ) {
					enemies.remove(i);
					
					i--;
					
					Log.d(TAG, "Enemy removed");
				}
			}
		}
		
		// Otherwise, skip to the next enemy wave if the spawning AI
		// has started and there are still more enemy to spawn.
		else {
			if (ai.isStarted() && !enemySpawnFinished) {
				ai.skipToNextWave();
			}
		}
	}
	
	/**
	 * Updates the location and states of the ammo drops on the game panel.
	 */
	private void updateAmmoDrops() {
		for (int i = 0; i < ammoDrops.size(); i++) {
			AmmoDrop ammoDrop = ammoDrops.get(i);
			
			ammoDrop.update();
			
			// Remove the ammo drop if it has collided with the player.
			if (CollisionDetector.hasCollided(player, ammoDrop)){
				ammoDrops.remove(i);
				
				incrementAmmo(ammoDrop.getAmmoType(), 1);
				
				i--;
				
				Log.d(TAG, "Ammo drop collected");
			}
			
			// Remove the ammo drop if it has collided with the floor.
			else if (ammoDrop.getY() + ammoDrop.getHeight() >= getHeight()) {
				ammoDrops.remove(i);
				
				i--;
				
				Log.d(TAG, "Ammo drop removed");
			}
		}
	}
	
	/**
	 * Updates the targeting AI of all the enemies on the game panel.
	 */
	private void updateEnemyTargeting() {
		for (int i = 0; i < enemies.size(); i++) {
			Enemy enemy = enemies.get(i);
			
			// Rock ignores pearls.
			if (enemy instanceof Rock) {
				continue;
			}
			
			// Turn on the targeting AI if the enemy is not dead, does not have a life, and is past 3/4 of the screen
			if (!enemy.isDead() && !enemy.hasLife() && (int) enemy.getHitbox().bottom >= (int) (getHeight() - getHeight() / 4)) {
				double closestDistance = Double.MAX_VALUE;
				int closestLife = -1;
				
				// Checks the distance between the enemy and each life.
				for (int j = 0; j < lives.length; j++) {
					if (lives[j] != null && !lives[j].isCaught() && !lives[j].isInvulnerable()) {
						double distance = Math.sqrt(Math.pow((enemy.centerX() - lives[j].centerX()), 2) + Math.pow((enemy.getHitbox().bottom - lives[j].getHitbox().top), 2));
						
						// If the distance is less than 3, the enemy has captured the life.
						if (distance <= 3) {
							lives[j].setCaught(enemy);
							enemy.setHasLife(lives[j]);
						}
						
						// Keep track of the closest life to the enemy.
						else if (distance < closestDistance) {
							closestDistance = distance;
							closestLife = j;
						}
					}
				}
				
				// If the enemy does not have a life and there is a life to capture,
				// target the closest life.
				if (!enemy.hasLife() && closestLife != -1) {
					enemy.setTarget(lives[closestLife].centerX(), lives[closestLife].getHitbox().top);
				}
				
				// Otherwise, continue as normal.
				else {
					//Enemies go back up here..
					enemy.setNoTarget();
					if( (lives[0] == null || lives[0].isCaught()) && (lives[1] == null || lives[1].isCaught()) && (lives[2] == null || lives[2].isCaught()) ){
						//set enemies to go back up
						enemy.setNoMoreLife(true);
					}
				}
			}

		}
	}
	
	private void checkVictory(){
		if (enemySpawnFinished && enemies.size() == 0){
			int score = 0;
			for (int i = 0; i < lives.length; i++){
				if (lives[i] != null){
					score++;
				}
			}
			
			victoryMenu.setLives(score);
			
			// Replace the old score with the new score if better
			if (SaveDataHandler.loadLevelData(levelID) < score){
				SaveDataHandler.storeLevelData(levelID, score);
			}
			
			// Activate the next level
			if (levelID < SaveDataHandler.NUM_LEVELS){
				if (SaveDataHandler.loadLevelData(levelID + 1) == -1){ // if not active
					SaveDataHandler.storeLevelData(levelID + 1, 0);
				}
			}
			
			
			gameStatus = VICTORY;
		}
	}
	
	private void checkFail(){
		boolean noLives = true;
		for (int i = 0; i < lives.length; i++){
			if (lives[i] != null){
				noLives = false;
				break;
			}
		}
		
		if (noLives){
			gameStatus = FAILED;
		}
	}
	
	public void render (Canvas canvas) {
		canvas.drawColor(0, PorterDuff.Mode.CLEAR);
		
		player.draw(canvas);
		
		for (int i = 0; i < projectiles.size(); i++){
			projectiles.get(i).draw(canvas);
		}
		
		for (int i = 0; i < enemies.size(); i++) {
			enemies.get(i).draw(canvas);
		}
		
		for (int i = 0; i < ammoDrops.size(); i++) {
			ammoDrops.get(i).draw(canvas);
		}
		
		for(int i = 0; i < lives.length; i++){
			if (lives[i] != null) {
				lives[i].draw(canvas);
			}
		}
		
		if (gameStatus != PLAYING){
			if (gameStatus == PAUSED){
				pauseMenu.draw(canvas);
			}else if (gameStatus == VICTORY){
				victoryMenu.draw(canvas);
			}else if (gameStatus == FAILED){
				failMenu.draw(canvas);
			}else{
				Log.e(TAG, "Invalid gameStatus state.");
			}
			
			Message message = new Message();
			message.what = 2;
			mainHandler.sendMessage(message);
		}
	}
	
	/**
	 * Increments ammo in ammoBar.
	 * Asks the main thread (which is associated with the handler) to
	 * call the incrementAmmo method in ammoBar.
	 * 
	 * @param ammoType
	 * @param ammoCount
	 */
	private void incrementAmmo(String ammoType, int ammoCount){
		Bundle messageData = new Bundle();
		messageData.putString("ammoType", ammoType);
		messageData.putInt("ammoCount", ammoCount);
		
		Message message = new Message();
		message.what = 0;
		message.setData(messageData);
		
		mainHandler.sendMessage(message);
	}

	//Touch event for projectiles
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (gameStatus == PLAYING){
			if(event.getAction() == MotionEvent.ACTION_DOWN){
				
				if (event.getY() <= player.getY() && !player.isStunned()){
					String ammoType = ammoBar.useAmmo();
				
					if (ammoType.equals("B_Bubble")){
						SoundManager.playSound(1, 1);
						BlueBubble bubble = new BlueBubble(getContext(), event.getX(), event.getY(), player.getX() + player.getWidth() / 2, player.getY());
						projectiles.add(bubble);
					}else if (ammoType.equals("R_Bubble")){
						SoundManager.playSound(1, 1);
						RedBubble bubble = new RedBubble(getContext(), event.getX(), event.getY(), player.getX() + player.getWidth() / 2, player.getY());
						projectiles.add(bubble);
					}else if (ammoType.equals("P_Bubble")){
						SoundManager.playSound(1, 1);
						PurpleBubble bubble = new PurpleBubble(getContext(), event.getX(), event.getY(), player.getX() + player.getWidth() / 2, player.getY());
						projectiles.add(bubble);
					}else if (ammoType.equals("G_Bubble")){
						SoundManager.playSound(1, 1);
						GreenBubble bubble = new GreenBubble(getContext(), event.getX(), event.getY(), player.getX() + player.getWidth() / 2, player.getY());
						projectiles.add(bubble);
					}else if (ammoType.equals("W_Bubble")){
						SoundManager.playSound(1, 1);
						WhiteBubble bubble = new WhiteBubble(getContext(), event.getX(), event.getY(), player.getX() + player.getWidth() / 2, player.getY());
						projectiles.add(bubble);
					}
				}
			}
		}else{ // if current session not active
			if (gameStatus == PAUSED){
				pauseMenu.handleActionDown((int)event.getX(), (int)event.getY());
				
				if (pauseMenu.isTouched()){
					int selection = pauseMenu.getSelection();
					
					if (selection == PauseMenu.RESUME){
						gameStatus = PLAYING;
						startThread();
					}else if (selection == PauseMenu.RESTART){
						createGame(getContext());
						startThread();
					}else if (selection == PauseMenu.LEVEL_SELECT){
						((GameActivity)getContext()).gotoLevelActivity();
					}else if (selection == PauseMenu.MAIN_MENU){
						((GameActivity)getContext()).gotoMainActivity();
					}
					
					pauseMenu.setTouched(false);
				}
			}else if (gameStatus == VICTORY){
				victoryMenu.handleActionDown((int)event.getX(), (int)event.getY());
				
				if (victoryMenu.isTouched()){
					int selection = victoryMenu.getSelection();
					
					if (selection == VictoryMenu.REPLAY){
						createGame(getContext());
						startThread();
					}else if (selection == VictoryMenu.NEXT_LEVEL){
						if (levelID < SaveDataHandler.NUM_LEVELS){
							levelID++;
							createGame(getContext());
							startThread();
						}else{
							((GameActivity)getContext()).gotoLevelActivity();
						}
					}else if (selection == VictoryMenu.LEVEL_SELECT){
						((GameActivity)getContext()).gotoLevelActivity();
					}else if (selection == VictoryMenu.MAIN_MENU){
						((GameActivity)getContext()).gotoMainActivity();
					}
					
					victoryMenu.setTouched(false);
				}
			}else if (gameStatus == FAILED){
				failMenu.handleActionDown((int)event.getX(), (int)event.getY());
				
				if (failMenu.isTouched()){
					int selection = failMenu.getSelection();
					
					if (selection == FailMenu.REPLAY){
						createGame(getContext());
						startThread();
					}else if (selection == FailMenu.LEVEL_SELECT){
						((GameActivity)getContext()).gotoLevelActivity();
					}else if (selection == FailMenu.MAIN_MENU){
						((GameActivity)getContext()).gotoMainActivity();
					}
					
					failMenu.setTouched(false);
				}
			}else{
				Log.e(TAG, "Invalid gameStatus state.");
			}
		}
		
		return true;
	}
	
	/**
	 * Called by GameActivity when back button or the home button is pressed.
	 * 
	 * @return	True if the current game is active.
	 */
	public boolean gamePlaying(){
		if (gameStatus == PLAYING){
			return true;
		}
		return false;
	}
	
	/**
	 * Called by GameActivity when menu button is pressed.
	 * 
	 * @return	True if the current game is paused.
	 */
	public boolean gamePaused(){
		if (gameStatus == PAUSED){
			return true;
		}
		return false;
	}
	
	/**
	 * Called by GameActivity when back button or the home button is pressed.
	 * Called after gamePlaying() if gamePlaying() returns true.
	 */
	public void pauseGame(){
		if (gameStatus == PLAYING){ // game is active
			gameStatus = PAUSED;
		}
	}
	
	/**
	 * Called by GameActivity when menu button is pressed.
	 * Called after gamePaused() if gamePaused() returns true.
	 */
	public void resumeGame(){
		if (gameStatus == PAUSED){ // game is active
			gameStatus = PLAYING;
			startThread();
		}
	}

	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
    	if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT){
    		player.keyDownLeft();
    		return true;
    	}else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
    		player.keyDownRight();
    		return true;
    	}
		return false;
    	
    }
	
	@Override
    public boolean onKeyUp(int keyCode, KeyEvent event){
    	if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT){
    		player.keyUpLeft();
    		return true;
    	}else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
    		player.keyUpRight();
    		return true;
    	}
		return false;
    	
    }
	
	class MainThreadHandler extends Handler{
		@Override
		public void handleMessage(Message msg){
			if (msg.what == 0){ // AmmoBar increment ammo
				String ammoType = msg.getData().getString("ammoType");
				int ammoCount = msg.getData().getInt("ammoCount");
				ammoBar.incrementAmmo(ammoType, ammoCount);
				Log.d(TAG, "AmmoType: " + ammoType + ", AmmoCount: " + ammoCount);
			}else if (msg.what == 1){ // AmmoBar reset
				ammoBar.reset();
				ammoBar.addStartingAmmo(levelID);
			}else if (msg.what == 2){ // PauseMenu
				stopThread();
			}else{
				Log.e(TAG, "HandleMessage in MainThreadHandler cannot identify the call.");
			}
			
		}
	}
}

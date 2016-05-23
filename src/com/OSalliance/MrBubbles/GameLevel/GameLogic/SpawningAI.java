package com.OSalliance.MrBubbles.GameLevel.GameLogic;

import java.util.ArrayList;
import java.util.Random;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.OSalliance.MrBubbles.GameLevel.GamePanel;
import com.OSalliance.MrBubbles.GameLevel.Sprites.AmmoDrop;

/**
 * An AI used to spawn enemies and ammo drops based on the the specified level
 * given to it. It spawns enemies and ammo in sequence or randomly with delays
 * between each spawn using timers.
 * 
 * @author Raymond Lee
 */
public class SpawningAI {
	private static final String TAG = SpawningAI.class.getSimpleName();
	
	private GamePanel panel;							// The game panel this spawning AI is spawning for.
	private ArrayList<EnemyWave> enemyWaves;			// The enemy waves to spawn for the specified level.
	private ArrayList<AmmoDrop> ammoDrops;				// The ammo drops to spawn for the specified level.
	private EnemySpawnTimer currentEnemyTimer;			// The countdown timer used to spawn the current enemy wave after a certain delay.
	private AmmoSpawnTimer currentAmmoTimer;			// The countdown timer used to spawn the current ammo drop after a certain delay.
	private Random randomGenerator;						// The random number generator used to set random location and delay.
	private SkipToNextWaveHandler skipToNextWaveHandler;// The handler used to handle skip to next wave calls.
	private String spawnType;							// The spawning type, either "sequential" or "random".
	private long ammoDropDelayInterval;					// The average delay interval between each ammo drop spawn.
	private long extraAmmoDelay;						// Extra delay for the next ammo drop.
	private int screenWidth;							// The width of the screen.
	private boolean isStarted;							// Whether the spawning AI has started spawning.
	private boolean isFinished;							// Whether the spawning AI has finished spawning.
	
	/**
	 * Initializes all global variables, as well as passing the game panel,
	 * screen width, screen height, and level id to the LevelHandler to get
	 * a list of enemy waves to spawn.
	 * 
	 * @param panel The game panel creating this spawning AI.
	 * @param width The width of the screen.
	 * @param height The height of the screen.
	 * @param levelID The level ID representing the level to spawn enemy waves for.
	 */
	public SpawningAI(GamePanel panel, double width, double height, int levelID) {
		this.panel = panel;
		enemyWaves = LevelHandler.getEnemyWaves(panel, width, levelID);
		ammoDrops = new ArrayList<AmmoDrop>();
		currentEnemyTimer = null;
		currentAmmoTimer = null;
		skipToNextWaveHandler = new SkipToNextWaveHandler();
		randomGenerator = new Random();
		spawnType = null;
		ammoDropDelayInterval = 0;
		extraAmmoDelay = 0;
		screenWidth = (int)width;
		isStarted = false;
		isFinished = false;
	}
	
	/**
	 * Resets the spawning AI to be used again with a new level.
	 * 
	 * @param levelID The new level to use the spawning AI for.
	 */
	public void reset(int levelID) {
		enemyWaves = LevelHandler.getEnemyWaves(panel, screenWidth, levelID);
		ammoDrops = new ArrayList<AmmoDrop>();
		currentEnemyTimer = null;
		currentAmmoTimer = null;
		spawnType = null;
		ammoDropDelayInterval = 0;
		extraAmmoDelay = 0;
		isStarted = false;
		isFinished = false;
	}
	
	/**
	 * Returns whether the spawning AI has been started yet.
	 * 
	 * @return Whether the spawning AI has started yet.
	 */
	public boolean isStarted() {
		return isStarted;
	}
	
	/**
	 * Pauses the spawning AI timers, by stopping the current enemy wave and
	 * ammo drop tasks, and stores the remaining time for the tasks as their
	 * new scheduled delay.
	 */
	public void pauseSpawning() {
		// Only pause spawning if spawning has started.
		if (isStarted && !isFinished) {
			currentEnemyTimer.cancel();
			currentAmmoTimer.cancel();

			currentEnemyTimer = new EnemySpawnTimer(currentEnemyTimer);
			currentAmmoTimer = new AmmoSpawnTimer(currentAmmoTimer);
		}
		
		// Otherwise do nothing.
		else {
			Log.w(TAG, "pauseSpawning() called when spawning AI has not started or is already finished.");
		}
	}

	/**
	 * Resumes the spawning AI timers by scheduling the tasks again with the
	 * scheduled remaining time left stored in the tasks.
	 */
	public void resumeSpawning() {
		if (!isFinished) {
			currentEnemyTimer.start();
			currentAmmoTimer.start();
		}
	}

	/**
	 * Start spawning enemy waves and ammo drops with the specified spawning method,
	 * the two possible spawning methods being "sequential" and "random". The spawning is
	 * started by starting a enemy wave task immediately.
	 * 
	 * @param spawnType The type of spawning to use, either "sequential" or "random".
	 */
	public void startEnemyWaves(String spawnType) {
		this.spawnType = spawnType;
		
		// Checks if enemy waves started already, or if enemyWaves is empty
		// before trying to start an enemy wave.
		if (!isStarted && !enemyWaves.isEmpty()) {
			EnemyWave enemyWave = getNextWave();
			
			currentEnemyTimer = new EnemySpawnTimer(this, panel, enemyWave, 0);
			currentEnemyTimer.start();
			
			currentAmmoTimer = new AmmoSpawnTimer(this, panel, null, 0);
			
			isStarted = true;
		}
		
		else {
			Log.w(TAG, "Could not start enemy waves: either already started, or enemyWaves is empty.");
			
			isFinished = true;
			panel.setEnemySpawnFinished(isFinished);
		}
	}
	
	/**
	 * Schedules the next enemy wave from the list of enemy waves, as well as get
	 * the ammo drops associated with this next wave. Also calls to schedule an
	 * ammo drop to start the ammo drops associated with the enemy wave.
	 */
	public void scheduleNextWave() {
		if (!enemyWaves.isEmpty()) {
			EnemyWave enemyWave = getNextWave();
			
			if (!ammoDrops.isEmpty()) {
				Log.w(TAG, "ammoDrops not empty when scheduling next wave.");
				
				ammoDrops.clear();
			}
			
			ammoDrops.addAll(enemyWave.getAmmoDrops());

			// Set each ammo drop's location to the spawn location, which is above the top
			// of the screen.
			for (AmmoDrop ammoDrop : ammoDrops) {
				int randLocation = randomGenerator.nextInt(screenWidth - ammoDrop.getWidth() + 1);

				ammoDrop.setY(ammoDrop.getY() - ammoDrop.getHeight());
				ammoDrop.setX(randLocation);
			}

			// Gets the enemy wave delay for the current enemy wave task from the previous enemy wave task.
			long enemyDelay = currentEnemyTimer.getEnemyWaveDelay();
			
			currentEnemyTimer = new EnemySpawnTimer(this, panel, enemyWave, enemyDelay);
			currentEnemyTimer.start();
			
			if (!ammoDrops.isEmpty()) {
				// Ammo drop average interval is the amount of time given for this enemy wave
				// divided by the number of ammo drops.
				ammoDropDelayInterval = enemyDelay / ammoDrops.size();

				scheduleNextAmmoDrop();
			}
		}
		
		else if (isFinished) {
			Log.w(TAG, "scheduleNextWave() called when enemy spawn is finished.");
		}
		
		else {
			isFinished = true;
			panel.setEnemySpawnFinished(isFinished);
		}
	}
	
	/**
	 * Schedules another ammo drop from the list of ammo drops.
	 */
	public void scheduleNextAmmoDrop() {
		if (!ammoDrops.isEmpty()) {
			int randAmmo = randomGenerator.nextInt(ammoDrops.size());
			
			AmmoDrop ammoDrop = ammoDrops.get(randAmmo);
			ammoDrops.remove(randAmmo);
			
			// Set a random delay for the ammo drop that is at most the ammoDrop delay interval.
			int delay = randomGenerator.nextInt((int)ammoDropDelayInterval + 1);
			
			currentAmmoTimer = new AmmoSpawnTimer(this, panel, ammoDrop, delay + extraAmmoDelay);
			currentAmmoTimer.start();
			
			// Extra delay for the next ammo drop if this ammo drop spawns early for its interval.
			extraAmmoDelay = ammoDropDelayInterval - delay;
		}
	}
	
	/**
	 * Skips to the next enemy wave.
	 */
	public void skipToNextWave() {
		Message message = new Message();
		skipToNextWaveHandler.sendMessage(message);
	}
	
	/**
	 * Gets the next enemy wave from the list of enemy waves. Based on the spawn type,
	 * either the next enemy wave at the front of the list is returned, or a random
	 * enemy wave from the list is returned.
	 * 
	 * @return the next enemy wave
	 */
	private EnemyWave getNextWave() {
		if (!enemyWaves.isEmpty()) {
			EnemyWave enemyWave = null;
			
			if (spawnType.equals("sequential")) {
				enemyWave = enemyWaves.get(0);
				enemyWaves.remove(0);
			}
			
			else if (spawnType.equals("random")) {
				int randWave = randomGenerator.nextInt(enemyWaves.size());
				
				enemyWave = enemyWaves.get(randWave);
				enemyWaves.remove(randWave);
			}
			
			else {
				Log.w(TAG, "Spawn type not specified correctly");
			}
			
			// Sets the spawn location of the enemy wave to a random x location.
			int randLocation = randomGenerator.nextInt((screenWidth - enemyWave.getWaveWidth()) + 1);
			
			enemyWave.setSpawnLocation(randLocation);
			
			return enemyWave;
		}
		
		else {
			Log.w(TAG, "getNextWave() called with empty enemyWaves.");
			
			return null;
		}
	}
	
	/**
	 * Handler class to handle skip to next wave calls.
	 */
	class SkipToNextWaveHandler extends Handler{
		@Override
		public void handleMessage(Message msg){
			if (isStarted && !isFinished) {
				currentEnemyTimer.onFinish();
			}
			
			else {
				Log.w(TAG, "skipToNextWave() called when spawning AI is not started yet, or enemy spawn is finished.");
				
				isFinished = true;
				panel.setEnemySpawnFinished(isFinished);
			}
		}
	}
}

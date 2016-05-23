package com.OSalliance.MrBubbles.GameLevel.GameLogic;

import com.OSalliance.MrBubbles.GameLevel.GamePanel;

import android.os.CountDownTimer;

/**
 * A CountDownTimer specializing in spawning an enemy wave.
 * 
 * @author Raymond Lee
 */
public class EnemySpawnTimer extends CountDownTimer {

	private SpawningAI ai;			// The spawning AI creating this TimerTask.
	private GamePanel panel;		// The game panel spawning the enemy wave.
	private EnemyWave enemyWave;	// The enemy wave to spawn.
	private long delay;				// The delay before the enemy wave spawns.
	
	/**
	 * Obtain a reference to the spawning AI creating this CountDownTimer,
	 * a reference to the game panel spawning the enemy wave, a reference
	 * to the enemy wave to spawn, and the delay before spawning.
	 * 
	 * @param ai The spawning AI creating this TimerTask.
	 * @param panel The GamePanel to spawn an enemy wave for.
	 * @param enemyWave The enemy wave to spawn.
	 * @param delay The delay before spawning the enemy wave.
	 */
	public EnemySpawnTimer(SpawningAI ai, GamePanel panel, EnemyWave enemyWave, long delay) {
		super(delay, 100);
		this.ai = ai;
		this.panel = panel;
		this.enemyWave = enemyWave;
		this.delay = delay;
	}

	/**
	 * Copy constructor to make a copy of an existing EnemySpawnTimer.
	 * 
	 * @param copy The EnemySpawnTimer to copy from.
	 */
	public EnemySpawnTimer(EnemySpawnTimer copy) {
		super(copy.delay, 100);
		ai = copy.ai;
		panel = copy.panel;
		enemyWave = copy.enemyWave;
		delay = copy.delay;
	}
	
	public long getEnemyWaveDelay() {
		return enemyWave.getDelay();
	}
	
	/**
	 * Called  when the length of the delay has passed, which will call
	 * the game panel's addEnemies method to add the enemy wave to the
	 * game panel, if it is not null. Then the next enemy wave will be
	 * scheduled by calling the spawning AI's scheduleNextWave method.
	 */
	@Override
	public void onFinish() {
		// Only add an enemy wave if it is not null.
		if (enemyWave.getEnemies() != null) {
			panel.addEnemies(enemyWave.getEnemies());

			ai.scheduleNextWave();

			cancel();	// Terminates the timer.
		}
		
		else {
			ai.scheduleNextWave();

			cancel();	// Terminates the timer.
		}
	}

	/**
	 * Updates the delay remaining before the enemy wave is spawned.
	 * 
	 * @param remainingDelay The amount of time until finished.
	 */
	@Override
	public void onTick(long remainingDelay) {
		delay = remainingDelay;
	}
	

}
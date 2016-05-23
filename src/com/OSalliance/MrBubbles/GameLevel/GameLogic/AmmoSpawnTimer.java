package com.OSalliance.MrBubbles.GameLevel.GameLogic;

import com.OSalliance.MrBubbles.GameLevel.GamePanel;
import com.OSalliance.MrBubbles.GameLevel.Sprites.AmmoDrop;

import android.os.CountDownTimer;

/**
 * A CountDownTimer specializing in spawning an ammo drop.
 * 
 * @author Raymond Lee
 */
public class AmmoSpawnTimer extends CountDownTimer {

	private SpawningAI ai;			// The spawning AI creating this TimerTask.
	private GamePanel panel;		// The game panel spawning the ammo drop.
	private AmmoDrop ammoDrop;		// The ammo drop to spawn.
	private long delay;				// The delay before the ammo drop spawns.
	
	/**
	 * Obtain a reference to the spawning AI creating this CountDownTimer,
	 * a reference to the game panel spawning the ammo drop, a reference
	 * to the ammo drop to spawn, and the delay before spawning.
	 * 
	 * @param ai The spawning AI creating this TimerTask.
	 * @param panel The GamePanel to spawn an ammo drop for.
	 * @param ammoDrop The ammo drop to spawn.
	 * @param delay The delay before spawning the ammo drop.
	 */
	public AmmoSpawnTimer(SpawningAI ai, GamePanel panel, AmmoDrop ammoDrop, long delay) {
		super(delay, 100);
		this.ai = ai;
		this.panel = panel;
		this.ammoDrop = ammoDrop;
		this.delay = delay;
	}

	/**
	 * Copy constructor to make a copy of an existing AmmoSpawnTimer.
	 * 
	 * @param copy The AmmoSpawnTimer to copy from.
	 */
	public AmmoSpawnTimer(AmmoSpawnTimer copy) {
		super(copy.delay, 100);
		ai = copy.ai;
		panel = copy.panel;
		ammoDrop = copy.ammoDrop;
		delay = copy.delay;
	}
	
	/**
	 * Called  when the length of the delay has passed, which will call
	 * the game panel's addAmmoDrop method to add the ammo drop to the
	 * game panel, if the ammo drop is not null. Then the next ammo drop
	 * will be scheduled by calling the spawning AI's scheduleNextAmmoDrop method.
	 */
	@Override
	public void onFinish() {
		// Only add the ammo drop if it is not null.
		if (ammoDrop != null) {
			panel.addAmmoDrop(ammoDrop);

			ai.scheduleNextAmmoDrop();

			cancel();	// Terminates the timer.
		}
		
		else {
			ai.scheduleNextAmmoDrop();
			
			cancel();	// Terminates the timer.
		}
	}

	/**
	 * Updates the delay remaining before the ammo drop is spawned.
	 * 
	 * @param remainingDelay The amount of time until finished.
	 */
	@Override
	public void onTick(long remainingDelay) {
		delay = remainingDelay;
	}
	

}
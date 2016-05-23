package com.OSalliance.MrBubbles.GameLevel.GameLogic;

import java.util.ArrayList;

import android.util.Log;

import com.OSalliance.MrBubbles.GameLevel.Sprites.AmmoDrop;
import com.OSalliance.MrBubbles.GameLevel.Sprites.Enemies.Enemy;
import com.OSalliance.MrBubbles.GameLevel.Sprites.Enemies.Jellyfish;
import com.OSalliance.MrBubbles.GameLevel.Sprites.Enemies.Octopus;

/**
 * Representation of a group of enemies to be spawned all at once in the form
 * of an ArrayList of enemies, which contains ammo drops associated with the
 * group of enemies also in the form of an ArrayList of ammo drops.
 * 
 * @author Raymond Lee
 */
public class EnemyWave {
	private static final String TAG = SpawningAI.class.getSimpleName();
	
	private ArrayList<Enemy> enemies;		// List of enemies associated with this enemy wave.
	private ArrayList<AmmoDrop> ammoDrops;	// List of ammo drops associated with this enemy wave.
	private int Xmin;						// The leftmost x coordinate of the enemy wave.
	private int Ymax;						// The highest y coordinate of the enemy wave.
	private int groupWidth;					// The total width of the entire enemy wave.
	private long delay;						// The delay associated with the enemy wave.
	private boolean finalized;				// Whether the enemy wave is ready to spawn.
	
	/**
	 * Initializes the ArrayList of enemies and the ArrayList of ammo drops,
	 * as well as all global variables.
	 */
	public EnemyWave() {
		enemies = new ArrayList<Enemy>();
		ammoDrops = new ArrayList<AmmoDrop>();
		Xmin = 0;
		Ymax = 0;
		groupWidth = 0;
		delay = 0;
		finalized = false;
	}
	
	/**
	 * Copy constructor to make a copy of an existing EnemyWave.
	 * 
	 * @param copy The EnemyWave to copy from.
	 */
	public EnemyWave(EnemyWave copy) {
		enemies = new ArrayList<Enemy>();
		ammoDrops = new ArrayList<AmmoDrop>();
		Xmin = copy.Xmin;
		Ymax = copy.Ymax;
		groupWidth = copy.groupWidth;
		delay = copy.delay;
		finalized = copy.finalized;
		
		/*
		 * To copy each enemy from the enemy wave, the type of enemy
		 * must first be determined, and then call the copy constructor
		 * of that specific enemy type.
		 */
		for (Enemy enemy : copy.enemies) {
			if (enemy instanceof Jellyfish) {
				enemies.add(new Jellyfish((Jellyfish)enemy, enemy.getFallSpeed()));
			}
			
			else if (enemy instanceof Octopus) {
				enemies.add(new Octopus((Octopus)enemy, enemy.getFallSpeed()));
			}
		}
		
		// Copy the ArrayList of ammo drops from the enemy wave
		for (AmmoDrop ammoDrop : copy.ammoDrops) {
			ammoDrops.add(new AmmoDrop(ammoDrop, ammoDrop.getFallSpeed()));
		}
	}
	
	/**
	 * Adds an enemy into the enemy wave.
	 * 
	 * @param enemy The enemy to add.
	 */
	public void addEnemy(Enemy enemy) {
		enemies.add(enemy);
	}
	
	/**
	 * Returns the ArrayList of enemies in this enemy wave,
	 * if the enemy wave is finalized.
	 * 
	 * @return The ArrayList of enemies.
	 */
	public ArrayList<Enemy> getEnemies() {
		if (finalized) {
			return enemies;
		}
		
		else {
			Log.e(TAG, "Attempted to get an enemy wave that is not finalized");
			
			return enemies;
		}
	}
	
	/**
	 * Adds an ammo drop to be associated with this enemy wave.
	 * 
	 * @param ammoDrop The ammo drop to add.
	 */
	public void addAmmoDrop(AmmoDrop ammoDrop) {
		ammoDrops.add(ammoDrop);
	}
	
	/**
	 * Get an ArrayList of ammo drops associated with this enemy wave.
	 * 
	 * @return The ArrayList of ammo drops.
	 */
	public ArrayList<AmmoDrop> getAmmoDrops() {
		return ammoDrops;
	}
	
	/**
	 * Set the time delay used to wait before spawning another enemy wave
	 * which should be an estimate on how long it takes to clear
	 * this enemy wave.
	 * 
	 * @param delay The time delay to associate with this enemy wave.
	 */
	public void setDelay(long delay) {
		this.delay = delay;
	}
	
	/**
	 * Get the time delay associated with this enemy wave.
	 * 
	 * @return The time delay.
	 */
	public long getDelay() {
		return delay;
	}
	
	/**
	 * Get the total width of the enemy wave.
	 * 
	 * @return The total width of the enemy wave.
	 */
	public int getWaveWidth() {
		return groupWidth;
	}
	
	/**
	 * Sets the spawn location of the enemy wave, by placing the left side of
	 * the enemy wave on the given x coordinate.
	 * 
	 * @param x The x coordinate to set the left side of the enemy wave to.
	 */
	public void setSpawnLocation(int x) {
		for (Enemy enemy : enemies) {
			enemy.setX(enemy.getX() + x);
		}
	}
	
	/**
	 * Returns whether the enemy wave is finalized, which indicates
	 * whether the enemy wave is ready for spawning.
	 * 
	 * @return Whether the enemy wave is finalized.
	 */
	public boolean isFinalized() {
		return finalized;
	}
	
	/**
	 * Finalizes the enemy wave to be ready to spawn by setting the position
	 * of all the enemies in the enemy wave to be above the top of the screen
	 * and the left side of the wave aligned with the left side of the screen.
	 */
	public void finalize() {
		if (!finalized) {
			calculateGroupDimensions();
			
			for (Enemy enemy : enemies) {
				enemy.setX(enemy.getX() - Xmin);
				enemy.setY(enemy.getY() - Ymax);
			}
			
			finalized = true;
		}
		
		else {
			return;
		}
	}
	
	/**
	 * Used to calculate the leftmost x coordinate of the enemy wave, the greatest
	 * y coordinate of the enemy wave, and the group width, all used to finalize the
	 * enemy wave.
	 */
	private void calculateGroupDimensions() {
		int Xmax = (int)(enemies.get(0).getX() + enemies.get(0).getWidth());
		
		Xmin = (int)(enemies.get(0).getX());
		Ymax = (int)(enemies.get(0).getY() + enemies.get(0).getHeight());

		
		for (Enemy enemy : enemies) {
			if ((int)enemy.getX() < Xmin) {
				Xmin = (int)enemy.getX();
			}
			
			if ((int)(enemy.getX() + enemy.getWidth()) > Xmax) {
				Xmax = (int)(enemy.getX() + enemy.getWidth());
			}
			
			if ((int)(enemy.getY() + enemy.getHeight()) > Ymax) {
				Ymax = (int)(enemy.getY() + enemy.getHeight());
			}
		}
		
		groupWidth = Xmax - Xmin;
	}
}

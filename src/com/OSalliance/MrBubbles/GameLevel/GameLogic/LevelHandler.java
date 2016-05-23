package com.OSalliance.MrBubbles.GameLevel.GameLogic;

import java.util.ArrayList;
import java.util.Random;

import android.util.Log;

import com.OSalliance.MrBubbles.GameLevel.GamePanel;
import com.OSalliance.MrBubbles.GameLevel.Sprites.AmmoDrop;
import com.OSalliance.MrBubbles.GameLevel.Sprites.Enemies.Jellyfish;
import com.OSalliance.MrBubbles.GameLevel.Sprites.Enemies.Octopus;
import com.OSalliance.MrBubbles.GameLevel.Sprites.Enemies.Rock;
import com.OSalliance.MrBubbles.GameLevel.Sprites.Enemies.Seahorse;
import com.OSalliance.MrBubbles.GameLevel.Sprites.Enemies.Starfish;

/**
 * Contains information on what enemy groups and ammo drops appear in each level.
 * It contains a single method used to get the enemy groups pertaining to a specific level as an
 * ArrayList of enemy groups.
 * 
 * @author Raymond Lee
 */
public class LevelHandler {
	private static final String TAG = LevelHandler.class.getSimpleName();
	
	private static Random randomGenerator = new Random();	// The random number generator used to set random fall speed and delay.
	
	private static long randLong(long min, long max) {
		return randomGenerator.nextInt((int)(max-min) + 1) + min;
	}
	
	private static double randDouble(double min, double max) {
		return randomGenerator.nextDouble() * (max-min) + min;
	}
	
	/**
	 * Returns an ArrayList of enemy groups to spawn for a specific level.
	 * While building enemy waves, they must be finalized before returning them.
	 * 
	 * @param panel	The game panel the enemies will be drawn on.
	 * @param width The width of the screen.
	 * @param levelID The level ID of the requested level to get enemies for.
	 * @return An ArrayList of enemy waves to spawn for the specified level.
	 */
	public static ArrayList<EnemyWave> getEnemyWaves(GamePanel panel, double width, int levelID) {
		ArrayList<EnemyWave> enemyWaves = new ArrayList<EnemyWave>();
		
		switch (levelID) {
		
		case 1:
			
			for (int i = 0; i < 3; i++) {
				enemyWaves.add(getOneJellyfish(panel, randLong(1000,5000)));
				enemyWaves.add(getJellyfishRow(panel, randLong(6000,12000)));
				enemyWaves.add(getTwoSeahorse(panel, randLong(8000,12000)));
			}
			
			break;
			
		case 2:
			
			for (int i = 0; i < 3; i++) {
				enemyWaves.add(getOneOctopus(panel, randLong(2000,6000)));
				enemyWaves.add(getOctopusRow(panel, randLong(6000,12000)));
			}
			
			break;
			
		case 3:
			
			for (int i = 0; i < 3; i++) {
				enemyWaves.add(getOneJellyfish(panel, randLong(1000,5000)));
				enemyWaves.add(getJellyfishRow(panel, randLong(6000,12000)));
				enemyWaves.add(getOneOctopus(panel, randLong(2000,6000)));
				enemyWaves.add(getOctopusRow(panel, randLong(6000,12000)));
			}
			
			break;
			
		case 4:
			
			for (int i = 0; i < 3; i++) {
				enemyWaves.add(getTwoSeahorse(panel, randLong(8000,12000)));
			}
			
			break;
			
		case 5:
			
			for (int i = 0; i < 3; i++) {
				enemyWaves.add(getOneStarfish(panel, randLong(3000,7000)));
			}
			
			break;
			
		case 20:
			
			for (int i = 0; i < 3; i++) {
				enemyWaves.add(getOneRock(panel, randLong(2000,6000)));
			}
			
			break;
			
		default:
			
			Log.e(TAG, "Invalid levelID");
		}
		
		return enemyWaves;
	}
	
	private static void addBBubbleDrops(GamePanel panel, EnemyWave enemyWave, int amount) {
		AmmoDrop bBubble = new AmmoDrop(panel.getContext(), 0, 0, "B_Bubble", randDouble(2,4));
		
		for (int i = 0; i < amount; i++) {
			enemyWave.addAmmoDrop(new AmmoDrop(bBubble, 3));
		}
	}
	
	private static void addPBubbleDrops(GamePanel panel, EnemyWave enemyWave, int amount) {
		AmmoDrop pBubble = new AmmoDrop(panel.getContext(), 0, 0, "P_Bubble", randDouble(2,4));
		
		for (int i = 0; i < amount; i++) {
			enemyWave.addAmmoDrop(new AmmoDrop(pBubble, 3));
		}
	}
	
	private static void addRBubbleDrops(GamePanel panel, EnemyWave enemyWave, int amount) {
		AmmoDrop rBubble = new AmmoDrop(panel.getContext(), 0, 0, "R_Bubble", randDouble(2,4));
		
		for (int i = 0; i < amount; i++) {
			enemyWave.addAmmoDrop(new AmmoDrop(rBubble, 3));
		}
	}
	
	private static void addGBubbleDrops(GamePanel panel, EnemyWave enemyWave, int amount) {
		AmmoDrop gBubble = new AmmoDrop(panel.getContext(), 0, 0, "G_Bubble", randDouble(2,4));
		
		for (int i = 0; i < amount; i++) {
			enemyWave.addAmmoDrop(new AmmoDrop(gBubble, 3));
		}
	}
	
	private static EnemyWave getOneJellyfish(GamePanel panel, long delay) {
		EnemyWave oneJellyfish = new EnemyWave();
		
		oneJellyfish.addEnemy(new Jellyfish(panel.getContext(), 0, 0, randDouble(1,3)));
		addBBubbleDrops(panel, oneJellyfish, 2);
		
		oneJellyfish.finalize();
		oneJellyfish.setDelay(delay);
		
		return oneJellyfish;
	}
	
	private static EnemyWave getJellyfishRow(GamePanel panel, long delay) {
		EnemyWave jellyfishRow = new EnemyWave();
		
		double jellyfishWidth = (new Jellyfish(panel.getContext(), 0, 0, 2)).getWidth();
		
		jellyfishRow.addEnemy(new Jellyfish(panel.getContext(), 0, 0, randDouble(1,3)));
		jellyfishRow.addEnemy(new Jellyfish(panel.getContext(), 3 * jellyfishWidth, 0, randDouble(1,3)));
		jellyfishRow.addEnemy(new Jellyfish(panel.getContext(), 6 * jellyfishWidth, 0, randDouble(1,3)));
		addBBubbleDrops(panel, jellyfishRow, 6);
		
		jellyfishRow.finalize();
		jellyfishRow.setDelay(delay);
		
		return jellyfishRow;
	}
	
	private static EnemyWave getOneOctopus(GamePanel panel, long delay) {
		EnemyWave oneOctopus = new EnemyWave();
		
		oneOctopus.addEnemy(new Octopus(panel.getContext(), 0, 0, randDouble(1.5,3.5)));
		addPBubbleDrops(panel, oneOctopus, 2);
		
		oneOctopus.finalize();
		oneOctopus.setDelay(delay);
		
		return oneOctopus;
	}
	
	private static EnemyWave getOctopusRow(GamePanel panel, long delay) {
		EnemyWave octopusRow = new EnemyWave();
		
		double octopusWidth = (new Octopus(panel.getContext(), 0, 0, 2)).getWidth();
		
		octopusRow.addEnemy(new Octopus(panel.getContext(), 0, 0, randDouble(1.5,3.5)));
		octopusRow.addEnemy(new Octopus(panel.getContext(), 3 * octopusWidth, 0, randDouble(1.5,3.5)));
		octopusRow.addEnemy(new Octopus(panel.getContext(), 6 * octopusWidth, 0, randDouble(1.5,3.5)));
		addPBubbleDrops(panel, octopusRow, 6);
		
		octopusRow.finalize();
		octopusRow.setDelay(delay);
		
		return octopusRow;
	}
	
	private static EnemyWave getTwoSeahorse(GamePanel panel, long delay) {
		EnemyWave twoSeahorse = new EnemyWave();
		
		double seahorseWidth = (new Seahorse(panel.getContext(), 0, 0, 2)).getWidth();
		
		twoSeahorse.addEnemy(new Seahorse(panel.getContext(), 0, 0, randDouble(1.5,3.5)));
		twoSeahorse.addEnemy(new Seahorse(panel.getContext(), 6 * seahorseWidth, 0, randDouble(1.5,3.5)));
		addRBubbleDrops(panel, twoSeahorse, 2);
		addGBubbleDrops(panel, twoSeahorse, 2);
		
		twoSeahorse.finalize();
		twoSeahorse.setDelay(delay);
		
		return twoSeahorse;
	}
	
	private static EnemyWave getOneStarfish(GamePanel panel, long delay) {
		EnemyWave oneStarfish = new EnemyWave();
		
		oneStarfish.addEnemy(new Starfish(panel.getContext(), 0, 0, randDouble(1,3)));
		addBBubbleDrops(panel, oneStarfish, 2);
		
		oneStarfish.finalize();
		oneStarfish.setDelay(delay);
		
		return oneStarfish;
	}
	
	private static EnemyWave getOneRock(GamePanel panel, long delay) {
		EnemyWave oneRock = new EnemyWave();
		
		oneRock.addEnemy(new Rock(panel.getContext(), 0, 0, randDouble(0.5,0.75)));
		
		oneRock.finalize();
		oneRock.setDelay(delay);
		
		return oneRock;
	}
}
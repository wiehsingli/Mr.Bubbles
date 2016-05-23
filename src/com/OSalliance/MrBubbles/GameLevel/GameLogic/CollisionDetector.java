package com.OSalliance.MrBubbles.GameLevel.GameLogic;

import com.OSalliance.MrBubbles.GameLevel.Sprites.Sprite;

/**
 * Static class used to determine whether a sprite has collided with a side
 * of the screen, or to determine whether two sprites has collided with each other.
 * 
 * @author Raymond Lee
 */
public class CollisionDetector {
	private static int screenWidth = 0;		// The width of the screen.
	private static int screenHeight = 0;	// The height of the screen.
	
	/**
	 * Defines the type of collision between two sprites.
	 */
	public enum CollisionType {
		HORIZONTAL,
		VERTICAL,
		DIAGONAL,
		NULL;
	}
	
	/**
	 * Sets the screen width and height for the collision detector to check collisions
	 * against the sides of the screen.
	 * 
	 * @param width The width of the screen.
	 * @param height The height of the screen.
	 */
	public static void setScreenDimensions(int width, int height) {
		screenWidth = width;
		screenHeight = height;
	}
	
	/**
	 * Gets the screen width the collision detector is using.
	 * 
	 * @return The width of the screen.
	 */
	public static int getScreenWidth() {
		return screenWidth;
	}
	
	/**
	 * Gets the screen height the collision detector is using.
	 * 
	 * @return The height of the screen.
	 */
	public static int getScreenHeight() {
		return screenHeight;
	}
	
	/**
	 * Checks if the hitbox of a sprite has collided with the left side of the screen.
	 * 
	 * @param sprite The sprite.
	 * @return Whether the sprite has collided with the left side of the screen.
	 */
	public static boolean hitTestLeftWall(Sprite sprite) {
		if (sprite.getX() <= 0) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Checks if the hitbox of a sprite has collided with the right side of the screen.
	 * 
	 * @param sprite The sprite.
	 * @return Whether the sprite has collided with the left side of the screen.
	 */
	public static boolean hitTestRightWall(Sprite sprite) {
		if (sprite.getX() + sprite.getWidth() >= screenWidth) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Checks if the hitbox of a sprite has collided with the top of the screen.
	 * 
	 * @param sprite the sprite.
	 * @return Whether the sprite has collided with the top of the screen.
	 */
	public static boolean hitTestTopWall(Sprite sprite) {
		if (sprite.getY() < 0) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Checks if the hitbox of a sprite has collided with the bottom of the screen.
	 * 
	 * @param sprite The sprite.
	 * @return Whether the sprite has collided with the bottom of the screen.
	 */
	public static boolean hitTestBottomWall(Sprite sprite) {
		if (sprite.getY() + sprite.getHeight() > screenHeight) {
			return true;
		}

		return false;
	}

	/**
	 * Checks if two sprites has collided with each other horizontally, vertically, or
	 * diagonally based on the closest point to the center of the sprite colliding.
	 * 
	 * @param sprite1 The first sprite.
	 * @param sprite2 The second sprite.
	 * @return Whether the two sprites have collided with each other horizontally, vertically, or diagonally.
	 */
	public static CollisionType getCollisionType(Sprite sprite1, Sprite sprite2) {
		double shortestDist = Double.MAX_VALUE;
		CollisionType type = CollisionType.NULL;
		
		for (double x = sprite2.getX(); x <= sprite2.getX() + sprite2.getWidth(); x += sprite2.getWidth()) {
			double dist = Maths.distance(sprite1.centerX(), sprite1.centerY(), x, sprite2.centerY());
			
			if (dist < shortestDist) {
				shortestDist = dist;
				type = CollisionType.HORIZONTAL;
			}
		}
		
		for (double y = sprite2.getY(); y <= sprite2.getY() + sprite2.getHeight(); y += sprite2.getHeight()) {
			double dist = Maths.distance(sprite1.centerX(), sprite1.centerY(), sprite2.centerX(), y);
			
			if (dist < shortestDist) {
				shortestDist = dist;
				type = CollisionType.VERTICAL;
			}
		}
		
		for (double x = sprite2.getX(); x <= sprite2.getX() + sprite2.getWidth(); x += sprite2.getWidth()) {
			for (double y = sprite2.getY(); y <= sprite2.getY() + sprite2.getHeight(); y += sprite2.getHeight()) {
				double dist = Maths.distance(sprite1.centerX(), sprite1.centerY(), x, y);
				
				if (dist < shortestDist) {
					type = CollisionType.DIAGONAL;
					
					return type;
				}
			}
		}
		
		return type;
	}

	/**
	 * Checks if two sprites has collided with each other, by testing whether
	 * their hitboxes intersect.
	 * 
	 * @param sprite1 The first sprite.
	 * @param sprite2 The second sprite.
	 * @return Whether the two sprites have collided with each other.
	 */
	public static boolean hasCollided(Sprite sprite1, Sprite sprite2) {
		return sprite1.getHitbox().intersect(sprite2.getHitbox());
	}
}

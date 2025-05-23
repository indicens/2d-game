package main;
/**
 * Handles basic collision detection, currently with a fixed floor rectangle.
 */
import java.awt.Rectangle;

public class CollisionCheck {
	/** The floor collision area (hardcoded at y = 500). */
	private Rectangle floor = new Rectangle(0, 500, 1280, 50); // Example ground
	/**
	 * Checks if the given position and hitbox would collide with the floor.
	 *
	 * @param newX    The new X position to check.
	 * @param newY    The new Y position to check.
	 * @param hitbox  The entity's current hitbox.
	 * @return        True if the new position intersects with the floor.
	 */
	public boolean isColliding(float newX, float newY, Rectangle hitbox) {
		Rectangle moved = new Rectangle((int) newX, (int) newY, hitbox.width, hitbox.height);
		return moved.intersects(floor);
	}
	/**
	 * Gets the corrected Y position just above the floor for collision resolution.
	 * Assumes the player's height is 64 pixels.
	 *
	 * @return The Y coordinate the player should be placed at to stand on the floor.
	 */
	public float getCorrectedY() {
		return floor.y - 64; // Assuming player height is 64
	}

}

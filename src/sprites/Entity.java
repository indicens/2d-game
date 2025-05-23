package sprites;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * Represents a basic entity with position, size, and a hitbox. This class can
 * be extended by other game objects like players or enemies.
 */
public class Entity {

	protected float x, y;
	protected int width, height;
	protected Rectangle hitbox;

	/**
	 * Constructs a new Entity with specified position and dimensions.
	 *
	 * @param x      The x-coordinate of the entity.
	 * @param y      The y-coordinate of the entity.
	 * @param width  The width of the entity.
	 * @param height The height of the entity.
	 */
	public Entity(float x, float y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

		inithitbox();
	}

	/**
	 * Draws the hitbox of the entity for debugging purposes.
	 *
	 * @param g The Graphics object to draw with.
	 */
	public void drawHitbox(Graphics g) {
		g.setColor(Color.PINK);
		g.drawRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
	}

	/**
	 * Initializes the hitbox based on the entity's current position and size.
	 */
	public void inithitbox() {
		// TODO Auto-generated method stub
		hitbox = new Rectangle((int) x, (int) y, width, height);

	}

	/**
	 * Updates the hitbox position to match the entity's current coordinates.
	 */
	public void updateHitbox() {
		hitbox.x = (int) x;
		hitbox.y = (int) y;

	}

	/**
	 * Returns the current hitbox of the entity.
	 *
	 * @return The hitbox rectangle.
	 */
	public Rectangle gethitbox() {
		return hitbox;
	}

	/**
	 * Sets the entity's position.
	 *
	 * @param x The new x-coordinate.
	 * @param y The new y-coordinate.
	 */
	public void setPos(float x, float y) {
		this.x = x;
		this.y = y;
	}

}

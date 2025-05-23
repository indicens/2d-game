package main;

import java.awt.Rectangle;

public class CollisionCheck {
	
    private Rectangle floor = new Rectangle(0, 500, 1280, 50); // Example ground

    public boolean isColliding(float newX, float newY, Rectangle hitbox) {
        Rectangle moved = new Rectangle((int)newX, (int)newY, hitbox.width, hitbox.height);
        return moved.intersects(floor);
    }

    public float getCorrectedY() {
        return floor.y - 64; // Assuming player height is 64
    }
	    
	}


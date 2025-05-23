package sprites;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Entity {
	
	protected float x, y;
	protected int width, height;
	protected Rectangle hitbox;
	
	public Entity(float x, float y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		inithitbox();
	}
	
	public void drawHitbox(Graphics g) {
		g.setColor(Color.PINK);
		g.drawRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
	}
	

	public void inithitbox() {
		// TODO Auto-generated method stub
		hitbox = new Rectangle((int) x, (int) y, width, height);
		
		
	}
	
	public void updateHitbox() {
		hitbox.x = (int) x;
		hitbox.y = (int) y;
		
	}
	
	public Rectangle gethitbox() {
		return hitbox;
	}
	

	public void setPos(float x, float y) {
		this.x = x;
		this.y = y;
	}

	
}

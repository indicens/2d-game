package levels;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Game;

public class LevelHandler {
	private Game game;
	private BufferedImage levelSprite;
	public LevelHandler(Game game) {
		this.game = game;
		
	}
	
	public void draw(Graphics g) {
		
		g.fillRect(50, 50, 1000, 1000);
		
	}
	
	
}

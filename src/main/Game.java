package main;

import java.awt.Graphics;



/**
 * The main Game class that initializes the game window, panel, and handles the
 * game loop including updating and rendering.
 */
public class Game implements Runnable {

	private GameWindow gameWindow;
	private GamePanel gamePanel;
	private Thread gameThread;
	
	/** Target frames per second. */
	private final int FPS_SET = 120;
	/** Target updates per second. */
	private final int UPS_SET = 200;

	private long lastCheck;
	private int frames = 0;
	long previousTime = System.nanoTime();

	/**
	 * Constructs the Game, initializing components and starting the game loop.
	 */
	public Game() {
		gamePanel = new GamePanel();
		gameWindow = new GameWindow(gamePanel);
		gamePanel.requestFocus();
		
		run();

	}

	/**
	 * Updates the game logic.
	 */
	public void update() {
		gamePanel.update(null);

	}

	

	/**
	 * The main game loop, runs continuously updating and rendering the game at
	 * fixed time steps.
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		double timePerFrame = 1000000000.0 / FPS_SET;
		double timePerUpdate = 1000000000.0 / UPS_SET;
		long lastFrame = System.nanoTime();

		int frames = 0;
		int updates = 0;
		long lastCheck = System.currentTimeMillis();
		double dU = 0;
		double dF = 0;

		long now = System.nanoTime();

		while (true) {

			now = System.nanoTime();
			long currentTime = System.nanoTime();

			dU += (currentTime - previousTime) / timePerUpdate;
			dF += (currentTime - previousTime) / timePerFrame;
			previousTime = currentTime;

			if (dU >= 1) {
				updates++;
				dU--;
			}

			if (dF >= 1) {
				gamePanel.repaint();
				dF--;
				frames++;

			}

			if (now - lastFrame >= timePerFrame) {
				gamePanel.repaint();
				lastFrame = now;
				frames++;
			}
			if (System.currentTimeMillis() - lastCheck >= 1000) {
				lastCheck = System.currentTimeMillis();
				System.out.println("FPS: " + frames + ", UPS: " + updates);
				frames = 0;
				updates = 0;
			}
		}
	}
}

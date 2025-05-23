package main;

import javax.swing.JFrame;

/**
 * Sets up the main game window using a JFrame. Handles window properties and
 * attaches the GamePanel.
 */

public class GameWindow {

	/** The main application window (JFrame). */
	private JFrame jframe;

	/**
	 * Constructs the game window and attaches the provided GamePanel.
	 *
	 * @param gamePanel The panel that contains and renders the game content.
	 */
	public GameWindow(GamePanel gamePanel) {
		jframe = new JFrame();

		// Set the window size before packing
		jframe.setSize(1280, 720);

		// Add the game panel to the window
		jframe.add(gamePanel);

		// Ensures proper layout sizing based on added components
		jframe.pack();

		// Prevent the user from resizing the window
		jframe.setResizable(false);

		// Center the window on the screen
		jframe.setLocationRelativeTo(null);

		// Define the close operation (exit the application)
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Make the window visible
		jframe.setVisible(true);
	}

}

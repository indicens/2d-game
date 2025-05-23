package key_inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import main.GamePanel;
import static utils.Constants.Direction.*;

/**
 * Handles keyboard input events for controlling the game. Implements
 * KeyListener to respond to key presses, releases, and typing.
 */
public class KeyboardInputs implements KeyListener {
	/** Reference to the GamePanel for updating game state based on input */
	private GamePanel gamePanel;

	/**
	 * Constructs a KeyboardInputs instance linked to the specified GamePanel.
	 * 
	 * @param gamePanel The GamePanel instance to control with keyboard input.
	 */
	public KeyboardInputs(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}

	/**
	 * Invoked when a key has been typed (pressed and released). Currently not used.
	 * 
	 * @param e The KeyEvent object representing the key typed.
	 */
	@Override
	public void keyTyped(KeyEvent e) {
		// Not used
	}

	/**
	 * Invoked when a key has been released. Stops the player movement if the A, S,
	 * or D keys are released.
	 * 
	 * @param e The KeyEvent object representing the key released.
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		switch (e.getKeyCode()) {
		case KeyEvent.VK_A:
		case KeyEvent.VK_S:
		case KeyEvent.VK_D:
			gamePanel.setMoving(false);
			break;
		}
	}

	/**
	 * Invoked when a key has been pressed. Sets the player's movement state to
	 * moving and changes direction according to the key pressed (WASD). Also
	 * triggers jump action when W is pressed.
	 * 
	 * @param e The KeyEvent object representing the key pressed.
	 */
	@Override
	public void keyPressed(KeyEvent e) {

		gamePanel.setMoving(true);
		switch (e.getKeyCode()) {
		case KeyEvent.VK_W:
			gamePanel.setDirection(UP);
			gamePanel.jump();
			break;
		case KeyEvent.VK_A:
			gamePanel.setDirection(LEFT);
			break;
		case KeyEvent.VK_S:
			gamePanel.setDirection(DOWN);
			break;
		case KeyEvent.VK_D:
			gamePanel.setDirection(RIGHT);
			break;

		}

	}
}

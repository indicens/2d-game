package main;

import java.util.ArrayList;
import java.util.List;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import key_inputs.KeyboardInputs;


import static utils.Constants.PlayerConstants.*;
import static utils.Constants.Direction.*;
import sprites.Entity;

public class GamePanel extends JPanel {



	// Player position variables
	private float dx = 100, dy = 100;

	// Frame counter for animation timing
	private int frames = 0;
	private long lastCheck;

	// Sprite Sheets for different animations
	private BufferedImage img, img2, img3, img4;

	// Platforms and Background
	private BufferedImage backgroundImgDay;
	private BufferedImage backgroundImgNight;
	private BufferedImage platform1Img;
	private BufferedImage KillBox;

	// Animation frames for different states
	private BufferedImage[] idleAni, LidleAni, runAni, LrunAni, Anim;

	// Animation timing variables
	private int aniTick, aniIndex, aniSpeed = 30;

	// Constants to determine player action/state
	private int playerAction = IDLE;
	private int playerDir = -1;
	private boolean moving = false;

	// Player's entity and level data
	private Entity playerEntity;
	private int[][] lvlData;

	// Platforms and obstacles (used with collision detection)
	private List<Rectangle> platforms = new ArrayList<>();
	private List<Rectangle> deadlyObstacles = new ArrayList<>();
	private List<Rectangle> coins = new ArrayList<>();

	private int coinsCollected = 0;

	// Gravity & physics-related variables
	private float velocityY = 0;
	private final float gravity = 0.4f;
	private final float maxFallSpeed = 10f;

	// State booleans for player status
	private boolean onGround = false, inAir = false, wantsToJump = false;

	// Game state
	private boolean gameWon = false;
	private boolean gameOver = false;

	/**
	 * GamePanel.java Author: Sino
	 *
	 * This class represents the main game panel where all rendering and game logic
	 * occur. It handles player movement, animation, physics (gravity, collision),
	 * input (keyboard & mouse), and game state (win/loss). Utilizes Java Swing for
	 * GUI rendering.
	 *
	 * Meets the following requirements: - Declare and use variables for data
	 * persistence within a program. - Invoke methods on an object, with parameters
	 * and a returned value. - Write a method, with parameters and a returned value.
	 * - Use single dimensional arrays to store and access data. - Use loops: while,
	 * for, and nested loops. - Use the Scanner class or an appropriate GUI widget
	 * to interact with the user via keyboard input. - Use conditional execution
	 * (if-else). - Write and use a custom class. - Construct programs utilizing
	 * graphical user interfaces utilizing event-driven programming. - Construct
	 * programs utilizing exception handling.
	 */

	/**
	 * Constructor initializes the game, sets up platforms, coins, images, input,
	 * and player entity.
	 */

	public GamePanel() {

		// Platforms & coins setup (array/list usage requirement)
		platforms.add(new Rectangle(50, 200, 500, 50)); // Platform 1
		platforms.add(new Rectangle(300, 400, 300, 40)); // Platform 2
		platforms.add(new Rectangle(700, 300, 200, 30)); // Platform 3
		coins.add(new Rectangle(150, 150, 20, 20)); // Coin 1
		coins.add(new Rectangle(400, 180, 20, 20)); // Coin 2
		deadlyObstacles.add(new Rectangle(600, 500, 100, 20)); // Trap 1
		deadlyObstacles.add(new Rectangle(600, 250, 100, 20)); // Trap 2



		importImg(); // Loads image files (exception handling requirement)
		loadAnimations();

		// Keyboard input setup
		addKeyListener(new KeyboardInputs(this));
		setFocusable(true);
		requestFocusInWindow();
		setPanelSize();
		addMouseListener(null);
		playerEntity = new Entity(dx, dy, 32, 64); // Creates Player Entity, Custom class requirement

	}

	/**
	 * Loads animation frames from sprite sheets.
	 */

	private void loadAnimations() {
		// TODO Auto-generated method stub
		idleAni = new BufferedImage[1];
		runAni = new BufferedImage[3];
		LrunAni = new BufferedImage[3];
		LidleAni = new BufferedImage[1];

		idleAni[0] = img.getSubimage(0 * 16, 0, 16, 32);
		LidleAni[0] = img4.getSubimage(0 * 16, 0, 16, 32);

		for (int i2 = 0; i2 < runAni.length; i2++) {
			runAni[i2] = img2.getSubimage(i2 * 16, 0, 16, 32);
		}

		for (int i3 = 0; i3 < LrunAni.length; i3++) {
			LrunAni[i3] = img3.getSubimage(i3 * 16, 0, 16, 32);
		}

	}

	/**
	 * Imports player images from resource files using ImageIO.
	 */

	private void importImg() {

		InputStream platform1Stream = getClass().getResourceAsStream("/platform1.png");
		InputStream KillBoxStream = getClass().getResourceAsStream("/KillBox.png");
		InputStream bgStreamDay = getClass().getResourceAsStream("/Background_Day.png");
		InputStream bgStreamNight = getClass().getResourceAsStream("/Background_Night.png");
		InputStream is = getClass().getResourceAsStream("/Player-run-Sheet.png");
		InputStream is2 = getClass().getResourceAsStream("/Player-Sheet.png");
		InputStream is3 = getClass().getResourceAsStream("/Player-run-left-Sheet.png");
		InputStream is4 = getClass().getResourceAsStream("/Player-Sheet-Left.png");

		if (is == null) {
			System.out.println("Could not find image");
		}
		try {
			img = ImageIO.read(is);
			img2 = ImageIO.read(is2);
			img3 = ImageIO.read(is3);
			img4 = ImageIO.read(is4);
			backgroundImgDay = ImageIO.read(bgStreamDay);
			backgroundImgNight = ImageIO.read(bgStreamNight);
			platform1Img = ImageIO.read(platform1Stream);
			KillBox = ImageIO.read(KillBoxStream);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Sets the preferred panel size for the game window.
	 */
	private void setPanelSize() {
		Dimension size = new Dimension(1280, 720);
		setPreferredSize(size);

	}

	public void setDirection(int direction) {
		this.playerDir = direction;
	}

	public void setMoving(boolean moving) {
		this.moving = moving;
	}

	/**
	 * Sets the animation array and action type based on movement and player state.
	 */
	private void setAnimation() {

		if (inAir) {
			// Freeze on first idle frame during jump
			Anim = (playerDir == LEFT) ? LidleAni : idleAni;
			aniIndex = 0; // Always use the first frame
			System.out.println("In Air - Using idle animation with " + Anim.length + " frames.");
			return;
		}
		if (moving) {
			playerAction = RUNNING;
			if (playerDir == LEFT) {
				Anim = LrunAni;
			} else {
				Anim = runAni;
			}

		} else {
			playerAction = IDLE;
			if (playerDir == LEFT) {
				Anim = LidleAni;
			} else {
				Anim = idleAni;
			}

		}
	}

	/**
	 * Updates the player's position based on movement direction, gravity, and
	 * collisions. Also handles coin collection, death conditions, and win state.
	 */

	private void updatePos() {
		float moveSpeed = 4f;

		// Horizontal movement (always allowed)
		if (moving) {
			switch (playerDir) {
			case LEFT:
				dx -= moveSpeed;
				break;
			case RIGHT:
				dx += moveSpeed;
				break;
			}
		}

		// Apply gravity
		if (!onGround) {
			velocityY += gravity;
			if (velocityY > maxFallSpeed)
				velocityY = maxFallSpeed;
		}

		// Predict vertical movement
		float nextY = dy + velocityY;

		// Check vertical collision
		Rectangle futureHitbox = new Rectangle((int) dx, (int) nextY, playerEntity.gethitbox().width,
				playerEntity.gethitbox().height);

		boolean collided = false;

		for (Rectangle platform : platforms) {
			if (futureHitbox.intersects(platform)) {
				if (velocityY > 0) {
					// Falling - land on platform
					dy = platform.y - playerEntity.gethitbox().height;
					onGround = true;
					inAir = false;
				} else if (velocityY < 0) {
					// Jumping up - hit head on bottom of platform
					dy = platform.y + platform.height;
				}

				velocityY = 0;
				collided = true;
				break;
			}
		}

		if (!collided) {
			onGround = false;
			dy += velocityY;
		}

		dy += velocityY; // or inside the "if (!collided)" block
		playerEntity.setPos(dx, dy);
		playerEntity.updateHitbox();

		// Check if standing on ground using feet collider
		onGround = isOnGround();

		System.out.println("[updatePos()] onGround: " + onGround + ", wantsToJump: " + wantsToJump);

		if (wantsToJump && onGround) {
			System.out.println("[updatePos()] Jump executed");
			velocityY = -8f;
			onGround = false;
			inAir = true;
			wantsToJump = false;
		} else {
			wantsToJump = false;
		}

		coins.removeIf(coin -> {
			if (playerEntity.gethitbox().intersects(coin)) {
				coinsCollected++;
				return true;
			}
			return false;
		});

		if (coins.isEmpty() && !gameWon) {
			gameWon = true;
			System.out.println("You win!");

		}
		if (!gameOver && dy > 720) {
			gameOver = true;
			System.out.println("You died!");

			// Start timer to close app after 3 seconds
			new javax.swing.Timer(2000, e -> {
				System.exit(0);
			}).start();
		}
		for (Rectangle obstacle : deadlyObstacles) {
			if (playerEntity.gethitbox().intersects(obstacle)) {
				if (!gameOver) {
					gameOver = true;
					System.out.println("You touched a deadly obstacle. You died!");
					new javax.swing.Timer(1500, e -> System.exit(0)).start();
				}
			}
		}

	}

	/**
	 * Checks if the player would collide with any platform at a given position.
	 *
	 * @param newX   New X-coordinate
	 * @param newY   New Y-coordinate
	 * @param hitbox The player's current hitbox
	 * @return true if collision would occur, false otherwise
	 */

	public boolean isColliding(float newX, float newY, Rectangle hitbox) {
		Rectangle moved = new Rectangle((int) newX, (int) newY, hitbox.width, hitbox.height);
		for (Rectangle platform : platforms) {
			if (moved.intersects(platform)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if the player is currently standing on a platform using a small hitbox
	 * below the feet.
	 *
	 * @return true if the player is on the ground
	 */

	private boolean isOnGround() {
		Rectangle playerHitbox = playerEntity.gethitbox();
		Rectangle feet = new Rectangle(playerHitbox.x, playerHitbox.y + playerHitbox.height, playerHitbox.width, 2); // 2px
																														// under
																														// feet

		for (Rectangle platform : platforms) {
			if (feet.intersects(platform)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Calculates the corrected Y-position for the player when colliding with a
	 * platform.
	 *
	 * @return The corrected Y-coordinate to place the player on top of a platform
	 */

	public float getCorrectedY() {
		Rectangle playerHitbox = playerEntity.gethitbox();
		for (Rectangle platform : platforms) {
			Rectangle test = new Rectangle((int) dx, (int) (dy + velocityY), playerHitbox.width, playerHitbox.height);
			if (test.intersects(platform)) {
				return platform.y - playerHitbox.height;
			}
		}
		return dy; // fallback
	}

	/**
	 * Paints the current game state, including background, platforms, coins, and
	 * the player.
	 *
	 * @param g The Graphics object to draw to
	 */
	public void paintComponent(Graphics g) {
		// Clear Previous Frame
		super.paintComponent(g);

		// Draw Background
		if (coinsCollected > 1) {
			g.drawImage(backgroundImgNight, 0, 0, getWidth(), getHeight(), null);
		} else {
			g.drawImage(backgroundImgDay, 0, 0, getWidth(), getHeight(), null);
		}

		// Set Running Animation depending on direction
		if (playerAction == RUNNING) {
			if (playerDir == LEFT) {
				Anim = LrunAni;
			} else {
				Anim = runAni;
			}
		} else {
			if (playerAction == IDLE) {
				if (playerDir == LEFT) {
					Anim = LidleAni;
				} else {
					Anim = idleAni;
				}

			}
		}

		// Draw game elements
		setAnimation();
		updatePos();
		playerEntity.setPos(dx, dy);
		playerEntity.updateHitbox();
		updateAnimationTick();

		// Draw Animation
		g.drawImage(Anim[aniIndex], (int) dx, (int) dy, 32, 64, null);
		if (platform1Img != null) {
			for (Rectangle platform : platforms) {
				g.drawImage(platform1Img, platform.x, platform.y, platform.width, platform.height, null);
			}
		}
		g.setColor(Color.RED);
		for (Rectangle obstacle : deadlyObstacles) {
			g.drawImage(KillBox, obstacle.x, obstacle.y, obstacle.width, obstacle.height, null);
		}
		// Used for Debugging
		// playerEntity.drawHitbox(g);

		// Draw coins (only if game not won)
		if (!gameWon) {
			for (Rectangle coin : coins) {
				g.setColor(Color.YELLOW);
				g.fillOval(coin.x, coin.y, coin.width, coin.height);
			}
		}

		// Check if the Game has been Won or Lost
		if (gameWon) {
			g.setColor(Color.GREEN);
			g.drawString("You collected all the coins! You win!", 500, 100);
		}
		if (gameOver) {

			g.setColor(Color.RED);
			g.drawString("You Died! Exiting", 500, 300);
		}

	}

	/**
	 * Requests a jump action from the player. The actual jump occurs in the next
	 * update cycle if the player is on the ground.
	 */
	public void jump() {
		wantsToJump = true;
		System.out.println("[jump()] Jump requested");
	}

	/**
	 * Updates the animation frame index based on time ticks. Freezes animation
	 * while in air.
	 */
	private void updateAnimationTick() {
		// TODO Auto-generated method stub
		if (inAir) {
			// Freeze animation index on 0 when in air
			aniIndex = 0;
			aniTick = 0;
			return;
		}

		if (Anim.length <= 1) {
			aniIndex = 0;
			return;
		}
		aniTick++;
		if (aniTick >= aniSpeed) {
			aniTick = 0;
			aniIndex++;
			if (aniIndex >= Anim.length) {
				aniIndex = 0;
			}
		}
	}

}

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
import key_inputs.MouseInputs;

import static utils.Constants.PlayerConstants.*;
import static utils.Constants.Direction.*;
import sprites.Entity;


public class GamePanel extends JPanel {
	
	private MouseInputs mouseInputs;
	private float dx = 100, dy = 100;
	private int frames = 0;
	private long lastCheck;
	private BufferedImage img;
	private BufferedImage img2;
	private BufferedImage img3;
	private BufferedImage img4;
	private BufferedImage[] idleAni;
	private BufferedImage[] LidleAni;
	private BufferedImage[] runAni;
	private BufferedImage[] LrunAni;
	private BufferedImage[] Anim;
	private int aniTick, aniIndex, aniSpeed = 30;
	private int playerAction = IDLE;
	private int playerDir = -1;
	private boolean moving = false;
	private Entity playerEntity;
	private int[][] lvlData;
	private List<Rectangle> platforms = new ArrayList<>();
	private Rectangle testObstacle = new Rectangle(50, 200, 500, 50);
	private float velocityY = 0;
	private final float gravity = 0.4f;
	private final float maxFallSpeed = 10f;
	private boolean onGround = false;
	private boolean inAir = false;
	private boolean wantsToJump = false;
	private List<Rectangle> coins = new ArrayList<>();
	private int coinsCollected = 0;
	private boolean gameWon = false;
	private boolean gameOver = false;
	private List<Rectangle> deadlyObstacles = new ArrayList<>();

	

	
	
	public GamePanel() {
		
		
		platforms.add(new Rectangle(50, 200, 500, 50));   // Platform 1
		platforms.add(new Rectangle(300, 400, 300, 40));  // Platform 2
		platforms.add(new Rectangle(700, 300, 200, 30));  // Platform 3
		coins.add(new Rectangle(150, 150, 20, 20)); // Coin 1
		coins.add(new Rectangle(400, 180, 20, 20)); // Coin 2
		deadlyObstacles.add(new Rectangle(600, 500, 100, 20)); // Trap 1
		deadlyObstacles.add(new Rectangle(600, 250, 100, 20)); // Trap 2

		mouseInputs = new MouseInputs();
		
		importImg();
		loadAnimations();
		
		addKeyListener(new KeyboardInputs(this));
		setFocusable(true);
		requestFocusInWindow();
		setPanelSize();
		addMouseListener(null);
		addMouseMotionListener(mouseInputs);
		playerEntity = new Entity(dx, dy, 32, 64);  // Width and height can match sprite
		
	}
	
	private void loadAnimations() {
		// TODO Auto-generated method stub
		idleAni = new BufferedImage[1];
		runAni = new BufferedImage[3];
		LrunAni = new BufferedImage[3];
		LidleAni = new BufferedImage[1];
		
		 for (int i = 0; i < idleAni.length; i++) {
		        idleAni[i] = img.getSubimage(i * 16, 0, 16, 32);
		    }

		    for (int i2 = 0; i2 < runAni.length; i2++) {
		        runAni[i2] = img2.getSubimage(i2 * 16, 0, 16, 32);
		    }

		    for (int i3 = 0; i3 < LrunAni.length; i3++) {
		        LrunAni[i3] = img3.getSubimage(i3 * 16, 0, 16, 32);
		    }

		    for (int i4 = 0; i4 < LidleAni.length; i4++) {
		        LidleAni[i4] = img4.getSubimage(i4 * 16, 0, 16, 32);
		    }
		}
	 

	private void importImg() {
		
		
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
	
	
	private void setAnimation() {
		if(moving) {
			playerAction = RUNNING;
			if(playerDir == LEFT) {
				Anim = LrunAni;
			} else {
				Anim = runAni;
			}
			
		} else {
			playerAction = IDLE;
			if(playerDir == LEFT) {
				Anim = LidleAni;
			} else {
				Anim = idleAni;
			}
			
		}
	}
	
	
	private void updatePos() {
	    float moveSpeed = 4f;

	    // Horizontal movement (always allowed)
	    if (moving) {
	        switch (playerDir) {
	            case LEFT: dx -= moveSpeed; break;
	            case RIGHT: dx += moveSpeed; break;
	        }
	    }

	    // Apply gravity
	    if (!onGround) {
	        velocityY += gravity;
	        if (velocityY > maxFallSpeed) velocityY = maxFallSpeed;
	    }

	    // Predict vertical movement
	    float nextY = dy + velocityY;

	    // Check vertical collision
	    Rectangle futureHitbox = new Rectangle((int) dx, (int) nextY, playerEntity.gethitbox().width, playerEntity.gethitbox().height);

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
	      
	        
	
	private Rectangle floor = new Rectangle(50, 200, 500, 50); // Example ground

	
	public boolean isColliding(float newX, float newY, Rectangle hitbox) {
	    Rectangle moved = new Rectangle((int) newX, (int) newY, hitbox.width, hitbox.height);
	    for (Rectangle platform : platforms) {
	        if (moved.intersects(platform)) {
	            return true;
	        }
	    }
	    return false;
	}
	
	private boolean checkCollision(Rectangle a, Rectangle b) {
	    return a.intersects(b);
	    
	}
	
	
	private boolean isOnGround() {
	    Rectangle playerHitbox = playerEntity.gethitbox();
	    Rectangle feet = new Rectangle(playerHitbox.x, playerHitbox.y + playerHitbox.height, playerHitbox.width, 2); // 2px under feet

	    for (Rectangle platform : platforms) {
	        if (feet.intersects(platform)) {
	            return true;
	        }
	    }

	    return false;
	}

	
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
	

	
	public void LoadlvlData(int[][] lvlData) {
		this.lvlData = lvlData; 
	}
	
	public void paintComponent(Graphics g) {
		
		if(playerAction == RUNNING) {
			if(playerDir == LEFT) {
				Anim = LrunAni;
			} else {
				Anim = runAni;
			}
		} else {
			if(playerAction == IDLE) {
				if(playerDir == LEFT) {
					Anim = LidleAni;
				} else {
					Anim = idleAni;
				}
				
			}
		}
		
		
		setAnimation();
		updatePos();
		playerEntity.setPos(dx, dy);
		playerEntity.updateHitbox();
		
		super.paintComponent(g);
		
		updateAnimationTick();
		
		
		// subImg = img.getSubimage(1*16, 0*32, 16, 32);
		g.drawImage(Anim[aniIndex], (int)dx, (int)dy, 32, 64, null);
		for (Rectangle platform : platforms) {
		    g.fillRect(platform.x, platform.y, platform.width, platform.height);
		   
		}
		g.setColor(Color.RED);
		for (Rectangle obstacle : deadlyObstacles) {
		    g.fillRect(obstacle.x, obstacle.y, obstacle.width, obstacle.height);
		}
		playerEntity.drawHitbox(g);
		
		// Draw coins (only if game not won)
		if (!gameWon) {
		    g.setColor(Color.YELLOW);
		    for (Rectangle coin : coins) {
		        g.fillOval(coin.x, coin.y, coin.width, coin.height);
		    }
		}

		// Always check for win message
		if (gameWon) {
		    g.setColor(Color.GREEN);
		    g.drawString("You collected all the coins! You win!", 500, 100);
		}
		if (gameOver) {
		    g.setColor(Color.RED);
		    g.drawString("You Died! Exiting", 500, 100);
		}
		    
		    }
		
		
		
	
	
	public void jump() {
		  wantsToJump = true;
		  System.out.println("[jump()] Jump requested");
	    }
	

	private void updateAnimationTick() {
		// TODO Auto-generated method stub
		if (Anim.length <= 1) {
	        aniIndex = 0;
	        return;
	    }
		aniTick++;
		if(aniTick >= aniSpeed) {
			aniTick = 0;
			aniIndex++;
			if(aniIndex >= Anim.length) {
				aniIndex = 0;
			}
		}
	}
	

		
}

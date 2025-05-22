package main;

import java.awt.Dimension;
import java.awt.Graphics;
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

public class GamePanel extends JPanel {
	
	private MouseInputs mouseInputs;
	private float dx = 100, dy = 100;
	private int frames = 0;
	private long lastCheck;
	private BufferedImage img;
	private BufferedImage img2;
	private BufferedImage[] idleAni;
	private BufferedImage[] runAni;
	private BufferedImage[] Anim;
	private int aniTick, aniIndex, aniSpeed = 45;
	private int playerAction = IDLE;
	public String imageName = "/Player-run-Sheet.png";
	private int playerDir = -1;
	private boolean moving = false;
	
	
	public GamePanel() {
		
		mouseInputs = new MouseInputs();
		
		importImg();
		loadAnimations();
		
		addKeyListener(new KeyboardInputs(this));
		setPanelSize();
		addMouseListener(null);
		addMouseMotionListener(mouseInputs);
		
	}
	
	private void loadAnimations() {
		// TODO Auto-generated method stub
		idleAni = new BufferedImage[1];
		runAni = new BufferedImage[4];
		
		for(int i = 0; i < idleAni.length; i++) {
			idleAni[i] = img.getSubimage(i*16, 0, 16, 32);
		}
	}

	private void importImg() {
		
		
		InputStream is = getClass().getResourceAsStream("/Player-run-Sheet.png");
		InputStream is2 = getClass().getResourceAsStream("/Player-Sheet.png");
		if (is == null) {
			System.out.println("Could not find image");
			}
		try {
			img = ImageIO.read(is);
			
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
		} else {
			playerAction = IDLE;
		}
	}
	
	
	private void updatePos() {
		if(moving) {
			switch(playerDir) {
			case LEFT:
				dx -= 2;
				break;
			case RIGHT:
				dx += 2;
				break;
			case UP:
				dy -= 2;
				break;
			case DOWN :
				dy += 2;
				break;
			}
		}
	}
	
	public void paintComponent(Graphics g) {
		
		if(playerAction == RUNNING) {
			Anim = runAni;
		} else {
			if(playerAction == IDLE) {
				Anim = idleAni;
			}
		}
		
		
		setAnimation();
		updatePos();
		
		super.paintComponent(g);
		
		updateAnimationTick();
		
		
		// subImg = img.getSubimage(1*16, 0*32, 16, 32);
		g.drawImage(Anim[aniIndex], (int)dx, (int)dy, 32, 64, null);
		
		
		
	}

	private void updateAnimationTick() {
		// TODO Auto-generated method stub
		aniTick++;
		if(aniTick >= aniSpeed) {
			aniTick = 0;
			aniIndex++;
			if(aniIndex >= idleAni.length) {
				aniIndex = 0;
			}
		}
	}
	

		
}

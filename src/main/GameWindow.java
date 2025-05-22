package main;

import javax.swing.JFrame;

public class GameWindow{
	private JFrame jframe;
	
	public GameWindow(GamePanel gamePanel) {
		jframe = new JFrame();
		
		jframe.setSize(1280, 720);
		jframe.setVisible(true);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.add(gamePanel);
		jframe.setLocationRelativeTo(null);
		jframe.pack();
		jframe.setResizable(false);
	}
	
	
	
}

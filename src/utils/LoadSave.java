package utils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class LoadSave {

	public static BufferedImage GetPlayerAtlas() {
		BufferedImage img = null;
		InputStream is = LoadSave.class.getResourceAsStream("/Player-run-Sheet.png");
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
		return img;
	}

}

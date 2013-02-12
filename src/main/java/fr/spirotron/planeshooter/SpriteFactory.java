package fr.spirotron.planeshooter;

import java.awt.image.BufferedImage;
import java.io.IOException;

import fr.spirotron.planeshooter.utils.ResourceHelper;

public class SpriteFactory {
	public enum Sprite {
		PLAYER1_1 (MAIN_SHEET, 4, 400, 65, 65),
		PLAYER1_SHOT (MAIN_SHEET, 4, 169, 32, 32),
		
		PLAYER2_1 (MAIN_SHEET, 268, 400, 65, 65),
		PLAYER2_SHOT (MAIN_SHEET, 37, 169, 32, 32);
		
		public final BufferedImage image;
		private Sprite(BufferedImage sheet, int x, int y, int width, int height) {
			image = sheet.getSubimage(x, y, width, height);
		}
	}
	
	private static final BufferedImage MAIN_SHEET = loadSheet("1945");
	
	private static BufferedImage loadSheet(String name) {
		String sheetPath = String.format("/spritesheets/%s.png", name);
		
		try {
			return ResourceHelper.loadImage(sheetPath);
		} catch (IOException e) {
			throw new Error("Sprite Sheet resource not found: "+sheetPath);
		}
	}
}

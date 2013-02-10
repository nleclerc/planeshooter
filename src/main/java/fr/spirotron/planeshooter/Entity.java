package fr.spirotron.planeshooter;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

public class Entity {
	private String name;
	private BufferedImage sprite;
	private Point position;
	private Dimension dimension;
	
	public Entity(String name, BufferedImage spriteSheet, int sheetX, int sheetY, int width, int height) {
		this.name = name;
		sprite = spriteSheet.getSubimage(sheetX, sheetY, width, height);
		dimension = new Dimension(width, height);
		position = new Point();
	}
	
	public Point getPosition() {
		return position;
	}
	
	public void setPosition(int x, int y) {
		position.x = x;
		position.y = y;
	}
	
	public Dimension getDimension() {
		return dimension;
	}
	
	public void draw (Graphics2D gfx) {
		gfx.drawImage(sprite, null, position.x, position.y);
	}
	
	public String getName() {
		return name;
	}
	
	public int getLeftBound() {
		return position.x;
	}
	
	public int getRightBound() {
		return position.x+dimension.width;
	}
	
	public int getTopBound() {
		return position.y;
	}
	
	public int getBottomBount() {
		return position.y+dimension.height;
	}
}

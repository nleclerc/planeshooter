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
	private Bounds bounds;
	
	public Entity() {
		name = "UNINITIALIZED_ENTITTY";
		dimension = new Dimension();
		position = new Point();
		bounds = new Bounds();
	}
	
	public void init(String name, BufferedImage spriteSheet, int sheetX, int sheetY, int width, int height) {
		this.name = name;
		sprite = spriteSheet.getSubimage(sheetX, sheetY, width, height);
		
		dimension.height = height;
		dimension.width = width;
		
		setPosition(0, 0);
	}
	
	public Point getPosition() {
		return position;
	}
	
	public void changePositionX(int delta) {
		setPosition(position.x+delta, position.y);
	}
	
	public void changePositionY(int delta) {
		setPosition(position.x, position.y+delta);
	}
	
	public void setPosition(int x, int y) {
		position.x = x;
		position.y = y;
		
		// Tedious calculations required for consistency in case one of the dimensions is even.
		
		int topHalf = dimension.height / 2;
		int bottomHalf = dimension.height - topHalf;
		
		int leftHalf = dimension.width / 2;
		int rightHalf = dimension.width - leftHalf;
		
		bounds.top = y - topHalf;
		bounds.bottom = y + bottomHalf;
		bounds.left = x - leftHalf;
		bounds.right = x + rightHalf;
	}
	
	public Dimension getDimension() {
		return dimension;
	}
	
	public void draw (Graphics2D gfx) {
		gfx.drawImage(sprite, null, bounds.left, bounds.top);
	}
	
	public String getName() {
		return name;
	}
	
	public Bounds getBounds() {
		return bounds;
	}
}

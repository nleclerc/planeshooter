package fr.spirotron.planeshooter.entities;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

import fr.spirotron.planeshooter.Bounds;

public class Entity {
	private int id;
	private BufferedImage sprite;
	private boolean dead;
	
	private final Point position = new Point();;
	private final Dimension dimension = new Dimension();;
	private final Bounds bounds = new Bounds();;
	
	public Entity(int id) {
		this.id = id;
		reset();
	}
	
	public void init(BufferedImage sprite) {
		this.sprite = sprite;
		dead = false;
		
		dimension.height = sprite.getHeight();
		dimension.width = sprite.getWidth();
		
		setPosition(0, 0);
	}
	
	public int getId() {
		return id;
	}
	
	public boolean isDead() {
		return dead;
	}
	
	public void kill() {
		dead = true;
	}
	
	public void reset() {
		sprite = null;
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
	
	public boolean isOnScreen(Dimension screenDimension) {
		return bounds.left < screenDimension.width &&
				bounds.right >= 0 &&
				bounds.top < screenDimension.height &&
				bounds.bottom >= 0;
	}
	
	public Dimension getDimension() {
		return dimension;
	}
	
	public void draw (Graphics2D gfx) {
		gfx.drawImage(sprite, null, bounds.left, bounds.top);
	}
	
	public Bounds getBounds() {
		return bounds;
	}
}

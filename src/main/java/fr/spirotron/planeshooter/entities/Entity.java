package fr.spirotron.planeshooter.entities;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

import fr.spirotron.planeshooter.SpriteFactory.Sprite;
import fr.spirotron.planeshooter.utils.Bounds;

public class Entity {
	private static final int STATE_COUNT = 20;
	
	private int id;
	private BufferedImage image;
	private boolean dead;
	
	private final Point position = new Point();;
	private final Dimension dimension = new Dimension();;
	private final Bounds bounds = new Bounds();
	public final int[] states = new int[STATE_COUNT];
	
	public Entity(int id) {
		this.id = id;
	}
	
	public void init(Sprite sprite) {
		image = sprite.image;
		dead = false;
		
		for (int i=states.length-1; i>=0; i--)
			i = 0;
		
		dimension.height = image.getHeight();
		dimension.width = image.getWidth();
		
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
		gfx.drawImage(image, null, bounds.left, bounds.top);
	}
	
	public Bounds getBounds() {
		return bounds;
	}
}

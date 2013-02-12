package fr.spirotron.planeshooter.entities;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Arrays;

import fr.spirotron.planeshooter.SpriteFactory.Sprite;
import fr.spirotron.planeshooter.entities.EntityFactory.EntityType;
import fr.spirotron.planeshooter.utils.Bounds;

public class Entity {
	private static final int STATE_COUNT = 20;
	
	private int id;
	private boolean dead;
	
	private EntityType type;
	private EntityManager manager;
	
	private final Point position = new Point();;
	private final Dimension dimension = new Dimension();;
	private final Bounds bounds = new Bounds();
	
	public final int[] states = new int[STATE_COUNT];
	
	public int animationState;
	public Sprite sprite;
	
	public Entity(int id) {
		this.id = id;
	}
	
	public void init(EntityType type, EntityManager manager) {
		this.type = type;
		this.manager = manager;
		dead = false;
		
		Arrays.fill(states, 0);
		animationState = 0;
		
		dimension.height = type.animation[0].image.getHeight();
		dimension.width = type.animation[0].image.getWidth();
		
		manager.initialize(this);
	}
	
	public void update() {
		manager.update(this);
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
	
	public EntityType getType() {
		return type;
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
		gfx.drawImage(sprite.image, null, bounds.left, bounds.top);
	}
	
	public Bounds getBounds() {
		return bounds;
	}
}

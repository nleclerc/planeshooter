package fr.spirotron.planeshooter;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fr.spirotron.planeshooter.SpriteFactory.Sprite;
import fr.spirotron.planeshooter.entities.Entity;
import fr.spirotron.planeshooter.entities.EntityFactory;
import fr.spirotron.planeshooter.entities.PlayerShotEntityManager;
import fr.spirotron.planeshooter.entities.UserEntityManager;
import fr.spirotron.planeshooter.utils.Bounds;


public class Engine implements Runnable {
	private static final Color BACKGROUND_COLOR = new Color(0x024994);
	private static final int DISPLAY_BUFFER_COUNT = 2;
	private static final int TICK = 10;
	
	private Canvas canvas;
	private BufferStrategy bufferStrategy;
	
	private Entity playerEntity;
	private List<Entity> playerShots;
	
	private Dimension screenDimension;
	
	private boolean running;
	
	private UserEntityManager userManager;
	private PlayerShotEntityManager playerShotManager;
	private EntityFactory entityFactory;
	
	public Canvas createCanvas(Dimension screenDimension) {
		System.out.println("Creating canvas...");
		this.screenDimension = screenDimension;
		
		canvas = new Canvas();
		canvas.setSize(screenDimension);
		
		userManager = new UserEntityManager(canvas);
		playerShotManager = new PlayerShotEntityManager(screenDimension);
		
		return canvas;
	}
	
	public void init() throws Exception {
		System.out.println("Initializing engine...");
		canvas.createBufferStrategy(DISPLAY_BUFFER_COUNT);
		bufferStrategy = canvas.getBufferStrategy();
		
		entityFactory = new EntityFactory();
		
		playerEntity = entityFactory.activateEntity(Sprite.PLAYER1_1);
		playerEntity.setPosition(300, 200);
		
		playerShots = new ArrayList<Entity>();

		fillBackground();

		start();
	}

	private void fillBackground() {
		Graphics2D gfx = (Graphics2D)bufferStrategy.getDrawGraphics();
		
		gfx.setColor(BACKGROUND_COLOR);
		gfx.fillRect(0, 0, screenDimension.width, screenDimension.height);
		
		gfx.dispose();
		bufferStrategy.show();
		Toolkit.getDefaultToolkit().sync();
	}
	
	private void eraseEntities(Graphics2D gfx) {
		for (Iterator<Entity> it=entityFactory.getActivatedEntities(); it.hasNext(); ) {
			Entity entityToErase = it.next();
			Dimension dim = entityToErase.getDimension();
			Bounds bounds = entityToErase.getBounds();
			
			gfx.setColor(BACKGROUND_COLOR);
			gfx.fillRect(bounds.left, bounds.top, dim.width, dim.height);
		}
	}
	
	private long update() {
		long startTime = System.currentTimeMillis();
		Graphics2D gfx = (Graphics2D)bufferStrategy.getDrawGraphics();
		
		eraseEntities(gfx);

		userManager.update(playerEntity);
		
		playerEntity.draw(gfx);
		
		for (Entity shot: playerShots) {
			playerShotManager.update(shot);
			
			if (!shot.isDead())
				shot.draw(gfx);
		}
		
		for (int i=playerShots.size()-1; i>=0; i--) {
			Entity shot = playerShots.get(i);
			
			if (shot.isDead()) {
				playerShots.remove(i);
				entityFactory.deactivate(shot.getId());
			}
		}
		
		if (userManager.isFiring())
			createShot(gfx);
		
		/*
		for (Iterator<Entity> it=entityFactory.getActivatedEntities(); it.hasNext(); ) {
			Entity e = it.next();
			
			if (e.isDead())
				
		}
		*/
		
		gfx.dispose();
		bufferStrategy.show();
		Toolkit.getDefaultToolkit().sync();
		
		long totalTime = System.currentTimeMillis() - startTime;
		System.out.println("Update done in "+totalTime+" ms.");
		
		return totalTime;
	}
	
	private void createShot(Graphics2D gfx) {
		Entity newShot = entityFactory.activateEntity(Sprite.PLAYER1_SHOT);
		playerShots.add(newShot);
		
		Point playerPosition = playerEntity.getPosition();
		
		newShot.setPosition(playerPosition.x, playerPosition.y-30);
		newShot.draw(gfx);
	}

	private void start() {
		new Thread(this, "gamethread").start();
	}
	
	public void stop() {
		running = false;
	}
	
	public void run() {
		running = true;
		
		while (running) {
			long updateTime = update();
			
			if (updateTime < TICK) {
				try {
					Thread.sleep(TICK-updateTime);
				} catch (InterruptedException e) {
					throw new Error(e);
				}
			}
		}
	}
}

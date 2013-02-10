package fr.spirotron.planeshooter;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;


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
	
	private UserControlMovementHandler userMovementHandler;
	private PlayerShotMovementHandler playerShotMovementHandler;
	private EntityFactory entityFactory;
	
	public Canvas createCanvas(Dimension screenDimension) {
		System.out.println("Creating canvas...");
		this.screenDimension = screenDimension;
		
		canvas = new Canvas();
		canvas.setSize(screenDimension);
		
		userMovementHandler = new UserControlMovementHandler(canvas);
		playerShotMovementHandler = new PlayerShotMovementHandler(screenDimension);
		
		return canvas;
	}
	
	public void init() throws Exception {
		System.out.println("Initializing engine...");
		canvas.createBufferStrategy(DISPLAY_BUFFER_COUNT);
		bufferStrategy = canvas.getBufferStrategy();
		
		entityFactory = new EntityFactory();
		entityFactory.init("/spriteSheetList.txt");
		
		playerEntity = entityFactory.getEntity("player1", "1945#player1");
		playerEntity.setPosition(300, 200);
		
		playerShots = new ArrayList<Entity>();
		
		start();
	}
	
	private long update() {
		long startTime = System.currentTimeMillis();
		Graphics2D gfx = (Graphics2D)bufferStrategy.getDrawGraphics();
		
		gfx.setColor(BACKGROUND_COLOR);
		gfx.fillRect(0, 0, screenDimension.width, screenDimension.height);
		
		userMovementHandler.update(playerEntity);
		
		playerEntity.draw(gfx);
		
		for (Entity shot: playerShots) {
			playerShotMovementHandler.update(shot);
			shot.draw(gfx);
		}
		
		if (userMovementHandler.isFiring())
			createShot(gfx);
		
		gfx.dispose();
		bufferStrategy.show();
		Toolkit.getDefaultToolkit().sync();
		
		long totalTime = System.currentTimeMillis() - startTime;
		System.out.println("Update done in "+totalTime+" ms.");
		
		return totalTime;
	}
	
	private void createShot(Graphics2D gfx) {
		Entity newShot = entityFactory.getEntity("player1shot", "1945#player1shot");
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

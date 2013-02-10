package fr.spirotron.planeshooter;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;


public class Engine implements Runnable {
	private static final Color BACKGROUND_COLOR = new Color(0x024994);
	private static final int DISPLAY_BUFFER_COUNT = 2;
	private static final int TICK = 10;
	
	private Canvas canvas;
	private BufferStrategy bufferStrategy;
	private BufferedImage spriteSheet;
	
	private Entity playerEntity;
	
	private Dimension screenDimension;
	
	private boolean running;
	
	private UserControlMovementHandler userMovementHandler;
	
	public Canvas createCanvas(Dimension screenDimension) {
		System.out.println("Creating canvas...");
		this.screenDimension = screenDimension;
		
		canvas = new Canvas();
		canvas.setSize(screenDimension);
		
		userMovementHandler = new UserControlMovementHandler(canvas);
		
		return canvas;
	}
	
	public void init() throws Exception {
		System.out.println("Initializing engine...");
		canvas.createBufferStrategy(DISPLAY_BUFFER_COUNT);
		bufferStrategy = canvas.getBufferStrategy();
		
		spriteSheet = ImageIO.read(getClass().getResourceAsStream("/1945.png"));
		playerEntity = new Entity();
		playerEntity.init("player", spriteSheet, 4, 400, 65, 65);
		playerEntity.setPosition(300, 200);
		start();
	}
	
	private long update() {
		long startTime = System.currentTimeMillis();
		Graphics2D gfx = (Graphics2D)bufferStrategy.getDrawGraphics();
		
		gfx.setColor(BACKGROUND_COLOR);
		gfx.fillRect(0, 0, screenDimension.width, screenDimension.height);
		
		userMovementHandler.update(playerEntity);
		
		playerEntity.draw(gfx);
		
		gfx.dispose();
		bufferStrategy.show();
		Toolkit.getDefaultToolkit().sync();
		
		long totalTime = System.currentTimeMillis() - startTime;
		System.out.println("Update done in "+totalTime+" ms.");
		
		return totalTime;
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

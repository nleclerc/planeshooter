package fr.spirotron.planeshooter;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.TreeSet;

import javax.imageio.ImageIO;


public class Engine implements Runnable {
	private static final Color BACKGROUND_COLOR = new Color(0x024994);
	private static final int DISPLAY_BUFFER_COUNT = 2;
	private static final int TICK = 10;
	private static final int PLAYER_SPEED = 3;
	
	private Canvas canvas;
	private BufferStrategy bufferStrategy;
	private BufferedImage spriteSheet;
	
	private Entity playerEntity;
	
	private int screenWidth;
	private int screenHeight;
	
	private boolean running;
	
	private InputListener inputListener;
	
	private int controlX;
	private int controlY;
	@SuppressWarnings("unused")
	private boolean controlFiring;
	
	public Engine() {
		controlX = 0;
		controlY = 0;
		controlFiring = false;
	}
	
	public Canvas createCanvas(int width, int height) {
		System.out.println("Creating canvas...");
		this.screenWidth = width;
		this.screenHeight = height;
		
		canvas = new Canvas();
		canvas.setSize(width, height);
		
		inputListener = new InputListener();
		canvas.addKeyListener(inputListener);
		
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
		gfx.fillRect(0, 0, screenWidth, screenHeight);
		
		updatePLayerPosition();
		
		playerEntity.draw(gfx);
		
		gfx.dispose();
		bufferStrategy.show();
		Toolkit.getDefaultToolkit().sync();
		
		long totalTime = System.currentTimeMillis() - startTime;
		System.out.println("Update done in "+totalTime+" ms.");
		
		return totalTime;
	}
	
	private void updatePLayerPosition() {
		Point playerPosition = playerEntity.getPosition();
		Bounds playerBounds = playerEntity.getBounds();
		
		if (controlX < 0 && playerBounds.left > PLAYER_SPEED)
			playerEntity.changePositionX(-PLAYER_SPEED);
		else if (controlX > 0 && playerBounds.right < (screenWidth-PLAYER_SPEED))
			playerEntity.changePositionX(PLAYER_SPEED);
		
		if (controlY < 0 && playerBounds.top > PLAYER_SPEED)
			playerEntity.changePositionY(-PLAYER_SPEED);
		else if (controlY > 0 && playerBounds.bottom < (screenHeight-PLAYER_SPEED))
			playerEntity.changePositionY(PLAYER_SPEED);
		
		System.out.println("Control: x="+controlX+" y="+controlY);
		System.out.println("Player: x="+playerPosition.x+" y="+playerPosition.y);
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
	
	private class InputListener implements KeyListener {
		private TreeSet<Integer> pressBuffer;
		
		public InputListener() {
			pressBuffer = new TreeSet<Integer>();
		}
		
		public void keyPressed(KeyEvent e) {
			Integer keyCode = e.getKeyCode();
			
			if (!pressBuffer.contains(keyCode)) {
				pressBuffer.add(keyCode);
				
				switch (keyCode) {
					case KeyEvent.VK_UP:
						controlY--;
						break;
					case KeyEvent.VK_DOWN:
						controlY++;
						break;
					case KeyEvent.VK_LEFT:
						controlX--;
						break;
					case KeyEvent.VK_RIGHT:
						controlX++;
						break;
						
					case KeyEvent.VK_SPACE:
						controlFiring = true;
						break;
						
					default:
						break;
				}
			}
		}

		public void keyReleased(KeyEvent e) {
			Integer keyCode = e.getKeyCode();
			pressBuffer.remove(keyCode);
			
			switch (keyCode) {
				case KeyEvent.VK_UP:
					controlY++;
					break;
				case KeyEvent.VK_DOWN:
					controlY--;
					break;
				case KeyEvent.VK_LEFT:
					controlX++;
					break;
				case KeyEvent.VK_RIGHT:
					controlX--;
					break;
					
				case KeyEvent.VK_SPACE:
					controlFiring = false;
					break;
					
				default:
					break;
			}
		}
		
		public void keyTyped(KeyEvent e) {
		}
	}
}

package fr.spirotron.planeshooter;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
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
	private static final int PLAYER_SPEED = 5;
	
	private Canvas canvas;
	private BufferStrategy bufferStrategy;
	private BufferedImage spriteSheet;
	
	private Entity playerEntity;
	
	private int width;
	private int height;
	
	private boolean running;
	
	private InputListener inputListener;
	
	private int controlX;
	private int controlY;
	
	public Engine() {
		controlX = 0;
		controlY = 0;
	}
	
	public Canvas createCanvas(int width, int height) {
		System.out.println("Creating canvas...");
		this.width = width;
		this.height = height;
		
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
		playerEntity = new Entity("player", spriteSheet, 4, 400, 65, 65);
		playerEntity.setPosition(300, 200);
		start();
	}
	
	private void update() {
		long startTime = System.currentTimeMillis();
		Graphics2D gfx = (Graphics2D)bufferStrategy.getDrawGraphics();
		
		gfx.setColor(BACKGROUND_COLOR);
		gfx.fillRect(0, 0, width, height);
		
		updatePLayerPosition();
		
		playerEntity.draw(gfx);
		
		gfx.dispose();
		bufferStrategy.show();
		Toolkit.getDefaultToolkit().sync();
		
		long endTime = System.currentTimeMillis();
		System.out.println("Update done in "+(endTime-startTime)+" ms.");
	}
	
	private void updatePLayerPosition() {
		Point playerPosition = playerEntity.getPosition();
		Dimension playerDimension = playerEntity.getDimension();
		
		int leftBound = playerPosition.x;
		int rightBound = playerPosition.x+playerDimension.width;
		int topBound = playerPosition.y;
		int bottomBound = playerPosition.y+playerDimension.height;
		
		if (controlX < 0 && leftBound > PLAYER_SPEED)
			playerPosition.x -= PLAYER_SPEED;
		else if (controlX > 0 && rightBound < (width-PLAYER_SPEED))
			playerPosition.x += PLAYER_SPEED;
		
		if (controlY < 0 && topBound > PLAYER_SPEED)
			playerPosition.y -= PLAYER_SPEED;
		else if (controlY > 0 && bottomBound < (height-PLAYER_SPEED))
			playerPosition.y += PLAYER_SPEED;
		
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
			update();
			
			try {
				Thread.sleep(TICK);
			} catch (InterruptedException e) {
				throw new Error(e);
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
					
				default:
					break;
			}
		}
		
		public void keyTyped(KeyEvent e) {
		}
	}
}

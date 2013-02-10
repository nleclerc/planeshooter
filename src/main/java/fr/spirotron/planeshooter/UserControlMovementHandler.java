package fr.spirotron.planeshooter;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.TreeSet;

public class UserControlMovementHandler implements MovementHandler, KeyListener {
	private static final int PLAYER_SPEED = 3;
	private final TreeSet<Integer> pressBuffer;
	
	private int controlX;
	private int controlY;
	@SuppressWarnings("unused")
	private boolean controlFiring;
	
	private Dimension screenDimension;
	
	public UserControlMovementHandler (Component referenceComponent) {
		pressBuffer = new TreeSet<Integer>();
		screenDimension = referenceComponent.getSize();
		referenceComponent.addKeyListener(this);
	}
	
	@Override
	public void update(Entity entity) {
		Bounds playerBounds = entity.getBounds();
		
		if (controlX < 0 && playerBounds.left > PLAYER_SPEED)
			entity.changePositionX(-PLAYER_SPEED);
		else if (controlX > 0 && playerBounds.right < (screenDimension.width-PLAYER_SPEED))
			entity.changePositionX(PLAYER_SPEED);
		
		if (controlY < 0 && playerBounds.top > PLAYER_SPEED)
			entity.changePositionY(-PLAYER_SPEED);
		else if (controlY > 0 && playerBounds.bottom < (screenDimension.height-PLAYER_SPEED))
			entity.changePositionY(PLAYER_SPEED);
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
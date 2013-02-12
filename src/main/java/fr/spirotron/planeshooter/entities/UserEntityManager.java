package fr.spirotron.planeshooter.entities;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.TreeSet;

import fr.spirotron.planeshooter.utils.Bounds;

public class UserEntityManager implements EntityManager, KeyListener {
	public static final int STATE_SPAWN_ANIMATION = 0;
	public static final int STATE_FIRING = 1;
	
	private static final int PLAYER_SPEED = 3;
	
	private final TreeSet<Integer> pressBuffer;
	private final Dimension screenDimension;
	
	private int controlX;
	private int controlY;
	private boolean controlFiring;
	
	public UserEntityManager (Component referenceComponent) {
		pressBuffer = new TreeSet<Integer>();
		screenDimension = referenceComponent.getSize();
		referenceComponent.addKeyListener(this);
	}
	
	@Override
	public void update(Entity entity) {
		if (entity.states[STATE_SPAWN_ANIMATION] > 0) {
			// animate spawn.
			
			entity.changePositionY(-2);
			entity.states[STATE_SPAWN_ANIMATION]--;
		} else {
			updateFiringState(entity);
			updatePosition(entity);
		}
	}

	private void updateFiringState(Entity entity) {
		if (controlFiring) {
			entity.states[STATE_FIRING] = 1;
			controlFiring = false;
		}
	}

	private void updatePosition(Entity entity) {
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
				break;
				
			default:
				break;
		}
	}
	
	public void keyTyped(KeyEvent e) {
	}
	
	public static boolean isFiring(Entity playerEntity) {
		return playerEntity.states[STATE_FIRING] != 0;
	}
	
	public static void stopFiring(Entity playerEntity) {
		playerEntity.states[STATE_FIRING] = 0;
	}

	@Override
	public void initialize(Entity entity) {
		entity.setPosition(screenDimension.width/2, screenDimension.height);
		entity.states[STATE_SPAWN_ANIMATION] = 45;
	}
}

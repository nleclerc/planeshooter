package fr.spirotron.planeshooter.entities;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;

import fr.spirotron.planeshooter.utils.Bounds;

public class UserEntityManager implements EntityManager, KeyListener {
	// should be enough for most keyboards hopefully.
	private static final int PRESS_STATES_COUNT = 200;
	
	private static final int STATE_SPAWN_ANIMATION = 0;
	private static final int STATE_FIRING = 1;
	
	private static final int PLAYER_SPEED = 3;
	
	private final int[] pressStates;
	private final Dimension screenDimension;
	
	private int controlX;
	private int controlY;
	private boolean controlFiring;
	
	public UserEntityManager (Component referenceComponent) {
		pressStates = new int[PRESS_STATES_COUNT];
		Arrays.fill(pressStates, -1);
		
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
	
	private boolean isAlreadyPressed(int keyCode) {
		int lastEmptyIndex = -1;
		int foundIndex = -1;
		
		for (int i=0,count=pressStates.length; foundIndex < 0 && i < count; i++) {
			if (pressStates[i] < 0)
				lastEmptyIndex = i;
			else if (pressStates[i] == keyCode)
				foundIndex = i;
		}
		
		if (foundIndex >= 0 && lastEmptyIndex >= 0) {
			pressStates[lastEmptyIndex] = keyCode;
			pressStates[foundIndex] = -1;
		}
		
		return foundIndex >= 0;
	}
	
	private void clearPressed(int keyCode) {
		for (int i=0, count=pressStates.length; i < count; i++) {
			if (pressStates[i] == keyCode) {
				pressStates[i] = -1;
				return;
			}
		}
		
		throw new IllegalStateException("Key wasn't pressed: "+keyCode);
	}
	
	private void setPressed(int keyCode) {
		for (int i=0, count=pressStates.length; i < count; i++) {
			if (pressStates[i] == -1) {
				pressStates[i] = keyCode;
				return;
			}
		}
		
		throw new IllegalStateException("Too many keys pressed at the same time: more than "+PRESS_STATES_COUNT);
	}
	
	public void keyPressed(KeyEvent e) {
		Integer keyCode = e.getKeyCode();
		
		if (!isAlreadyPressed(keyCode)) {
			setPressed(keyCode);
			
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
		clearPressed(keyCode);
		
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
		switch (entity.getType()) {
			case PLAYER1:
				entity.setPosition(screenDimension.width/3, screenDimension.height+entity.getDimension().height);
				break;
	
			default:
				throw new IllegalArgumentException("Entity received is not a player: "+entity.getType());
		}
		
		entity.states[STATE_SPAWN_ANIMATION] = 80;
	}
}

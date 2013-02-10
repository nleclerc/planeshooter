package fr.spirotron.planeshooter;

import java.awt.Dimension;

public class PlayerShotMovementHandler implements MovementHandler {
	private static final int SHOT_SPEED = 8;
	
	private Dimension screenDimension;
	
	public PlayerShotMovementHandler(Dimension screenDimension) {
		this.screenDimension = screenDimension;
	}
	
	@Override
	public void update(Entity entity) {
		if (entity.isOnScreen(screenDimension))
			entity.changePositionY(-SHOT_SPEED);
	}
}

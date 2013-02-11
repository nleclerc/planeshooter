package fr.spirotron.planeshooter.entities;

import java.awt.Dimension;

public class PlayerShotEntityManager implements EntityManager {
	private static final int SHOT_SPEED = 8;
	
	private Dimension screenDimension;
	
	public PlayerShotEntityManager(Dimension screenDimension) {
		this.screenDimension = screenDimension;
	}
	
	@Override
	public void update(Entity entity) {
		if (entity.isOnScreen(screenDimension))
			entity.changePositionY(-SHOT_SPEED);
	}
}

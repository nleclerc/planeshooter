package fr.spirotron.planeshooter.entities;

import java.awt.Dimension;
import java.awt.Point;

public class PlayerShotEntityManager implements EntityManager {
	private static final int SHOT_SPEED = 8;
	
	private final Dimension screenDimension;
	private Entity playerEntity;
	
	public PlayerShotEntityManager(Dimension screenDimension) {
		this.screenDimension = screenDimension;
	}
	
	public void setPlayerEntity(Entity player) {
		playerEntity = player;
	}
	
	@Override
	public void initialize(Entity newShot) {
		Point playerPosition = playerEntity.getPosition();
		newShot.setPosition(playerPosition.x, playerPosition.y-20);
	}
	
	@Override
	public void update(Entity entity) {
		if (entity.isOnScreen(screenDimension))
			entity.changePositionY(-SHOT_SPEED);
		else
			entity.kill();
	}
}

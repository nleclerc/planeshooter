package fr.spirotron.planeshooter.entities;

import java.awt.Dimension;
import java.awt.Point;

import fr.spirotron.planeshooter.entities.EntityFactory.EntityType;

public class PlayerShotEntityManager implements EntityManager {
	private static final int SHOT_SPEED = 8;
	
	private final Dimension screenDimension;
	private Entity playerEntity;
	private EntityType shotType;
	
	public PlayerShotEntityManager(Dimension screenDimension, EntityType shotType) {
		this.screenDimension = screenDimension;
		this.shotType = shotType;
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
	
	public EntityType getShotType() {
		return shotType;
	}
}

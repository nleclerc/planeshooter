package fr.spirotron.planeshooter.entities;

import fr.spirotron.planeshooter.entities.EntityFactory.EntityType;

public class AnimationEntityManager implements EntityManager {
	@Override
	public void initialize(Entity entity) {
		EntityType type = entity.getType();
		
		if (type.frameDuration <= 0)
			entity.sprite = type.animation[0];
	}

	@Override
	public void update(Entity entity) {
		EntityType type = entity.getType();
		
		if (type.frameDuration > 0) {
			entity.sprite = type.animation[entity.animationState/type.frameDuration];
			entity.animationState ++;
			entity.animationState %= type.animation.length*type.frameDuration;
		}
	}
}

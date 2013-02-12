package fr.spirotron.planeshooter.entities;

import java.util.Iterator;

import fr.spirotron.planeshooter.SpriteFactory.Sprite;

public class EntityFactory {
	public enum EntityType {
		PLAYER1(
				anim(
						Sprite.PLAYER1_1,
						Sprite.PLAYER1_2,
						Sprite.PLAYER1_3
				),
				1
		),
		PLAYER1_SHOT(
				anim(
						Sprite.PLAYER1_SHOT
				),
				0
		);
		
		public final Sprite[] animation;
		public final int frameDuration;
		private EntityType(Sprite[] animation, int frameDuration) {
			this.animation = animation;
			this.frameDuration = frameDuration;
		}
	}
	
	private static final int POOL_SIZE = 50;
	
	private final Entity[] pool = new Entity[POOL_SIZE];
	private final boolean[] activated = new boolean[POOL_SIZE];
	
	private final ActivatedEntityIterator activatedIterator = new ActivatedEntityIterator();
	
	public EntityFactory() {
		for (int i=0; i<POOL_SIZE; i++)
			pool[i] = new Entity(i);
	}
	
	public Entity activateEntity(EntityType type) {
		Entity newEntity = lookupInPool();
		newEntity.init(type);
		return newEntity;
	}
	
	private Entity lookupInPool() {
		for (int i=0; i < POOL_SIZE; i++)
			if (!activated[i]) {
				activated[i] = true;
				return pool[i];
			}
				
		throw new IllegalStateException("No entity left in pool.");
	}

	public void deactivate(int entityId) {
		if (!activated[entityId])
			throw new IllegalStateException("Entity is not activated: "+entityId);
		
		activated[entityId] = false;
	}
	
	public Iterator<Entity> getActivatedEntities() {
		activatedIterator.reset();
		return activatedIterator;
	}
	
	private static Sprite[] anim(Sprite...sprites) {
		return sprites;
	}
	
	private class ActivatedEntityIterator implements Iterator<Entity> {
		private int index;
		
		private ActivatedEntityIterator() {
			reset();
		}
		
		private void reset() {
			index = 0;
		}
		
		@Override
		public boolean hasNext() {
			for (;index < POOL_SIZE && !activated[index]; index++) {/* Do nothing. */}
			
			return index < POOL_SIZE; // otherwise we reached the end of the activated pool.
		}

		@Override
		public Entity next() {
			// no check for proper state.
			// trust myself not to do anything weird...
			return pool[index++];
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}

package fr.spirotron.planeshooter.entities;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import fr.spirotron.planeshooter.utils.ResourceHelper;

public class EntityFactory {
	private static final String IMAGE_PATH_PATTERN = "/spritesheets/%s.png";
	private static final String PROPERTIES_PATH_PATTERN = "/spritesheets/%s.properties";
	private static final String PROPERTIES_SEPARATOR = "\\.";
	private static final String SPRITE_TYPE_SEPARATOR = "#";
	
	private static final int POOL_SIZE = 50;
	
	private final Map<String, SpriteSheet> loadedSheets = new TreeMap<String, EntityFactory.SpriteSheet>();
	private final Entity[] pool = new Entity[POOL_SIZE];
	private final boolean[] activated = new boolean[POOL_SIZE];
	
	private final ActivatedEntityIterator activatedIterator = new ActivatedEntityIterator();
	
	public void init(String spriteSheetListResourcePath) throws IOException {
		List<String> spriteSheets = ResourceHelper.loadTextFile("/spriteSheetList.txt");
		
		for (String sheetName: spriteSheets)
			loadedSheets.put(sheetName, new SpriteSheet(sheetName));
		
		for (int i=0; i<POOL_SIZE; i++)
			pool[i] = new Entity(i);
	}
	
	public Entity activateEntity(String type) {
		String[] spriteType = type.split(SPRITE_TYPE_SEPARATOR);
		String sheetName = spriteType[0];
		String spriteName = spriteType[1];
		
		if (!loadedSheets.containsKey(sheetName))
			throw new IllegalArgumentException("Unknown sprite sheet: "+sheetName);
		
		Entity newEntity = lookupInPool();
		newEntity.init(loadedSheets.get(sheetName).getSprite(spriteName));
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
		pool[entityId].reset();
	}
	
	public Iterator<Entity> getActivatedEntities() {
		activatedIterator.reset();
		return activatedIterator;
	}

	private class SpriteSheet {
		private BufferedImage sheet;
		private Map<String, BufferedImage> subimages;
		
		public SpriteSheet(String name) throws IOException {
			System.out.println("Initializing sprite sheet: "+name);
			
			sheet = ResourceHelper.loadImage(String.format(IMAGE_PATH_PATTERN, name));
			subimages = new TreeMap<String, BufferedImage>();
			
			TreeMap<String, String> rawProperties = ResourceHelper.loadProperties(String.format(PROPERTIES_PATH_PATTERN, name));
			Map<String, Map<String, String>> properties = parseProperties(rawProperties);
			
			for (Entry<String, Map<String, String>> spriteEntry: properties.entrySet())
				registerSprite(spriteEntry.getKey(), spriteEntry.getValue());
		}

		private void registerSprite(String name, Map<String, String> properties) {
			System.out.println("Registering sprite: "+name);
			subimages.put(name, sheet.getSubimage(
					Integer.valueOf(properties.get("x")),
					Integer.valueOf(properties.get("y")),
					Integer.valueOf(properties.get("width")),
					Integer.valueOf(properties.get("height"))
			));
		}
		
		private BufferedImage getSprite(String name) {
			if (!subimages.containsKey(name))
				throw new IllegalArgumentException("Undefined sprite requested: "+name);
			
			return subimages.get(name);
		}

		private Map<String, Map<String, String>> parseProperties(TreeMap<String, String> rawProperties) {
			Map<String, Map<String, String>> result = new HashMap<String, Map<String,String>>();
			
			for (Entry<String, String> entry: rawProperties.entrySet()) {
				System.out.println("Parsing properties key: "+entry.getKey());
				String[] keys = entry.getKey().split(PROPERTIES_SEPARATOR);
				
				if (!result.containsKey(keys[0]))
					result.put(keys[0], new HashMap<String, String>());
				
				result.get(keys[0]).put(keys[1], entry.getValue());
			}
			
			return result;
		}
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
			return pool[index++];
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}

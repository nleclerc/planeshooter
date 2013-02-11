package fr.spirotron.planeshooter.entities;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
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
	
	Map<String, SpriteSheet> loadedSheets;
	
	public void init(String spriteSheetListResourcePath) throws IOException {
		loadedSheets = new TreeMap<String, EntityFactory.SpriteSheet>();
		List<String> spriteSheets = ResourceHelper.loadTextFile("/spriteSheetList.txt");
		
		for (String sheetName: spriteSheets)
			loadedSheets.put(sheetName, new SpriteSheet(sheetName));
	}
	
	public Entity getEntity(String name, String type) {
		String[] spriteType = type.split(SPRITE_TYPE_SEPARATOR);
		String sheetName = spriteType[0];
		String spriteName = spriteType[1];
		
		if (!loadedSheets.containsKey(sheetName))
			throw new IllegalArgumentException("Unknown sprite sheet: "+sheetName);
		
		Entity newEntity = new Entity();
		newEntity.init(name, loadedSheets.get(sheetName).getSprite(spriteName));
		return newEntity;
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
}

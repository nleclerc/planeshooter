package fr.spirotron.planeshooter.utils;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.TreeMap;

import javax.imageio.ImageIO;

public class ResourceHelper {
	public static InputStream load(String path) {
		InputStream stream = ResourceHelper.class.getResourceAsStream(path);
		
		if (stream == null)
			throw new Error("Resource not found: "+path);
		
		return stream;
	}
	
	public static List<String> loadTextFile(String path) throws IOException {
		try {
			InputStream resourceStream = load(path);
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(resourceStream, "UTF-8"));
			List<String> result = new ArrayList<String>();
			
			String line;
			while ((line = reader.readLine()) != null)
				result.add(line);
			
			reader.close();
			
			return result;
		} catch (UnsupportedEncodingException e) {
			// Should not happen, but just in case.
			throw new Error(e);
		}
	}
	
	public static BufferedImage loadImage(String path) throws IOException {
		return ImageIO.read(load(path));
	}
	
	public static TreeMap<String, String> loadProperties(String path) throws IOException {
		Properties props = new Properties();
		props.load(load(path));
		
		// Converts Properties to Map for easier use down the line.
		TreeMap<String, String> result = new TreeMap<String, String>();
		
		for (Entry<Object, Object> entry: props.entrySet())
			result.put((String)entry.getKey(), (String)entry.getValue());
		
		return result;
	}
}

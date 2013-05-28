package com.ncgeek.games.shattered.utils;

import java.util.HashMap;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.ncgeek.games.shattered.entities.Sprite;
import com.ncgeek.games.shattered.shapes.Ellipse;
import com.ncgeek.games.shattered.shapes.IShape;
import com.ncgeek.games.shattered.shapes.Polygon;
import com.ncgeek.games.shattered.shapes.Polyline;
import com.ncgeek.games.shattered.shapes.Rectangle;

public class ShatteredMapLoader extends TmxMapLoader {

	private static HashMap<String, Class<? extends Sprite>> mapClasses = new HashMap<String, Class<? extends Sprite>>();
	
	public static void addMapping(String type, Class<? extends Sprite> cls) {
		if(type == null)
			throw new IllegalArgumentException("type cannot be null");
		if(cls == null)
			throw new IllegalArgumentException("cls cannot be null");
		
		type = type.toLowerCase().trim();
		
		if(type.length() == 0)
			throw new IllegalArgumentException("type cannot be empty");
		
		if(mapClasses.containsKey(type))
			throw new IllegalArgumentException(String.format("type '%s' has already been added", type));
		
		mapClasses.put(type, cls);
	}
	
	public ShatteredMapLoader() {
		super();
	}

	public ShatteredMapLoader(FileHandleResolver resolver) {
		super(resolver);
	}

	@Override
	protected void loadObject(MapLayer layer, Element element) {
		if(element.getName().equals("object")) {
			
			String type = element.getAttribute("type", "").toLowerCase().trim();
			
			if(ShatteredMapLoader.mapClasses.containsKey(type)) {
				Class<? extends Sprite> cls = ShatteredMapLoader.mapClasses.get(type);
				MapProperties props = new MapProperties();
				Element xmlProps = element.getChildByName("properties");
				if(xmlProps != null)
					loadProperties(props, xmlProps);
				
				int x = element.getIntAttribute("x", 0);
				int y = yUp ? mapHeightInPixels - element.getIntAttribute("y", 0) : element.getIntAttribute("y", 0);

				int width = element.getIntAttribute("width", 0);
				int height = element.getIntAttribute("height", 0);
				
				IShape bounds = null;
				
				if (element.getChildCount() > 0) {
					Element child = null;
					if ((child = element.getChildByName("polygon")) != null) {
						Polygon p = readPolygon(child);
						p.setPosition(x, y);
						bounds = p;
					} else if((child = element.getChildByName("polyline")) != null) {
						Polyline p = readPolyline(child);
						p.setPosition(x, y);
						bounds = p;
					} else if ((child = element.getChildByName("ellipse")) != null) {
						bounds = new Ellipse(x, yUp ? y - height : y, width, height);
					}
				} 
				if(bounds == null) {
					bounds = new Rectangle(x, yUp ? y - height : y, width/32, height/32);
				}
				
				props.put("name", element.getAttribute("name", null));
				if (type != null) {
					props.put("type", type);
				}
				props.put("x", x);
				props.put("y", yUp ? y - height : y);
				props.put("visible", element.getIntAttribute("visible", 1) == 1);
				
				try {
					Sprite object = (Sprite)cls.newInstance();
					object.load(props, bounds);
					layer.getObjects().add(object);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				super.loadObject(layer, element);
			}
		}
	}

	protected Polygon readPolygon(Element child) {
		String[] points = child.getAttribute("points").split(" ");
		float[] vertices = new float[points.length * 2];
		for (int i = 0; i < points.length; i++) {
			String[] point = points[i].split(",");
			vertices[i * 2] = Integer.parseInt(point[0]);
			vertices[i * 2 + 1] = Integer.parseInt(point[1]);
			if (yUp) {
				vertices[i * 2 + 1] *= -1;
			}
		}
		return new Polygon(vertices);
	}
	
	protected Polyline readPolyline(Element child) {
		String[] points = child.getAttribute("points").split(" ");
		float[] vertices = new float[points.length * 2];
		for (int i = 0; i < points.length; i++) {
			String[] point = points[i].split(",");
			vertices[i * 2] = Integer.parseInt(point[0]);
			vertices[i * 2 + 1] = Integer.parseInt(point[1]);
			if (yUp) {
				vertices[i * 2 + 1] *= -1;
			}
		}
		return new Polyline(vertices);
	}
}

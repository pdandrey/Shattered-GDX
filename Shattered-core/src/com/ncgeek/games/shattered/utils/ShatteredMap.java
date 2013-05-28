package com.ncgeek.games.shattered.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.ncgeek.games.shattered.entities.EntitySprite;
import com.ncgeek.games.shattered.entities.Sprite;

public class ShatteredMap implements Disposable {

	private static final float UNIT_SCALE = 1f / 32f;
	
	private TiledMap map;
	private TiledMapRenderer renderer;
	private Color bgColor;
	private HashMap<String,ArrayList<MapObject>> entities;
	private int width, height;
	private World world;
	
	private Box2DDebugRenderer b2renderer;
	
	public ShatteredMap(String mapName) {
		world = new World(new Vector2(0,0), true);
		
		map = new ShatteredMapLoader().load("maps/" + mapName + ".tmx");
		MapProperties props = map.getProperties();
		
		String bgColor = (String)props.get("background_color");
		width = (Integer)props.get("width");
		height = (Integer)props.get("height");
		
		float r = Integer.parseInt(bgColor.substring(1, 3), 16) / 255f;
		float g = Integer.parseInt(bgColor.substring(3,5), 16) / 255f;
		float b = Integer.parseInt(bgColor.substring(5,7), 16) / 255f;
		
		this.bgColor = new Color(r, g, b, 1);
		
		renderer = new TiledMapObjectRenderer(map, UNIT_SCALE);
		
		entities = new HashMap<String, ArrayList<MapObject>>();
		
		for(Iterator<MapLayer> i = map.getLayers().iterator(); i.hasNext(); ) {
			MapLayer ml = i.next();
			if(ml.getName().trim().toLowerCase().equals("collision")) {
				parseCollisions(ml);
			} else {
				for(Iterator<MapObject> j = ml.getObjects().iterator(); j.hasNext(); ) {
					MapObject mo = j.next();
					if(mo instanceof EntitySprite) {
						EntitySprite e = (EntitySprite)mo;
						e.setupBody(world, UNIT_SCALE);
					}
					String name = mo.getName();
					if(name != null && name.trim().length() > 0) {
						name = name.trim().toLowerCase();
						if(!entities.containsKey(name))
							entities.put(name, new ArrayList<MapObject>());
						entities.get(name).add(mo);
					}
				}
			}
		}
		
		b2renderer = new Box2DDebugRenderer();
	}

	@Override
	public void dispose() {
		map.dispose();
		world.dispose();
	}
	
	public List<MapObject> getEntityByName(String name) {
		return entities.get("player");
	}
	
	public final Color getBackgroundColor() { return bgColor; }
	public final int getWidth() { return width; }
	public final int getHeight() { return height; }
	
	public void render(OrthographicCamera camera) {
		for(List<MapObject> lst : entities.values()) {
			for(MapObject mo : lst) {
				if(mo instanceof Sprite) {
					((Sprite)mo).update();
				}
			}
		}
		world.step(Gdx.graphics.getDeltaTime(), 3, 3);
		
		renderer.setView(camera);
		renderer.render();
		
		b2renderer.render(world, camera.combined);
	}
	
	private void parseCollisions(MapLayer ml) {
		for(Iterator<MapObject> i = ml.getObjects().iterator(); i.hasNext(); ) {
			MapObject mo = i.next();
			
			if(mo instanceof RectangleMapObject)
				parseRectangleCollision((RectangleMapObject)mo);
			else if(mo instanceof PolygonMapObject)
				parsePolygonMapObject((PolygonMapObject)mo);
			else if(mo instanceof PolylineMapObject)
				parsePolylineMapObject((PolylineMapObject)mo);
			else if(mo instanceof CircleMapObject)
				parseCircleCollision((CircleMapObject)mo);
			else if(mo instanceof TextureMapObject)
				parseTextureMapObject((TextureMapObject)mo);
			else if(mo instanceof EllipseMapObject)
				parseEllipseMapObject((EllipseMapObject)mo);
		}
	}

	private void parseEllipseMapObject(EllipseMapObject mo) {
		Ellipse e = mo.getEllipse();
		if(e.width == e.height)
			parseCircleCollision(new CircleMapObject(e.x, e.y, e.width / 2));
//		else
//			throw new RuntimeException(String.format("Ellipses are not supported collision shapes: pos: %f, %f, size: %f, %f", e.x, e.y, e.width, e.height));
	}

	private void parseTextureMapObject(TextureMapObject mo) {
//		throw new RuntimeException("Method not implemented");
	}

	private void parseCircleCollision(CircleMapObject mo) {
		float radius = mo.getCircle().radius * UNIT_SCALE;
		BodyDef def = new BodyDef();
		def.type = BodyType.StaticBody;				
		CircleShape shape = new CircleShape();
		def.position.set(mo.getCircle().x * UNIT_SCALE, mo.getCircle().y * UNIT_SCALE);
		
		shape.setRadius(radius);
		world.createBody(def).createFixture(shape, 1);
		shape.dispose();	
	}

	private void parsePolylineMapObject(PolylineMapObject mo) {
		Polyline line = mo.getPolyline();
		float[] coords = line.getVertices();
		Vector2 [] vector = new Vector2[coords.length / 2];
		
		for(int i=0; i<coords.length; i+=2) {
			vector[i/2] = new Vector2(coords[i] * UNIT_SCALE, coords[i+1] * UNIT_SCALE);
		}
		
		BodyDef def = new BodyDef();
		def.type = BodyType.StaticBody;						
		ChainShape shape = new ChainShape();
		def.position.set(line.getX() * UNIT_SCALE, line.getY() * UNIT_SCALE);
		shape.createLoop(vector);
		world.createBody(def).createFixture(shape, 1);
		shape.dispose();
	}

	private void parsePolygonMapObject(PolygonMapObject mo) {
		Polygon p = mo.getPolygon();
		float[] coords = p.getVertices();
		Vector2 [] vector = new Vector2[coords.length / 2];
		
		for(int i=0; i<coords.length; i+=2) {
			vector[i/2] = new Vector2(coords[i] * UNIT_SCALE, coords[i+1] * UNIT_SCALE);
		}
		
		BodyDef def = new BodyDef();
		def.type = BodyType.StaticBody;						
		ChainShape shape = new ChainShape();
		def.position.set(p.getX() * UNIT_SCALE, p.getY() * UNIT_SCALE);
		shape.createLoop(vector);
		world.createBody(def).createFixture(shape, 1);
		shape.dispose();
	}

	private void parseRectangleCollision(RectangleMapObject mo) {
		float polyWidth = mo.getRectangle().width * UNIT_SCALE / 2f;
		float polyHeight = mo.getRectangle().height * UNIT_SCALE / 2f;
		BodyDef def = new BodyDef();
		def.type = BodyType.StaticBody;			
		def.fixedRotation = true;
		PolygonShape shape = new PolygonShape();
		def.position.set(mo.getRectangle().x * UNIT_SCALE + polyWidth, mo.getRectangle().y * UNIT_SCALE + polyHeight);
		
		shape.setAsBox(polyWidth, polyHeight);
		world.createBody(def).createFixture(shape, 1);
		shape.dispose();		
	}
}

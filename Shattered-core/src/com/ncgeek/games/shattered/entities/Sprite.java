package com.ncgeek.games.shattered.entities;

import java.util.Iterator;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.ncgeek.games.shattered.shapes.IShape;

public class Sprite extends MapObject implements Disposable {
	
	public static final String SPRITE_IMAGE = "image";
	public static final String SPRITE_WIDTH = "spritewidth";
	public static final String SPRITE_HEIGHT = "spriteheight";
	public static final String SPRITE_POSITION_X = "x";
	public static final String SPRITE_POSITION_Y = "y";
	
	private IShape bounds;
	private Texture texture;
	private float width, height;
	private Vector2 position;
	
	public Sprite() {}
	
	public void load(MapProperties props, IShape bounds) {
		setName(props.get("name", null, String.class));
		props.remove("name");
		
		width = props.get(SPRITE_WIDTH, -1, Integer.class);
		height = props.get(SPRITE_HEIGHT, -1, Integer.class);
		
		if(width <= 0)
			throw new IllegalArgumentException(SPRITE_WIDTH + " must be supplied and > 0");
		
		if(height <= 0)
			throw new IllegalArgumentException(SPRITE_HEIGHT + " must be supplied and > 0");
		
		String image = props.get(SPRITE_IMAGE, null, String.class);
		if(image == null)
			throw new IllegalArgumentException(SPRITE_IMAGE + " must be supplied");
		
		loadImage(image);
		
		position = new Vector2(
				props.get(SPRITE_POSITION_X, (int)bounds.getX(), Integer.class), 
				props.get(SPRITE_POSITION_Y, (int)bounds.getY(), Integer.class));
		
		for(Iterator<String> i = props.getKeys(); i.hasNext(); ) {
			String k = i.next();
			getProperties().put(k, props.get(k));
		}
		
		this.bounds = bounds;
	}
	
	@Override
	public void dispose() {
		if(texture != null) {
			texture.dispose();
			texture = null;
		}
	}
	
	public IShape getBounds() { return bounds; }
	public Vector2 getPosition() { return position; }
	
	protected void loadImage(String image) {
		if(texture != null)
			texture.dispose();
		texture = new Texture(image);
	}
	
	public void draw(SpriteBatch batch, float unitScale) {
		batch.draw(texture, position.x, position.y);
	}
	
	public void update() { }
}

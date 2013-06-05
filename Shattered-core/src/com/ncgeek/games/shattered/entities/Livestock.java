package com.ncgeek.games.shattered.entities;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.physics.box2d.World;
import com.ncgeek.games.shattered.shapes.IShape;
import com.ncgeek.games.shattered.utils.Log;

public class Livestock extends Mob {

	private static final String LOG_TAG = "Livestock";
	
	@Override
	public void load(MapProperties props, IShape bounds) {
		String image = props.get(SPRITE_IMAGE, String.class);
		
		if(image == null)
			throw new IllegalArgumentException("Image is required");
		
		image = image.toLowerCase().trim();
		
		if(image.equals("chicken")) {
			props.put(SPRITE_WIDTH, 64);
			props.put(SPRITE_HEIGHT, 64);
		} else {
			props.put(SPRITE_WIDTH, 128);
			props.put(SPRITE_HEIGHT, 128);
		}
		
		super.load(props, bounds);
		
		if(image.equals("chicken")) {
			getOffset().set(-16,-16);
		} else {
			getOffset().set(-64, -50);
		}
		
	}

	@Override
	public void setupBody(World world, float unitScale) {
		super.setupBody(world, unitScale);
	}

	@Override
	public void interact(EntitySprite target) {
		super.interact(target);
	}

}

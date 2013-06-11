package com.ncgeek.games.shattered.entities;

import java.util.Arrays;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.ncgeek.games.shattered.entities.movement.RandomMovement;
import com.ncgeek.games.shattered.shapes.IShape;
import com.ncgeek.games.shattered.utils.Log;
import com.ncgeek.games.shattered.utils.Strings;

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
		
		addEatingAnimations("eatsouth");
		addEatingAnimations("eatnorth");
		addEatingAnimations("eateast");
		addEatingAnimations("eatwest");
		
		if(movement == null)
			movement = new RandomMovement();
	}

	@Override
	public void setupBody(World world, float unitScale) {
		super.setupBody(world, unitScale);
	}

	@Override
	public void interact(EntitySprite target) {
		super.interact(target);
	}

	@Override
	protected void onSleep() {
		switch(getDirection()) {
		case 0: setCurrentAnimation("eatsouth-start"); break;
		case 1: setCurrentAnimation("eateast-start"); break;
		case 2: setCurrentAnimation("eatknorth-start"); break;
		case 3: setCurrentAnimation("eatwest-start"); break;
		}
		super.setIsAnimating(true);
	}
	
	@Override
	protected void onWake() {
		switch(getDirection()) {
		case 0: setCurrentAnimation("walksouth"); break;
		case 1: setCurrentAnimation("walkeast"); break;
		case 2: setCurrentAnimation("walknorth"); break;
		case 3: setCurrentAnimation("walkwest"); break;
		}
		super.setIsAnimating(false);
	}
	
	@Override
	protected void animationFinished(String key) {
		if(key.startsWith("eat")) {
			String[] parts = key.split("-");
			Log.log(LOG_TAG, Strings.join(", ", parts));
			if(parts.length == 2) {
				if(parts[1].equals("-start"))
					setCurrentAnimation(parts[0]);
				else
					setCurrentAnimation(parts[0] + "-start");
			} else {
				setCurrentAnimation(parts[0] + "-end");
			}
		}
	}

	private void addEatingAnimations(String name) {
		Array<AtlasRegion> regions = getAtlas().findRegions(name);
		Array<AtlasRegion> eating = new Array<AtlasRegion>();
		
		eating.add(regions.get(regions.size-2));
		eating.add(regions.get(regions.size-1));
		eating.addAll(eating);
		eating.addAll(eating);
		
		super.addAnimation(name + "-start", regions, 0.35f, Animation.NORMAL);
		super.addAnimation(name, 0.35f, Animation.NORMAL);
		super.addAnimation(name + "-end", regions, 0.35f, Animation.REVERSED);
	}
}

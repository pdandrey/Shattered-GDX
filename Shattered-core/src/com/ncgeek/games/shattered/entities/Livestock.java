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
import com.ncgeek.games.shattered.utils.Rand;
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
			setDrawingOffset(-16,-16);
		} else {
			setDrawingOffset(-64, -50);
		}
		
		addEatingAnimations("eatsouth");
		addEatingAnimations("eatnorth");
		addEatingAnimations("eateast");
		addEatingAnimations("eatwest");
		
		if(movement == null)
			movement = new RandomMovement();
		
		setState(State.MOVE);
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
		eat();
		//Log.log(LOG_TAG, "%s is now sleeping", getName());
//		switch(getState()) {
//			case MOVE:
//				// do we idle or eat?
//				//if(Rand.next() % 2 == 0)
//					eat();
////				else
////					idle();
//				break;
//		
//			default: break;
//		}
	}
	
	@Override
	protected void onWake() {
//		if(getState() == State.CUSTOM) {
			setCurrentAnimation("eat"+ getDirection().toString().toLowerCase() + "-end");
//		} else {
//			walk();
//		}
	}
	
	@Override
	protected void animationFinished(String key) {
		if(getState() == State.CUSTOM) {
			String[] parts = key.split("-");
			if(parts.length == 2) {
				if(parts[1].equals("start"))
					setCurrentAnimation(parts[0]);
				else
					walk();
			}
		}
	}
	
	private void eat() {
		setCurrentAnimation("eat"+ getDirection().toString().toLowerCase() + "-start");
		super.setIsAnimating(true);
		setState(State.CUSTOM);
	}
	
	public void walk() {
		setState(State.MOVE);
	}

	private void addEatingAnimations(String name) {
		Array<AtlasRegion> regions = getAtlas().findRegions(name);
		Array<AtlasRegion> eating = new Array<AtlasRegion>();
		
		eating.add(regions.pop());
		eating.add(regions.pop());
		eating.reverse();
		
		super.addAnimation(name + "-start", regions, 0.25f, Animation.NORMAL);
		super.addAnimation(name, eating, 0.5f, Animation.LOOP);
		super.addAnimation(name + "-end", regions, 0.25f, Animation.REVERSED);
	}
}

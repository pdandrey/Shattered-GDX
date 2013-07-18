package com.ncgeek.games.shattered.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import com.ncgeek.games.shattered.Direction;
import com.ncgeek.games.shattered.entities.movement.RandomMovement;
import com.ncgeek.games.shattered.shapes.IShape;
import com.ncgeek.games.shattered.utils.Log;

public class Livestock extends Mob {
	
	private static final String LOG_TAG = "Livestock";
	private String image;
	
	@Override
	public void load(MapProperties props, IShape bounds) {
		image = props.get(SPRITE_IMAGE, String.class);
		
		if(image == null)
			throw new IllegalArgumentException("Image is required");
		
		image = image.toLowerCase().trim();
		
		if(image.equals("chicken")) {
			props.put(SPRITE_WIDTH, 64);
			props.put(SPRITE_HEIGHT, 64);
			props.put(SPRITE_OFFSET_X, -16);
			props.put(SPRITE_OFFSET_Y, -16);
		} else {
			props.put(SPRITE_WIDTH, 128);
			props.put(SPRITE_HEIGHT, 128);
			if(image.equals("cow")) {
				props.put(SPRITE_OFFSET_X, -64);
				props.put(SPRITE_OFFSET_Y, -55);
			} else if(image.equals("pig")) {
				props.put(SPRITE_OFFSET_X, -64);
				props.put(SPRITE_OFFSET_Y, -60);
			} else if(image.equals("sheep")) { 
				props.put(SPRITE_OFFSET_X, -64);
				props.put(SPRITE_OFFSET_Y, -60);
			} else if(image.equals("llama")) {
				props.put(SPRITE_OFFSET_X, -64);
				props.put(SPRITE_OFFSET_Y, -55);
			}
		}
		
		super.load(props, bounds);
		
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

		if(image.equals("chicken"))
			super.setupBody(world, unitScale);
		else {
			BodyDef def = new BodyDef();
			def.type = BodyType.DynamicBody;
			def.fixedRotation = true;
			def.angle = (float)Math.toRadians(90);
			def.position.set(getPosition()).scl(unitScale);
			
			float width = 0;
			float height = 0;
			
			if(image.equals("cow")) {
				width = 27 * unitScale;
				height = 11.5f * unitScale;
			} else if(image.equals("pig")) {
				width = 22 * unitScale;
				height = 8 * unitScale;
			} else if(image.equals("sheep") || image.equals("llama")) {
				width = 18 * unitScale;
				height = 10 * unitScale;
			}
			
			PolygonShape shape = new PolygonShape();
			shape.setAsBox(width, height);
						
			FixtureDef fixture = new FixtureDef();
			fixture.shape = shape;
			fixture.density = 1;
			fixture.friction = 5;
			
			Body body = world.createBody(def);
			setBody(body);
			
			body.createFixture(fixture);
			shape.dispose();
		}
			
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
	public void turn(Direction dir) {
		super.turn(dir);
		if(!image.equals("chicken")) {
			float angle = 0;
			switch(dir) {
				case East:
					angle = 0;
					break;
				case North:
					angle = 1.57079633f;
					break;
				case West:
					angle = 3.14159265f;
					break;
				case South:
					angle = 4.71238898f;
					break;
			} 
			getBody().setTransform(getBody().getPosition(), angle);
		}
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

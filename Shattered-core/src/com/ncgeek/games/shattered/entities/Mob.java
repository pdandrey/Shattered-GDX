package com.ncgeek.games.shattered.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.ncgeek.games.shattered.shapes.IShape;

public class Mob extends EntitySprite {

	private int dir = 0;
	
	public Mob() {
		super();
	}
	
	@Override
	public void load(MapProperties props, IShape bounds) {
		props.put(Sprite.SPRITE_WIDTH, 64);
		props.put(Sprite.SPRITE_HEIGHT, 64);
		
		if(props.get(Sprite.SPRITE_IMAGE) == null)
			props.put(Sprite.SPRITE_IMAGE, "greendress");
		
		super.load(props, bounds);
		
		addAnimation("walknorth", 0.15f, Animation.LOOP);
		addAnimation("walkwest", 0.15f, Animation.LOOP);
		addAnimation("walkeast", 0.15f, Animation.LOOP);
		addAnimation("walksouth", 0.15f, Animation.LOOP);
		
		setCurrentAnimation("walksouth");
	}
	
	@Override
	public void setupBody(World world, float unitScale) {
		BodyDef def = new BodyDef();
		def.type = BodyType.DynamicBody;
		def.fixedRotation = false;
		def.position.set(getPosition().scl(unitScale));
		
		CircleShape shape = new CircleShape();
		shape.setRadius(10f * unitScale);
		
		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.density = 1;
		fixture.friction = 5;
		
		Body body = world.createBody(def);
		setBody(body);
		
		body.createFixture(fixture);
		shape.dispose();
		
		getOffset().set(-32 * unitScale, -8 * unitScale);
	}

	public void turn() {
		turn((dir + 1) % 4);
	}
	
	public void turn(int dir) {
		if(this.dir == dir)
			return;
		
		this.dir = dir;
		
		switch(dir) {
		case 0: setCurrentAnimation("walksouth"); break;
		case 1: setCurrentAnimation("walkeast"); break;
		case 2: setCurrentAnimation("walknorth"); break;
		case 3: setCurrentAnimation("walkwest"); break;
		}
		resetAnimation();
	}
	
	public void walk() {
		if(isAnimating())
			return;
		
		setIsAnimating(true);
		resetAnimation();
	}
	public void stand() {
		if(!isAnimating())
			return;
		
		setIsAnimating(false);
		resetAnimation();
		getVelocity().set(0, 0);
	}
	
	public void move(float x, float y) {
		getVelocity().set(x, y);
	}	
}

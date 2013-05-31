package com.ncgeek.games.shattered.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.ncgeek.games.shattered.shapes.IShape;
import com.ncgeek.games.shattered.utils.Dialog;
import com.ncgeek.games.shattered.utils.Log;

public class Chest extends EntitySprite {

	private static final String LOG_TAG = "Chest";
	private static final String CHEST_TYPE = "chest_type";
	
	private boolean isOpened = false;
	
	@Override
	public void load(MapProperties props, IShape bounds) {
		
		if(!props.containsKey(Sprite.SPRITE_WIDTH))
			props.put(Sprite.SPRITE_WIDTH, 32);
		if(!props.containsKey(Sprite.SPRITE_HEIGHT))
			props.put(Sprite.SPRITE_HEIGHT, 32);
		if(!props.containsKey(Sprite.SPRITE_IMAGE))
			props.put(Sprite.SPRITE_IMAGE, "Chests");
		
		super.load(props, bounds);
		
		String type = props.get(CHEST_TYPE, "", String.class).trim().toLowerCase();
		
		if(type != null && "square".equals(type)) {
			type = "square";
		} else {
			type = "rounded";
		}
		
		addAnimation(type, 0.15f, Animation.NORMAL);
		setIsAnimating(false);
		setCurrentAnimation(type);
	}

	@Override
	public void setupBody(World world, float unitScale) {
		BodyDef def = new BodyDef();
		def.type = BodyType.StaticBody;
		def.position.set(getPosition().scl(unitScale).cpy().add(16*unitScale, 16*unitScale));
		Log.log(LOG_TAG, "%s", getPosition().toString());
		
		PolygonShape shape = new PolygonShape();
		float radius = 16f * unitScale;
		shape.setAsBox(radius, radius);
		
		Body body = world.createBody(def);
		setBody(body);
		
		body.createFixture(shape, 1);
		shape.dispose();
		
		getOffset().set(-16 * unitScale, -16 * unitScale);
	}
	
	@Override
	public void draw(SpriteBatch batch, float unitScale) {
		super.draw(batch, unitScale);
	}

	@Override
	public void interact(EntitySprite target) {
		if(!isOpened) {
			setIsAnimating(true);
			Dialog.showDialog("You obtain an item");
			isOpened = true;
		} 
	}
}

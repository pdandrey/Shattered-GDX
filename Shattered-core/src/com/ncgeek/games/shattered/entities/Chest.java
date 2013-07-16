package com.ncgeek.games.shattered.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.ncgeek.games.shattered.dialog.Dialog;
import com.ncgeek.games.shattered.shapes.IShape;
import com.ncgeek.games.shattered.utils.Log;

public class Chest extends EntitySprite {

	private static final String LOG_TAG = "Chest";
	private static final String CHEST_TYPE = "chest_type";
	
	private boolean isOpened = false;
	
	@Override
	public void load(MapProperties props, IShape bounds) {
		
		//if(!props.containsKey(Sprite.SPRITE_WIDTH))
			props.put(Sprite.SPRITE_WIDTH, 32);
		//if(!props.containsKey(Sprite.SPRITE_HEIGHT))
			props.put(Sprite.SPRITE_HEIGHT, 24);
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
		setDrawingOffset(-getWidth()/2, -getHeight()/2);
	}

	@Override
	public void setupBody(World world, float unitScale) {
		BodyDef def = new BodyDef();
		def.type = BodyType.StaticBody;
		def.position.set(getPosition()).add(getWidth()/2, getHeight()/2).scl(unitScale);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(getWidth() / 2 * unitScale, getHeight() / 2 * unitScale);
		
		Body body = world.createBody(def);
		setBody(body);
		
		body.createFixture(shape, 1);
		shape.dispose();
	}
	
	@Override
	public void draw(SpriteBatch batch, float unitScale) {
		super.draw(batch, unitScale);
	}

	private int counter = 0;
	@Override
	public void interact(EntitySprite target) {
		if(!isOpened) {
			setIsAnimating(true);
			Dialog.getInstance().setText("You obtain an item");
			isOpened = true;
		} else {
			String[] responses = new String[] {
					"The chest is empty.",
					"You've already taken everything from here.",
					"Alas, nothing more remains.",
					"Careful, chests are known for just sitting there... empty.",
					"The gazebo eats you!"
			};
			
			Dialog.getInstance().setText(responses[counter]);
			counter = (counter + 1) % responses.length;
		}
	}
}

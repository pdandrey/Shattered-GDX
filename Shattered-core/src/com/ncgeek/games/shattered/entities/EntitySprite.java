package com.ncgeek.games.shattered.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.ncgeek.games.shattered.shapes.IShape;

public abstract class EntitySprite extends AnimatedSprite {

	private Body body;
	private Vector2 velocity;
	
	public EntitySprite() {
		body = null;
		velocity = new Vector2();
	}
	
	public void load(MapProperties props, IShape bounds) {
		super.load(props, bounds);
	}
	
	public abstract void setupBody(World world, float unitScale);
	
	protected final void setBody(Body body) {
		if(body != null)
			body.setUserData(null);
		this.body = body;
		body.setUserData(this);
	}
	public final Body getBody() { return body; }
	
	public final Vector2 getVelocity() { return velocity; }
	
	public void update() {
		body.setLinearVelocity(velocity);
		super.update();
	}
	
	@Override
	public void draw(SpriteBatch batch, float unitScale) {
		getPosition().set(body.getPosition()); //.scl(1f/unitScale));
		super.draw(batch, unitScale);
	}
}
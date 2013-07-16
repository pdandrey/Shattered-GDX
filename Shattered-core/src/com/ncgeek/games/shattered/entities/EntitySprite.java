package com.ncgeek.games.shattered.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.ncgeek.games.shattered.shapes.IShape;
import com.ncgeek.games.shattered.utils.Log;

public abstract class EntitySprite extends AnimatedSprite {

	private static final String LOG_TAG = "EntitySprite";
	
	private Body body;
	private Vector2 velocity;
	private Vector2 offset;
	private EntitySprite target;
	
	public EntitySprite() {
		body = null;
		velocity = new Vector2();
		offset = new Vector2(0,0);
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
	
	protected final void setDrawingOffset(float x, float y) {
		getPosition().sub(offset).add(x, y);
		offset.set(x, y);
	}
	protected final void setDrawingOffset(Vector2 offset) {
		setDrawingOffset(offset.x, offset.y);
	}
	
	public final EntitySprite getTarget() { return target; }
	public final void setTarget(EntitySprite tgt) { target = tgt; }
	public final boolean hasTarget() { return target != null; }
	
	public abstract void interact(EntitySprite target);
	
	public void update() {
		body.setLinearVelocity(velocity);
		super.update();
	}
	
	@Override
	public void draw(SpriteBatch batch, float unitScale) {
		Vector2 old = getPosition().cpy();
		getPosition().set(body.getPosition()).scl(1/unitScale).add(offset);
		super.draw(batch, unitScale);
		getPosition().set(old);
	}
}

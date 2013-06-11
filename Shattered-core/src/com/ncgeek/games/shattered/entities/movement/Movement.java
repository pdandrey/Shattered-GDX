package com.ncgeek.games.shattered.entities.movement;

import com.badlogic.gdx.math.Vector2;
import com.ncgeek.games.shattered.entities.Mob;
import com.ncgeek.games.shattered.shapes.IShape;
import com.ncgeek.games.shattered.utils.Log;

public abstract class Movement {
	
	private static final float DEFAULT_TOLERANCE = 16f;
	private static final String LOG_TAG = "Movement";
	
	private Vector2 destination;
	private float tolerance;
	private boolean bHasDestination;
	
	public Movement() {
		destination = new Vector2();
		bHasDestination = false;
		tolerance = DEFAULT_TOLERANCE;
	}
	
	public final float getTolerance() { return tolerance; }
	public final void setTolerance(float t) { tolerance = t; }
	
	public final Vector2 getDestination() { return destination.cpy(); }
	public final void clearDestination() { destination.set(0,0); bHasDestination = false; }
	public final void setDestination(Vector2 v) { 
		destination.set(v); 
		bHasDestination = true; 
	}
	public final void setDestination(float x, float y) {
		destination.set(x,y);
		bHasDestination = true;
	}
	
	public final boolean hasDestination() { return bHasDestination; }
	public boolean isAtDestination(Vector2 position) {
		return position.dst(destination) <= tolerance;
	}
	
	public abstract void getNextDestination(Vector2 position, IShape bounds);
}

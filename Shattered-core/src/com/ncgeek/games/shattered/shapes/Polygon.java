package com.ncgeek.games.shattered.shapes;

import com.badlogic.gdx.math.Vector2;

public class Polygon extends com.badlogic.gdx.math.Polygon implements IShape {

	public Polygon() {
		super();
	}

	public Polygon(float[] vertices) {
		super(vertices);
	}

	@Override
	public boolean contains(Vector2 v) {
		return super.contains(v.x, v.y);
	}
	
	@Override
	public float getWidth() {
		return super.getBoundingRectangle().width;
	}
	
	@Override
	public float getHeight() {
		return super.getBoundingRectangle().height;
	}
}

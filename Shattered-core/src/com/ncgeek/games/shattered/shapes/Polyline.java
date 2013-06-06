package com.ncgeek.games.shattered.shapes;

import com.badlogic.gdx.math.Vector2;

public class Polyline extends com.badlogic.gdx.math.Polyline implements IShape {

	public Polyline() {
		super();
	}

	public Polyline(float[] vertices) {
		super(vertices);
	}
	
	@Override
	public boolean contains(Vector2 v) {
		throw new UnsupportedOperationException("Cannot call contains on a Polyline");
	}
	
	@Override
	public float getWidth() {
		throw new UnsupportedOperationException("Cannot call width on a Polyline");
	}
	
	@Override
	public float getHeight() {
		throw new UnsupportedOperationException("Cannot call height on a Polyline");
	}

}

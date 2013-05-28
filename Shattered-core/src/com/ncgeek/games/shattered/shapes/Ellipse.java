package com.ncgeek.games.shattered.shapes;

import com.badlogic.gdx.math.Vector2;

public class Ellipse extends com.badlogic.gdx.math.Ellipse implements IShape {

	private static final long serialVersionUID = 1L;

	public Ellipse() {
		super();
	}

	public Ellipse(float x, float y, float width, float height) {
		super(x, y, width, height);
	}

	public Ellipse(Vector2 position, float width, float height) {
		super(position, width, height);
	}

	@Override
	public float getX() {
		return super.x;
	}

	@Override
	public float getY() {
		return super.y;
	}
	
	

}

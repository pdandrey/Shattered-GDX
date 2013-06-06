package com.ncgeek.games.shattered.shapes;

import com.badlogic.gdx.math.Vector2;;

public interface IShape {
	public float getX();
	public float getY();
	public float getWidth();
	public float getHeight();
	
	public boolean contains(Vector2 v);
}

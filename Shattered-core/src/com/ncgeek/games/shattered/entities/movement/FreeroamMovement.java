package com.ncgeek.games.shattered.entities.movement;

import com.badlogic.gdx.math.Vector2;
import com.ncgeek.games.shattered.shapes.IShape;
import com.ncgeek.games.shattered.utils.Rand;

public class FreeroamMovement extends Movement {

	private int maxX;
	private int maxY;
	
	public FreeroamMovement() { super(); }
	public FreeroamMovement(int maxx, int maxy) {
		super();
		maxX = maxx;
		maxY = maxy;
	}
	
	public final int getMaxX() { return maxX; }
	public final void setMaxX(int x) { maxX = x; }
	
	public final int getMaxY() { return maxY; }
	public final void setMaxY(int y) { maxY = y; }
	
	@Override
	public void getNextDestination(Vector2 pos, IShape bounds) {
		boolean xPos = Rand.next() % 2 == 0;
		boolean yPos = Rand.next() % 2 == 0;
		int x = Rand.next(maxX);
		int y = Rand.next(maxY);
		
		if(!xPos)
			x = -x;
		if(!yPos)
			y = -y;
		
		setDestination(pos.x + x, pos.y + y);
	}

}

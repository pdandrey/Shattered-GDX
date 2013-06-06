package com.ncgeek.games.shattered.entities.movement;

import com.badlogic.gdx.math.Vector2;
import com.ncgeek.games.shattered.shapes.IShape;
import com.ncgeek.games.shattered.utils.Log;
import com.ncgeek.games.shattered.utils.Rand;

public class RandomMovement extends Movement {

	private static final String LOG_TAG = "RandomMovement";
	private static final int MAX_ATTEMPTS = 5;
	
	@Override
	public void getNextDestination(IShape bounds) {
		int minx = (int)bounds.getX();
		int miny = (int)bounds.getY();
		int maxx = minx + (int)bounds.getWidth();
		int maxy = miny + (int)bounds.getHeight();
		Vector2 dest = new Vector2();
		
		int i = 0;
		boolean found = false;
		do {
			dest.x = Rand.next(minx, maxx);
			dest.y = Rand.next(miny, maxy);
			++i;
			found = bounds.contains(dest);
		} while(i < MAX_ATTEMPTS && !found);
		
		if(found)
			setDestination(dest);
	}

}

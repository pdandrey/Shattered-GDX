package com.ncgeek.games.shattered.characters;

public class HitPoints {

	private int current;
	private int max;
	
	public HitPoints() {
		current = max = 0;
	}
	
	public int getMax() { return max; }
	public void setMax(int max) {
		if(max <= 0)
			throw new IllegalArgumentException("Max HP cannot be <= 0");
		this.max = max;
		if(current > max)
			current = max;
	}
	
	public int getCurrent() { return current; }
	public void setCurrent(int current) { 
		if(current > max)
			throw new IllegalArgumentException("Cannot set HP over max");
		if(current < 0)
			current = 0;
		this.current = current;
	}
	
	public boolean isDead() { return current <= 0; }
	
	public int getBloodiedValue() { return max / 2; }
	
	public boolean isBloodied() { return current <= getBloodiedValue(); }
	
	public void takeDamage(int damage) {
		if(damage <= 0)
			throw new IllegalArgumentException("damage must be > 0");
		
		current = Math.max(0, current - damage);
	}
	
	public void heal(int healing) {
		if(healing <= 0)
			throw new IllegalArgumentException("Healing must be > 0");
		
		if(current <= 0) {
			current = healing;
		} else {
			current = Math.min(max, current + healing);
		}
	}
}

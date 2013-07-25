package com.ncgeek.games.shattered.characters;

import com.badlogic.gdx.graphics.g2d.Animation;

public class ShatteredCharacter {
	private String name;
	private String soul;
	private Animation animation;
	
	public ShatteredCharacter() {
		name = "No Name";
		soul = "No Soul";
		animation = null;
	}

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public String getSoul() { return soul; }
	public void setSoul(String soul) { this.soul = soul; }

	public Animation getAnimation() { return animation; }
	public void setAnimation(Animation animation) { this.animation = animation; }
	
	
}

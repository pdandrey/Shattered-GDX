package com.ncgeek.games.shattered.characters;

import com.badlogic.gdx.graphics.g2d.Animation;

public class ShatteredCharacter {
	private String name;
	private String soul;
	private Animation animation;
	
	private Stats baseStats;
	private Stats currentStats;
	private HitPoints hp;
	
	public ShatteredCharacter() {
		name = "No Name";
		soul = "No Soul";
		animation = null;
		baseStats = null;
		currentStats = null;
	}

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public String getSoul() { return soul; }
	public void setSoul(String soul) { this.soul = soul; }

	public Animation getAnimation() { return animation; }
	public void setAnimation(Animation animation) { this.animation = animation; }
	
	public void setBaseStats(Stats stats) { baseStats = stats; }
	public Stats getBaseStats() { return baseStats; }

	public void setCurrentStats(Stats stats) { currentStats = stats; }
	public Stats getCurrentStats() { return currentStats; }
	
	public void setHP(HitPoints hp) { this.hp = hp; }
	public HitPoints getHitPoints() { return hp; }
}

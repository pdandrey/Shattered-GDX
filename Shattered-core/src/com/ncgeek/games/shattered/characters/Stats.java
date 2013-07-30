package com.ncgeek.games.shattered.characters;

public class Stats {
	
	private int maxHP;
	
	private int str;
	private int dex;
	private int vit;
	private int agi;
	private int intel;
	private int mind;
	private int spirit;
	
	public Stats() {
		maxHP = str = dex = vit = agi = intel = mind = spirit = 0;
	}

	public int getMaxHP() {
		return maxHP;
	}

	public void setMaxHP(int maxHP) {
		this.maxHP = maxHP;
	}

	public int getStrength() {
		return str;
	}

	public void setStrength(int str) {
		this.str = str;
	}

	public int getDexterity() {
		return dex;
	}

	public void setDexterity(int dex) {
		this.dex = dex;
	}

	public int getVitality() {
		return vit;
	}

	public void setVitality(int vit) {
		this.vit = vit;
	}

	public int getAgility() {
		return agi;
	}

	public void setAgility(int agi) {
		this.agi = agi;
	}

	public int getIntellegence() {
		return intel;
	}

	public void setIntellegence(int intel) {
		this.intel = intel;
	}

	public int getMind() {
		return mind;
	}

	public void setMind(int mind) {
		this.mind = mind;
	}

	public int getSpirit() {
		return spirit;
	}

	public void setSpirit(int spirit) {
		this.spirit = spirit;
	}
	
	
}

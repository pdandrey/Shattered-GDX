package com.ncgeek.games.shattered.characters;

public class Stats {
	
	public enum Names { MaxHP, Strength, Dexterity, Vitality, Agility, Intelligence, Mind, Spirit }
	
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
	
	public Stats(Stats copyFrom) {
		maxHP = copyFrom.maxHP;
		str = copyFrom.str;
		dex = copyFrom.dex;
		vit = copyFrom.vit;
		agi = copyFrom.agi;
		intel = copyFrom.intel;
		mind = copyFrom.mind;
		spirit = copyFrom.spirit;
	}
	
	public void set(Names name, int value) {
		switch(name) {
			case MaxHP: setMaxHP(value); break;
			case Strength: setStrength(value); break;
			case Dexterity: setDexterity(value); break;
			case Vitality: setVitality(value); break;
			case Agility: setAgility(value); break;
			case Intelligence: setIntellegence(value); break;
			case Mind: setMind(value); break;
			case Spirit: setSpirit(value); break;
		}
	}
	
	public int get(Names name) {
		switch(name) {
			case MaxHP: return getMaxHP();
			case Strength: return getStrength();
			case Dexterity: return getDexterity();
			case Vitality: return getVitality();
			case Agility: return getAgility();
			case Intelligence: return getIntellegence();
			case Mind: return getMind();
			case Spirit: return getSpirit();
			
			default:
				throw new IllegalArgumentException("Unknown stat name: " + name.toString());
		}
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

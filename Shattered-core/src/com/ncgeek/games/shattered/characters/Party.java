package com.ncgeek.games.shattered.characters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Party {
	
	private ArrayList<ShatteredCharacter> party;
	private ArrayList<ShatteredCharacter> reserves;
	private int maxPartySize;
	
	public Party() {
		party = new ArrayList<ShatteredCharacter>();
		reserves = new ArrayList<ShatteredCharacter>();
		maxPartySize = 4;
	}
	
	public void add(ShatteredCharacter character) {
		if(party.size() < maxPartySize)
			party.add(character);
		else
			reserves.add(character);
	}
	
	public boolean moveToReserve(ShatteredCharacter character) {
		if(party.remove(character)) {
			reserves.add(character);
			return true;
		}
		return false;
	}
	
	public boolean moveToParty(ShatteredCharacter character) {
		if(party.size() < maxPartySize && reserves.remove(character)) {
			party.add(character);
			return true;
		}
		return false;
	}
	
	public boolean remove(ShatteredCharacter character) {
		return party.remove(character) || reserves.remove(character);
	}
	
	public final int getMaxPartySize() { return maxPartySize; }
	
	public List<ShatteredCharacter> getParty() {
		return Collections.unmodifiableList(party);
	}
	
	public List<ShatteredCharacter> getReserves() {
		return Collections.unmodifiableList(reserves);
	}
}

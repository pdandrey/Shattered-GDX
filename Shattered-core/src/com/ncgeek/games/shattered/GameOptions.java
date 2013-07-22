package com.ncgeek.games.shattered;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class GameOptions {
	
	private static final String KEY_DRAW_DEBUG = "debugDraw";
	
	private Preferences prefs;
	private boolean flush;
	
	public GameOptions() {
		prefs = Gdx.app.getPreferences("Options");
		flush = true;
	}
	
	public void setBulkUpdate() { flush = false; }
	public void endBulkUpdate() { flush = true; prefs.flush(); }
	
	public boolean getDebugDraw() {
		return prefs.getBoolean(KEY_DRAW_DEBUG, false);
	}
	
	public void setDebugDraw(boolean draw) {
		prefs.putBoolean(KEY_DRAW_DEBUG, draw);
		if(flush)
			prefs.flush();
	}
}

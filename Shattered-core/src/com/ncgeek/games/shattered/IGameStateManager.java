package com.ncgeek.games.shattered;

import com.ncgeek.games.shattered.screens.ShatteredScreen;

public interface IGameStateManager {
	public void pushScreen(ShatteredScreen screen);
	public void popScreen(Object returnValue);
}

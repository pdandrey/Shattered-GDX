package com.ncgeek.games.shattered;


import com.badlogic.gdx.Game;
import com.ncgeek.games.shattered.entities.Chest;
import com.ncgeek.games.shattered.entities.Livestock;
import com.ncgeek.games.shattered.entities.Mob;
import com.ncgeek.games.shattered.screens.GameMenu;
import com.ncgeek.games.shattered.screens.GameScreen;
import com.ncgeek.games.shattered.screens.MainMenu;
import com.ncgeek.games.shattered.utils.Log;
import com.ncgeek.games.shattered.utils.ShatteredMapLoader;

public class Shattered extends Game implements IGameStateManager {

	private final static String LOG_TAG = "Shattered";
	
	private GameState state;
	
	private GameScreen screenGame;
	private MainMenu screenMainMenu;
	private GameMenu screenMenu;
	
	@Override
	public void create() {		
		
		ShatteredMapLoader.addMapping("mob", Mob.class);
		ShatteredMapLoader.addMapping("chest", Chest.class);
		ShatteredMapLoader.addMapping("livestock", Livestock.class);
		
		screenGame = new GameScreen(this);
		screenMenu = new GameMenu(this);
		setScreen(screenGame);
		state = GameState.Game;
	}
	
	public void setState(GameState newState) {
		if(state == newState)
			return;
		
		switch(newState) {
			case SplashScreen:
				Log.error(LOG_TAG, "Cannot set state to SplashScreen after the game has started.");
				return;
				
			case MainMenu:
				setScreen(screenMainMenu);
				break;
				
			case Game:
				setScreen(screenGame);
				break;
				
			case Menu:
				setScreen(screenMenu);
				break;
		}
		
		Log.debug(LOG_TAG, "Switching from state %s to %s", state.toString(), newState.toString());
		state = newState;
	}
}

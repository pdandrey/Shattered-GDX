package com.ncgeek.games.shattered;


import java.util.LinkedList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.ncgeek.games.shattered.entities.Chest;
import com.ncgeek.games.shattered.entities.Livestock;
import com.ncgeek.games.shattered.entities.Mob;
import com.ncgeek.games.shattered.screens.GameScreen;
import com.ncgeek.games.shattered.screens.MainMenu;
import com.ncgeek.games.shattered.screens.ShatteredScreen;
import com.ncgeek.games.shattered.utils.Log;
import com.ncgeek.games.shattered.utils.ShatteredMapLoader;

public class Shattered extends Game implements IGameStateManager {

	private final static String LOG_TAG = "Shattered";
	
	private GameState state;
	
	private GameScreen screenGame;
	private MainMenu screenMainMenu;
	
	private LinkedList<ShatteredScreen> stackScreens;
	
	@Override
	public void create() {
		stackScreens = new LinkedList<ShatteredScreen>();
		
		Gdx.input.setCatchBackKey(true);
		Gdx.input.setCatchMenuKey(true);
		
		ShatteredMapLoader.addMapping("mob", Mob.class);
		ShatteredMapLoader.addMapping("chest", Chest.class);
		ShatteredMapLoader.addMapping("livestock", Livestock.class);
		
		screenGame = new GameScreen(this);
		setScreen(screenGame);
		state = GameState.Game;
	}
	
	@Override
	public void pushScreen(ShatteredScreen screen) {
		stackScreens.push((ShatteredScreen)getScreen());
		setScreen(screen);
	}

	@Override
	public void popScreen(Object returnValue) {
		if(stackScreens.size() == 0) {
			Gdx.app.exit();
			return;
		}
		
		ShatteredScreen s = stackScreens.pop();
		s.setParameter(returnValue);
		setScreen(s);
	}
}

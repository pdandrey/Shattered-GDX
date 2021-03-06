package com.ncgeek.games.shattered.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.ncgeek.games.shattered.IGameStateManager;

public abstract class ShatteredScreen implements Screen {

	private final IGameStateManager manager;
	private InputMultiplexer input;
	private InputProcessor oldInput;
	private ControllerListener gamepad;
	
	public ShatteredScreen(IGameStateManager manager) {
		this.manager = manager;		
		
		input = new InputMultiplexer(new InputAdapter() {

			@Override
			public boolean keyDown(int keycode) {
				if(keycode == Keys.BACK) {
					getManager().popScreen(null);
					return true;
				}
				return super.keyDown(keycode);
			}
		});
	}
	
	protected final IGameStateManager getManager() { return manager; }
	
	protected final void setGamepadController(ControllerListener gamepad) { this.gamepad = gamepad; }
	
	protected final void addInputProcessor(InputProcessor input) {
		this.input.addProcessor(input);
	}
	
	public abstract void setParameter(Object parameter);
	
	@Override
	public void resize(int width, int height) {	}

	@Override
	public void show() {
		oldInput = Gdx.input.getInputProcessor();
		Gdx.input.setInputProcessor(input);
		if(gamepad != null) {
			Controllers.addListener(gamepad);
		}
	}

	@Override
	public void hide() { 
		Gdx.input.setInputProcessor(oldInput);
		if(gamepad != null) {
			Controllers.removeListener(gamepad);
		}
		oldInput = null;
	}

	@Override
	public void pause() { }

	@Override
	public void resume() { }

	@Override
	public void dispose() { }

}

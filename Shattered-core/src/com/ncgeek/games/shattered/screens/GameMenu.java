package com.ncgeek.games.shattered.screens;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.ncgeek.games.shattered.GameOptions;
import com.ncgeek.games.shattered.GameState;
import com.ncgeek.games.shattered.IGameStateManager;

public class GameMenu implements Screen {

	private Stage stage;
	private InputProcessor oldInput;
	private InputMultiplexer input;
	private GameOptions options;
	
	private TextButton btnDebug;
	private IGameStateManager manager;
	
	public GameMenu(IGameStateManager manager) {
		this.manager = manager;
		options = new GameOptions();
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		stage = new Stage(w/2, h/2, false);
		
		input = new InputMultiplexer(stage);
		
		createUI();
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0,0,0, 1f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.setViewport(width/2, height/2, true);
	}

	@Override
	public void show() {
		oldInput = Gdx.input.getInputProcessor();
		Gdx.input.setInputProcessor(input);
		updateUI();
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(oldInput);
	}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void dispose() {
		stage.dispose();
	}

	private void createUI() {
		Skin skin = new Skin(Gdx.files.internal("data/skin3.json"));
		
		Table parent = new Table();
		parent.setFillParent(true);
		
		btnDebug = new TextButton("Debug Drawing: ", skin);
		
		TextButton btnClose = new TextButton("Close", skin);
		
		parent.add(btnDebug).top().center();
		parent.row();
		parent.add(btnClose).top().center().fill();
		
		stage.addActor(parent);
		
		btnDebug.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				options.setDebugDraw(!options.getDebugDraw());
				updateUI();
			}
		});
		
		btnClose.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				manager.setState(GameState.Game);
			}
		});
	}
	
	private void updateUI() {
		btnDebug.setText("Debug Drawing: " + (options.getDebugDraw() ? "on" : "off" ));
	}
}

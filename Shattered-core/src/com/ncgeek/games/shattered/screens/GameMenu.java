package com.ncgeek.games.shattered.screens;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.ncgeek.games.shattered.GameOptions;
import com.ncgeek.games.shattered.IGameStateManager;
import com.ncgeek.games.shattered.characters.Party;
import com.ncgeek.games.shattered.characters.ShatteredCharacter;

public class GameMenu extends ShatteredScreen {

	private Stage stage;
	private GameOptions options;
	
	private TextButton btnDebug;
	private Party party;
	
	private java.util.ArrayList<CharacterListItem> lst = new java.util.ArrayList<CharacterListItem>();
	
	public GameMenu(IGameStateManager manager, Party party) {
		super(manager);
		
		this.party = party;
		options = new GameOptions();
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		stage = new Stage(w/2, h/2, false);
		
		addInputProcessor(stage);
		
		createUI();
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0,0,0, 1f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		stage.act(delta);
		stage.draw();
		Table.drawDebug(stage);
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		stage.setViewport(width/2, height/2, true);
	}

	@Override
	public void show() {
		super.show();
		updateUI();
		
		for(CharacterListItem cli : lst) {
			cli.size();
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		stage.dispose();
	}

	private void createUI() {
		Skin skin = new Skin(Gdx.files.internal("data/skin3.json"));
		
		Table parent = new Table(skin);
		
		parent.setFillParent(true);
		stage.addActor(parent);
		
		parent.add(createPartyList(skin)).colspan(3).expand().fill();
		parent.add("stats").width(200).fill();
		parent.row();
		
		btnDebug = new TextButton("Debug Drawing: ", skin);
		
		TextButton btnClose = new TextButton("Close", skin);
		
		parent.add(btnDebug);
		parent.add(new TextButton("Button", skin));
		parent.add(new TextButton("Button", skin));
		parent.add(btnClose);
		
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
				getManager().popScreen(null);
			}
		});
	}
	
	private Actor createPartyList(Skin skin) {
		Table tbl = new Table();
		tbl.setFillParent(true);
		//tbl.debug();
		
		int i = 0;
		final int MAX_COLS = 2;
		CharacterListItem cli = null;
		
		for(ShatteredCharacter sc : party.getParty()) {
			if(i >= MAX_COLS) {
				tbl.row();
				i = 0;
			}
			cli = new CharacterListItem(sc, skin);
			cli.top().left();
			cli.debug();
			//tbl.add(cli).left();
			lst.add(cli);
			++i;
			break;
		}
		
//		for(ShatteredCharacter sc : party.getReserves()) {
//			if(i >= MAX_COLS) {
//				tbl.row();
//				i = 0;
//			}
//			CharacterListItem cli = new CharacterListItem(sc, skin);
//			cli.debug();
//			tbl.add(cli);
//			lst.add(cli);
//			++i;
//		}
		
//		while(i < MAX_COLS) {
//			tbl.add();
//			++i;
//		}
		
		ScrollPane pane = new ScrollPane(cli, skin);
		return pane;
	}
	
	private void updateUI() {
		btnDebug.setText("Debug Drawing: " + (options.getDebugDraw() ? "on" : "off" ));
	}

	@Override
	public void setParameter(Object parameter) {
		// TODO Auto-generated method stub
		
	}
}

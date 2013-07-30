package com.ncgeek.games.shattered.screens;

import java.util.EnumMap;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.ncgeek.games.shattered.GameOptions;
import com.ncgeek.games.shattered.IGameStateManager;
import com.ncgeek.games.shattered.characters.Party;
import com.ncgeek.games.shattered.characters.ShatteredCharacter;
import com.ncgeek.games.shattered.characters.Stats;
import com.ncgeek.games.shattered.characters.Stats.Names;
import com.ncgeek.games.shattered.screens.PartyLayout.CharacterSelectedEvent;

public class GameMenu extends ShatteredScreen {

	final static float WIDTH_STATS = 200f;
	
	private Stage stage;
	private GameOptions options;
	
	private TextButton btnDebug;
	private Party party;
	
	private Label lblName;
	private Label lblSoul;
	private Label lblHP;
	private EnumMap<Stats.Names, Label> mapStats;
	
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
		
		Actor stats = createStats(skin);
		
		parent.add(createPartyList(skin)).colspan(3).expand().fill();
		parent.add(stats).width(WIDTH_STATS).fill();
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
	
	private Actor createStats(Skin skin) {
		Table tbl = new Table();
		
		lblName = new Label("", skin);
		lblSoul = new Label("", skin);
		lblHP = new Label("", skin);
		
		tbl.add(lblName).colspan(2);
		tbl.row();
		tbl.add(lblSoul).colspan(2);
		tbl.row();
		tbl.add(lblHP).colspan(2);
		
		mapStats = new EnumMap<Stats.Names, Label>(Stats.Names.class);
		
		for(Stats.Names stat : Stats.Names.values()) {
			if(stat == Names.MaxHP)
				continue;
			
			Label lbl = new Label("", skin);
			mapStats.put(stat, lbl);
			tbl.row();
			tbl.add(new Label(stat.toString(), skin));
			tbl.add(lbl).expandX().right();
		}
		
		tbl.top().left();
		return tbl;
	}

	private Actor createPartyList(Skin skin) {
		PartyLayout layoutParty = new PartyLayout();
		
		CharacterListItem cli = null;
		for(ShatteredCharacter sc : party.getParty()) {
			cli = new CharacterListItem(sc, skin);
			layoutParty.addActor(cli);
		}
		
		for(ShatteredCharacter sc : party.getReserves()) {
			cli = new CharacterListItem(sc, skin);
			layoutParty.addActor(cli);
		}
		
		layoutParty.addListener(new PartyLayout.CharacterSelectedListener() {
			@Override
			public boolean characterSelected(CharacterSelectedEvent event) {
				ShatteredCharacter c = event.getSelected().getCharacter();
				setCharacterStats(c);
				return true;
			}
		});
		
		setCharacterStats(layoutParty.getSelectedCharacterListItem().getCharacter());
		
		ScrollPane pane = new ScrollPane(layoutParty, skin);
		pane.setFadeScrollBars(false);
		return pane;
	}
	
	private void setCharacterStats(ShatteredCharacter c) {
		lblName.setText(c.getName());
		lblSoul.setText(c.getSoul());
		lblHP.setText(String.format("HP: %d / %d", c.getHitPoints().getCurrent(), c.getHitPoints().getMax()));
		
		Stats stats = c.getBaseStats();
		
		for(Stats.Names name : Stats.Names.values()) {
			Label l = mapStats.get(name);
			if(l != null) {
				l.setText(stats.get(name) + "");
			}
		}
	}
	
	private void updateUI() {
		btnDebug.setText("Debug Drawing: " + (options.getDebugDraw() ? "on" : "off" ));
	}

	@Override
	public void setParameter(Object parameter) {
		// TODO Auto-generated method stub
		
	}
}

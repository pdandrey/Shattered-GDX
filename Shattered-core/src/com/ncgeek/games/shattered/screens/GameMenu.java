package com.ncgeek.games.shattered.screens;

import java.util.EnumMap;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.ncgeek.games.shattered.GameOptions;
import com.ncgeek.games.shattered.IGameStateManager;
import com.ncgeek.games.shattered.characters.Party;
import com.ncgeek.games.shattered.characters.ShatteredCharacter;
import com.ncgeek.games.shattered.characters.Stats;
import com.ncgeek.games.shattered.characters.Stats.Names;
import com.ncgeek.games.shattered.screens.PartyLayout.CharacterSelectedEvent;
import com.ncgeek.games.shattered.screens.ui.FocusGroup;
import com.ncgeek.games.shattered.screens.ui.FocusTextButton;
import com.ncgeek.games.shattered.utils.Log;

public class GameMenu extends ShatteredScreen {

	final static float WIDTH_STATS = 200f;
	
	private Stage stage;
	private GameOptions options;
	 
	private FocusTextButton btnDebug;
	private Party party;
	 
	private Label lblName;
	private Label lblSoul;
	private Label lblHP;
	private EnumMap<Stats.Names, Label> mapStats;
	
	private int focusedIndex = 0;
	private FocusTextButton [] buttons;
	
	private int[] keysMoveUp = { Keys.UP, Keys.W };
	private int[] keysMoveDown = { Keys.DOWN, Keys.S };
	private int[] keysMoveLeft = { Keys.LEFT, Keys.A };
	private int[] keysMoveRight = { Keys.RIGHT, Keys.D };
	private int[] keysMenu = { Keys.ESCAPE, Keys.UNKNOWN };
	private int[] keysAction = { Keys.ENTER, Keys.SPACE };
	private int[] keysCancel = { Keys.BACKSPACE, Keys.UNKNOWN };
	
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
		
		btnDebug = new FocusTextButton("Debug Drawing: ", skin);
		
		FocusTextButton btnClose = new FocusTextButton("Close", skin);
		btnClose.setChecked(true);
		buttons = new FocusTextButton [] {
				btnDebug,
				new FocusTextButton("Button", skin),
				new FocusTextButton("Button", skin),
				btnClose
		};
		
		
		FocusGroup fg = new FocusGroup(buttons);
		for(FocusTextButton ftb : buttons) {
			parent.add(ftb);
		}
		
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
				close();
			}
		});
		
		stage.addListener(new InputListener() {
			@Override
			public boolean keyUp(InputEvent event, int keycode) {
				if(keycode == keysMoveUp[0] || keycode == keysMoveUp[1] || keycode == keysMoveLeft[0] || keycode == keysMoveLeft[1]) {
					--focusedIndex;
					if(focusedIndex < 0)
						focusedIndex = buttons.length - 1;
				} else if(keycode == keysMoveDown[0] || keycode == keysMoveDown[1] || keycode == keysMoveRight[0] || keycode == keysMoveRight[1]) {
					focusedIndex = (focusedIndex + 1) % buttons.length;
				} else if(keycode == keysAction[0] || keycode == keysAction[1]) {
					buttons[focusedIndex].click();
					return true;
				} else if(keycode == keysCancel[0] || keycode == keysCancel[1] || keycode == keysMenu[0] || keycode == keysMenu[1]) {
					close();
					return true;
				} else {
					return super.keyUp(event, keycode);
				}
				
				buttons[focusedIndex].setFocused(true);
				
				return true;					
			}
		});
		
		buttons[focusedIndex].setFocused(true);
	}
	
	private void close() {
		getManager().popScreen(null);
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

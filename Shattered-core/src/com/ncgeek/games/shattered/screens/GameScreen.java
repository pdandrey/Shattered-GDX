package com.ncgeek.games.shattered.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.ncgeek.games.shattered.GameOptions;
import com.ncgeek.games.shattered.IGameStateManager;
import com.ncgeek.games.shattered.characters.HitPoints;
import com.ncgeek.games.shattered.characters.Party;
import com.ncgeek.games.shattered.characters.ShatteredCharacter;
import com.ncgeek.games.shattered.characters.Stats;
import com.ncgeek.games.shattered.dialog.Dialog;
import com.ncgeek.games.shattered.entities.Mob;
import com.ncgeek.games.shattered.utils.GameControllerListener;
import com.ncgeek.games.shattered.utils.Log;
import com.ncgeek.games.shattered.utils.ShatteredController;
import com.ncgeek.games.shattered.utils.ShatteredMap;

public class GameScreen extends ShatteredScreen {

private final static float TOUCHPAD_MAX_SCROLL_SPEED = 3f; //2.1f;

	private final String LOG_TAG = "GameScreen";

	private ShatteredMap map;
	private OrthographicCamera camera;
	private ShatteredController controller;
	private BitmapFont font;
	private SpriteBatch batch;
	private Stage stage;
	
	private Touchpad pad;
	
	private Mob player;
	
	private boolean isTouchscreen = true;
	
	private GameOptions options;
	
	private boolean bPaused;
	
	private Vector2 playerMovement;
	
	private GameMenu screenMenu;
	
	private Party party;
	
	public GameScreen(IGameStateManager manager) {
		super(manager);
		
		bPaused = false;
		options = new GameOptions();
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		camera = new OrthographicCamera();
		float vpWidth = (w / h) * 10;
		float vpHeight = 10;
		camera.setToOrtho(false, vpWidth, vpHeight);
		Log.log(LOG_TAG, "Screen Size: %f x %f", w, h);
		Log.log(LOG_TAG, "ViewPort size: %f x %f", vpWidth, vpHeight);
		camera.update();
		
		playerMovement = new Vector2(0, 0);
		
		controller = new ShatteredController();
		controller.setActionListener(new GameControllerListener() {
			@Override
			public void action() {
				if(Dialog.getInstance().isDialogActive()) {
					Dialog.getInstance().progress();
				} else if(player.hasTarget()) {
					player.getTarget().interact(player);
				}
			}

			@Override
			public void cancel() {
				
			}

			@Override
			public void move(float x, float y) {
				playerMovement.set(x, y);
			}

			@Override
			public void menu() {
				getManager().pushScreen(screenMenu);
			}

			@Override
			public void pause() {
				
			}
		});
		
		super.setGamepadController(controller.getGamepadListener());
		
		stage = new Stage(w/2, h/2, false);
		
		addInputProcessor(stage);
		addInputProcessor(controller);
		
		font = new BitmapFont();
		batch = new SpriteBatch();

		map = new ShatteredMap("test");
		
		createUI();
		
		player = (Mob)map.getEntityByName("player").get(0);
		player.update(0f);
		Vector3 curr = new Vector3(player.getPosition().x, player.getPosition().y, 0);
		camera.position.set(curr);
		
		loadParty();
		screenMenu = new GameMenu(manager, party);
		
		//isTouchscreen = Gdx.input.isPeripheralAvailable(Peripheral.MultitouchScreen);
	}
	
	@Override
	public void render(float delta) {
		Color bgColor = map.getBackgroundColor();
		Gdx.gl.glClearColor(bgColor.r, bgColor.g, bgColor.b, 1f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		float x = playerMovement.x, y = playerMovement.y;
		
		if(!bPaused) {
			if(x != 0 || y != 0) {
				player.move(TOUCHPAD_MAX_SCROLL_SPEED * x, TOUCHPAD_MAX_SCROLL_SPEED * y);
				camera.position.set(player.getPosition().x, player.getPosition().y, 0);
			} else {
				player.move(0,0);
			}
			
			map.updateEntites(delta);
		}
		
		map.render(camera);
		
		batch.begin();
		
		Vector3 pos = new Vector3(player.getPosition().x, player.getPosition().y, 0);
		
		font.draw(batch, String.format("FPS: %d, Camera: %f, %f, Player: %f, %f", Gdx.graphics.getFramesPerSecond(), camera.position.x, camera.position.y, pos.x, pos.y), 10, 20);
		batch.end();
		
		if(options.getDebugDraw())
			map.drawDebug(camera);
		
		stage.act(delta);
		stage.draw();
//		Table.drawDebug(stage);
	}
	
	private void createUI() {
		Skin skin = new Skin(Gdx.files.internal("data/skin3.json"));
		
		Table parent = new Table();
		parent.setFillParent(true);
		parent.debug();
		
		
		TextButton btnMenu = new TextButton("Menu", skin);
		parent.add(btnMenu).top().right();
		
		btnMenu.addListener(controller.getMenuButtonClickedListener());
		
		parent.row();
		
		if(isTouchscreen) {
			pad = new Touchpad(10f, skin);
			pad.addListener(controller.getTouchpadChangedListener());
			//parent.add(pad).expand().bottom().left();
			parent.add(pad).expand().bottom().left();
		} else {
			parent.add().expand();
		}
		
		stage.addActor(parent);

		Label l = new Label("", skin);
		l.setName("lblDialog");
		l.setWrap(true);
		l.setAlignment(Align.top | Align.left);
		
		ScrollPane p = new ScrollPane(l, skin, "dialog");
		p.setSize(stage.getWidth() - 10, 100);
		p.setPosition(5, 5);
		Dialog.init(p);
		p.setFadeScrollBars(false);
		stage.addActor(p);
	}
	
	private void loadParty() {
		Stats s = new Stats();
		
		party = new Party();
		ShatteredCharacter sc = new ShatteredCharacter();
		HitPoints hp = new HitPoints();
		hp.setMax(24);
		hp.setCurrent(24);
		sc.setName("Player");
		sc.setSoul("Tester");
		sc.setAnimation(player.getAnimation("walksouth"));
		sc.setHP(hp);
		sc.setBaseStats(s);
		party.add(sc);
		
		Mob m = (Mob)map.getEntityByName("princess").get(0);
		sc = new ShatteredCharacter();
		hp = new HitPoints();
		hp.setMax(17);
		hp.setCurrent(10);
		sc.setName("Princess");
		sc.setSoul("Royalty");
		sc.setAnimation(m.getAnimation("walksouth"));
		sc.setHP(hp);
		sc.setBaseStats(s);
		party.add(sc);
		
		m = (Mob)map.getEntityByName("anna").get(0);
		sc = new ShatteredCharacter();
		hp = new HitPoints();
		hp.setMax(30);
		hp.setCurrent(5);
		sc.setName("Anna");
		sc.setSoul("Fashionista");
		sc.setAnimation(m.getAnimation("walksouth"));
		sc.setHP(hp);
		sc.setBaseStats(s);
		party.add(sc);
	}

	@Override
	public void resize(int width, int height) {
		stage.setViewport((float)width/2f, (float)height/2f, true);
		camera.setToOrtho(false, ((float)width/(float)height)*10, 10);
		Vector3 curr = new Vector3(player.getPosition().x, player.getPosition().y, 0);
		camera.position.set(curr);
		camera.update();
	}

	@Override
	public void show() {
		super.show();
		resume();
	}

	@Override
	public void hide() {
		super.hide();
		pause();
	}

	@Override
	public void pause() {
		super.pause();
		bPaused = true;
	}

	@Override
	public void resume() {
		super.pause();
		bPaused = false;
	}

	@Override
	public void dispose() {
		super.dispose();
		batch.dispose();
		map.dispose();
	}

	@Override
	public void setParameter(Object parameter) {
	}

}

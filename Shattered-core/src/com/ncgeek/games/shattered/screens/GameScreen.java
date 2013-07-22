package com.ncgeek.games.shattered.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.ncgeek.games.shattered.GameOptions;
import com.ncgeek.games.shattered.GameState;
import com.ncgeek.games.shattered.IGameStateManager;
import com.ncgeek.games.shattered.dialog.Dialog;
import com.ncgeek.games.shattered.entities.Mob;
import com.ncgeek.games.shattered.utils.ActionListener;
import com.ncgeek.games.shattered.utils.Log;
import com.ncgeek.games.shattered.utils.ShatteredController;
import com.ncgeek.games.shattered.utils.ShatteredMap;

public class GameScreen implements Screen {

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
	
	private IGameStateManager manager;
	private GameOptions options;
	
	private boolean bPaused;
	
	public GameScreen(IGameStateManager manager) {
		bPaused = false;
		options = new GameOptions();
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, (w / h) * 10, 10);
		camera.update();
		
		controller = new ShatteredController(camera);
		controller.setActionListener(new ActionListener() {
			@Override
			public void defaultActionPerformed() {
				if(player.hasTarget()) {
					player.getTarget().interact(player);
				}
			}
		});
		
		stage = new Stage(w/2, h/2, false);
		
		InputMultiplexer input = new InputMultiplexer(stage, controller);
		Gdx.input.setInputProcessor(input);
		
		font = new BitmapFont();
		batch = new SpriteBatch();

		map = new ShatteredMap("test");
		
		createUI();
		
		player = (Mob)map.getEntityByName("player").get(0);
		player.update(0f);
		Vector3 curr = new Vector3(player.getPosition().x, player.getPosition().y, 0);
		camera.position.set(curr);
		
		//isTouchscreen = Gdx.input.isPeripheralAvailable(Peripheral.MultitouchScreen);
		
		this.manager = manager;
	}
	
	@Override
	public void render(float delta) {
		Color bgColor = map.getBackgroundColor();
		Gdx.gl.glClearColor(bgColor.r, bgColor.g, bgColor.b, 1f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		float x = 0, y = 0;
		
		if(pad != null && pad.isTouched()) {
			x = pad.getKnobPercentX();
			y = pad.getKnobPercentY();
		} else if(controller.hasMovement()) {
			Vector2 movement = controller.getMovement();
			x = movement.x;
			y = movement.y;
		}
		
		if(!bPaused) {
			if(x != 0 || y != 0) {
				player.move(TOUCHPAD_MAX_SCROLL_SPEED * x, TOUCHPAD_MAX_SCROLL_SPEED * y);
				
				Vector3 curr = new Vector3(player.getPosition().x, player.getPosition().y, 0);
				camera.position.set(curr);
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
		
		btnMenu.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				manager.setState(GameState.Menu);
				//map.setShowDebug(!map.isShowDebug());
			}
		});
		
		parent.row();
		
		if(isTouchscreen) {
			pad = new Touchpad(10f, skin);
			
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

	@Override
	public void resize(int width, int height) {}

	@Override
	public void show() {
		resume();
	}

	@Override
	public void hide() {
		pause();
	}

	@Override
	public void pause() {
		bPaused = true;
	}

	@Override
	public void resume() {
		bPaused = false;
	}

	@Override
	public void dispose() {
		batch.dispose();
		map.dispose();
	}

}

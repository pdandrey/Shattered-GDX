package com.ncgeek.games.shattered;


import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.ncgeek.games.shattered.dialog.Dialog;
import com.ncgeek.games.shattered.entities.Chest;
import com.ncgeek.games.shattered.entities.Livestock;
import com.ncgeek.games.shattered.entities.Mob;
import com.ncgeek.games.shattered.utils.ActionListener;
import com.ncgeek.games.shattered.utils.Log;
import com.ncgeek.games.shattered.utils.ShatteredController;
import com.ncgeek.games.shattered.utils.ShatteredMap;
import com.ncgeek.games.shattered.utils.ShatteredMapLoader;

public class Shattered implements ApplicationListener {

	@SuppressWarnings("unused")
	private final static String LOG_TAG = "Shattered";
	private final static float TOUCHPAD_MAX_SCROLL_SPEED = 3f; //2.1f;
	
	private ShatteredMap map;
	private OrthographicCamera camera;
	private ShatteredController controller;
	private BitmapFont font;
	private SpriteBatch batch;
	private Stage stage;
	
	private Touchpad pad;
	
	private Mob player;
	
	private boolean isTouchscreen = true;
	
	@Override
	public void create() {		
		
		ShatteredMapLoader.addMapping("mob", Mob.class);
		ShatteredMapLoader.addMapping("chest", Chest.class);
		ShatteredMapLoader.addMapping("livestock", Livestock.class);
		
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
		player.update();
		Vector3 curr = new Vector3(player.getPosition().x, player.getPosition().y, 0);
		camera.position.set(curr);
		
		//isTouchscreen = Gdx.input.isPeripheralAvailable(Peripheral.MultitouchScreen);
	}

	@Override
	public void dispose() {
		batch.dispose();
		map.dispose();
	}

	@Override
	public void render() {		
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
		
		if(x != 0 || y != 0) {
			player.walk();
			if(player.isAnimating())
				player.move(TOUCHPAD_MAX_SCROLL_SPEED * x, TOUCHPAD_MAX_SCROLL_SPEED * y);
			
			if(Math.abs(x) > Math.abs(y)) {
				if(x > y)
					player.turn(1);
				else if(x < y)
					player.turn(3);
			} else {
				if(y > x)
					player.turn(2);
				else if(y < x)
					player.turn(0);
			}
			
			Vector3 curr = new Vector3(player.getPosition().x, player.getPosition().y, 0);
			camera.position.set(curr);
		} else {
			player.stand();
		}
		
		clampCamera();
		//camera.update();
		map.render(camera);
		
		batch.begin();
		
		Vector3 pos = new Vector3(player.getPosition().x, player.getPosition().y, 0);
		
		font.draw(batch, String.format("FPS: %d, Camera: %f, %f, Player: %f, %f", Gdx.graphics.getFramesPerSecond(), camera.position.x, camera.position.y, pos.x, pos.y), 10, 20);
		batch.end();
		
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
//		Table.drawDebug(stage);
	}
	
	public void clampCamera() {
		camera.position.x = Math.max(camera.position.x, camera.viewportWidth / 2);
		camera.position.x = Math.min(camera.position.x, map.getWidth() - camera.viewportWidth / 2);
		camera.position.y = Math.max(camera.position.y, camera.viewportHeight / 2);
		camera.position.y = Math.min(camera.position.y, map.getHeight() - camera.viewportHeight / 2);
		
		camera.update();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
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
				map.setShowDebug(!map.isShowDebug());
			}
		});
		
		parent.row();
		
		if(isTouchscreen) {
			pad = new Touchpad(10f, skin);
			pad.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					float x = Math.abs(pad.getKnobPercentX());
					float y = Math.abs(pad.getKnobPercentY());
					
					if(x > 0 && y > 0)
						player.walk();
					else
						player.stand();
				}
			});
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
}

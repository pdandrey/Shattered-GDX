package com.ncgeek.games.shattered;


import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.ncgeek.games.shattered.entities.Chest;
import com.ncgeek.games.shattered.entities.Mob;
import com.ncgeek.games.shattered.utils.OrthoCamController;
import com.ncgeek.games.shattered.utils.ShatteredMap;
import com.ncgeek.games.shattered.utils.ShatteredMapLoader;

public class Shattered implements ApplicationListener {

	private final static float TOUCHPAD_MAX_SCROLL_SPEED = 3f; //2.1f;
	
	private ShatteredMap map;
	private OrthographicCamera camera;
	private OrthoCamController cameraController;
	private BitmapFont font;
	private SpriteBatch batch;
	private Stage stage;
	
	private Touchpad pad;
	
	private Mob player;
	
	@Override
	public void create() {		
		
		ShatteredMapLoader.addMapping("mob", Mob.class);
		ShatteredMapLoader.addMapping("chest", Chest.class);
		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, (w / h) * 10, 10);
		camera.update();
		
		cameraController = new OrthoCamController(camera);
		
		stage = new Stage(w/2, h/2, false);
		
		InputMultiplexer input = new InputMultiplexer(stage, cameraController);
		Gdx.input.setInputProcessor(input);
		
		font = new BitmapFont();
		batch = new SpriteBatch();

		map = new ShatteredMap("test");
		
		createUI();
		
		player = (Mob)map.getEntityByName("player").get(0);
		player.update();
		Vector3 curr = new Vector3(player.getPosition().x, player.getPosition().y, 0);
		camera.position.set(curr);
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
		
		if(pad != null && pad.isTouched()) {
			float x = pad.getKnobPercentX();
			float y = pad.getKnobPercentY();
			
			if(player.isAnimating())
				player.move(TOUCHPAD_MAX_SCROLL_SPEED * x, TOUCHPAD_MAX_SCROLL_SPEED * y);
			//camera.update();
			
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
		Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		skin.add("touchpad_background", new Texture(Gdx.files.internal("data/controller_base.png")));
		skin.add("touchpad_knob", new Texture(Gdx.files.internal("data/controller_knob.png")));
		
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
		
		TouchpadStyle tpStyle = new TouchpadStyle(skin.getDrawable("touchpad_background"), skin.getDrawable("touchpad_knob"));
		
		pad = new Touchpad(10f, tpStyle);
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
		parent.row();
		parent.add(pad).expand().bottom().left();
		
		stage.addActor(parent);
	}
}

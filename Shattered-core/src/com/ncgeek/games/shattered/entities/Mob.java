package com.ncgeek.games.shattered.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Json;
import com.ncgeek.games.shattered.Shattered;
import com.ncgeek.games.shattered.dialog.Conversation;
import com.ncgeek.games.shattered.entities.movement.Movement;
import com.ncgeek.games.shattered.shapes.IShape;
import com.ncgeek.games.shattered.shapes.Rectangle;
import com.ncgeek.games.shattered.utils.Log;

public class Mob extends EntitySprite {

	private static final String LOG_TAG = "Mob";
	private static final String CONVERSATION = "conversation";
	private static final String MOVEMENT = "movement";
	private static final String SLEEP = "sleep";
	private static final int DEFAULT_SLEEP = 15;
	
	private int dir = 0;
	private Conversation conversation;
	protected Movement movement;
	protected boolean shouldMove = false;
	
	private float sleepTotal;
	private float sleepCurrent;
	private boolean bIsAsleep;
	
	public Mob() {
		super();
	}
	
	public final int getDirection() { return dir; }
	
	@Override
	public void load(MapProperties props, IShape bounds) {
		if(!props.containsKey(Sprite.SPRITE_WIDTH))
			props.put(Sprite.SPRITE_WIDTH, 64);
		if(!props.containsKey(SPRITE_HEIGHT))
			props.put(Sprite.SPRITE_HEIGHT, 64);
		
		if(props.get(Sprite.SPRITE_IMAGE) == null)
			props.put(Sprite.SPRITE_IMAGE, "female_greendress");
		
		sleepTotal = props.get(SLEEP, DEFAULT_SLEEP, Integer.class);
		sleepCurrent = 0;
		bIsAsleep = false;
		
		int x = (Integer)props.get("x");
		int width = (Integer)props.get("width");
		x += width/2;
		props.put("x", x);
		
		int y = (Integer)props.get("y");
		y += 16;
		props.put("y", y);
		
		super.load(props, bounds);
		
		addAnimation("walknorth", 0.15f, Animation.LOOP);
		addAnimation("walkwest", 0.15f, Animation.LOOP);
		addAnimation("walkeast", 0.15f, Animation.LOOP);
		addAnimation("walksouth", 0.15f, Animation.LOOP);
		
		setCurrentAnimation("walksouth");
		
		final String defaultConv = "{lines:[\"Hello there. This is a really long sentance that I am sure that you have no interest in at all.\n\nWord 1\nWord 2\nWord 3\nWord 4\nWord 5\nWord 6\nWord 7\"]}";
		
		String c = props.get(CONVERSATION, defaultConv, String.class).replaceAll("&quot;", "\"");
		
		Json j = new Json();
		conversation = j.fromJson(Conversation.class, c);
		
		String m = props.get(MOVEMENT, String.class);
		if(m != null) {
			movement = j.fromJson(Movement.class, m);
		}
		
		getOffset().set(-32, -10);
	}
	
	@Override
	public void setupBody(World world, float unitScale) {
		BodyDef def = new BodyDef();
		def.type = BodyType.DynamicBody;
		def.fixedRotation = false;
		def.position.set(getPosition().cpy().scl(unitScale));
		
		CircleShape shape = new CircleShape();
		shape.setRadius(10f * unitScale);
		
		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.density = 1;
		fixture.friction = 5;
		
		Body body = world.createBody(def);
		setBody(body);
		
		body.createFixture(fixture);
		shape.dispose();
	}

	public void turn() {
		turn((dir + 1) % 4);
	}
	
	public void turn(int dir) {
		if(this.dir == dir)
			return;
		
		this.dir = dir;
		
		switch(dir) {
		case 0: setCurrentAnimation("walksouth"); break;
		case 1: setCurrentAnimation("walkeast"); break;
		case 2: setCurrentAnimation("walknorth"); break;
		case 3: setCurrentAnimation("walkwest"); break;
		}
		resetAnimation();
	}
	
	public void walk() {
		if(isAnimating())
			return;
		
		setIsAnimating(true);
		resetAnimation();
	}
	public void stand() {
		if(!isAnimating())
			return;
		
		setIsAnimating(false);
		resetAnimation();
		getVelocity().set(0, 0);
	}
	
	public void move(float x, float y) {
		getVelocity().set(x, y);
		
		if(x != 0 || y != 0) {
			if(Math.abs(x) > Math.abs(y)) {
				if(x > y)
					turn(1);
				else if(x < y)
					turn(3);
			} else {
				if(y > x)
					turn(2);
				else if(y < x)
					turn(0);
			}
			walk();
		} else {
			stand();
		}
	}	
	public void move(Vector2 v) { move(v.x, v.y); }
	
	public final boolean isAsleep() { return bIsAsleep; }
	public final void sleep() { bIsAsleep = true; sleepCurrent = 0; onSleep(); }
	public final void wake() { bIsAsleep = false; onWake(); }
	
	protected void onSleep() {}
	protected void onWake() {}
	
	@Override
	public void interact(EntitySprite target) {
		//conversation.getLines()[0] = String.format("Position is %s\nBodyPosition is %s\nBounds are %s\nDestination is %s (left: %f)", getPosition().cpy().add(getOffset()).toString(), getBody().getPosition().cpy().scl(32f), getBounds(), movement == null ? "null" : movement.getDestination(), movement.getDestination().dst(getPosition().cpy().add(getOffset())));
		conversation.begin();
		walk();
	}
	
	@Override
	public void update() {
		if(bIsAsleep) {
			sleepCurrent += Gdx.graphics.getDeltaTime();
			if(sleepCurrent > sleepTotal) {
				sleepCurrent = 0;
				wake();
			}
		}
		if(!bIsAsleep && movement != null) {
			Vector2 pos = getBody().getPosition().cpy().scl(32);
			if(movement.isAtDestination(pos)) {
				movement.clearDestination();
				move(0,0);
				sleep();
			} else {
				if(!movement.hasDestination()) {
					movement.getNextDestination(getBody().getPosition().cpy().scl(32), getBounds());
				}
//				if(shouldMove) {
					Vector2 d = movement.getDestination().sub(pos);
					move(d.clamp(-1, 1));
//				}
			}
		}
		super.update();
	}

	@Override
	public void draw(SpriteBatch batch, float unitScale) {
		super.draw(batch, unitScale);
	}
	
	@Override
	public void drawDebug(ShapeRenderer shape, float unitScale) {
		shape.begin(ShapeType.Line);
		shape.setColor(Color.RED);
		shape.rect(getBounds().getX() * unitScale, getBounds().getY() * unitScale, getBounds().getWidth() * unitScale, getBounds().getHeight() * unitScale);
		if(movement != null && movement.hasDestination()) {
			shape.setColor(Color.GREEN);
			Vector2 v = movement.getDestination().scl(unitScale);
			Vector2 v1 = getBody().getPosition();
			shape.line(v1.x, v1.y, v.x, v.y);
		}
		shape.end();
	}
}

package com.ncgeek.games.shattered.entities;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.ncgeek.games.shattered.shapes.IShape;
import com.ncgeek.games.shattered.utils.Log;
import com.badlogic.gdx.graphics.g2d.Animation;

public class AnimatedSprite extends Sprite {

	private static final String LOG_TAG = "AnimatedSprite";
	
	private TextureAtlas atlas;
	private HashMap<String, Animation> mapAnimations;
	private float stateTime;
	private String currentAnimationKey;
	private boolean _isAnimating;
	
	public AnimatedSprite() {
		mapAnimations = new HashMap<String, Animation>();
		stateTime = 0f;
		_isAnimating = false;
		currentAnimationKey = null;
	}
	
	public void load(MapProperties props, IShape bounds) {
		super.load(props, bounds);
	}

	@Override
	protected void loadImage(String packfile) {
		if(atlas != null)
			atlas.dispose();
		packfile = String.format("packs/%s.pack", packfile);
		atlas = new TextureAtlas(packfile);
	}
	
	public void addAnimation(String name, float animationSpeed, int animationType) {
		Array<AtlasRegion> regions = atlas.findRegions(name);
		if(regions != null && regions.size > 0) {
			mapAnimations.put(name.toLowerCase(), new Animation(animationSpeed, regions, animationType));
		} else {
			Log.warn(LOG_TAG, "Attempt to get null or empty animation \"%s\" for a %s", name, this.getClass().getName());
		}
	}
	
	public void setCurrentAnimation(String name) {
		name = name.toLowerCase();
		if(mapAnimations.containsKey(name))
			currentAnimationKey = name;
	}
	
	public final void resetAnimation() { stateTime = 0f; }
	
	public final void setIsAnimating(boolean isAnimating) { _isAnimating = isAnimating; }
	public final boolean isAnimating() { return _isAnimating; }
	
	@Override
	public void dispose() {
		super.dispose();
		if(atlas != null) {
			atlas.dispose();
			atlas = null;
			currentAnimationKey = null;
		}
	}
	
	@Override
	public void update() {
		super.update();
		
		if(_isAnimating)
			stateTime += Gdx.graphics.getDeltaTime();
	}
	
	@Override
	public void draw(SpriteBatch batch, float unitScale) {
		if(currentAnimationKey != null) {
			TextureRegion frame = mapAnimations.get(currentAnimationKey).getKeyFrame(stateTime);
			Vector2 position = getPosition().cpy().scl(unitScale);
			batch.draw(frame, position.x, position.y, frame.getRegionWidth() * unitScale, frame.getRegionHeight() * unitScale);
		} else {
			Log.logOnce(LOG_TAG, "currentAnimationKey", "Attempt to draw without the current animation key set");
		}
	}
}

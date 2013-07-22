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
	
	protected final TextureAtlas getAtlas() { return atlas; }
	
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
		addAnimation(name, regions, animationSpeed, animationType);
	}
	
	public void addAnimation(String name, Array<AtlasRegion> regions, float animationSpeed, int animationType) {
		if(regions != null && regions.size > 0) {
			mapAnimations.put(name.toLowerCase(), new Animation(animationSpeed, regions, animationType));
		} else {
			Log.warn(LOG_TAG, "Attempt to get null or empty animation \"%s\" for a %s", name, this.getClass().getName());
		}
	}
	
	public void setCurrentAnimation(String name) {
		name = name.toLowerCase();
		if(mapAnimations.containsKey(name)) {
			currentAnimationKey = name;
			stateTime = 0;
		} else {
			Log.warn(LOG_TAG, "Could not find animation '%s' for '%s'", name, getName());
		}
	}
	
	public final void resetAnimation() { stateTime = 0f; }
	
	public final void setIsAnimating(boolean isAnimating) { _isAnimating = isAnimating; }
	public final boolean isAnimating() { return _isAnimating; }
	public final boolean isAnimationFinished() { 
		if(currentAnimationKey != null) {
			return mapAnimations.get(currentAnimationKey).isAnimationFinished(stateTime);
		}
		return false;
	}
	
	protected void animationFinished(String key) {}
	
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
	public void update(float delta) {
		super.update(delta);
		
		if(_isAnimating)
			stateTime += delta;
	}
	
	@Override
	public void draw(SpriteBatch batch, float unitScale) {
		if(currentAnimationKey != null) {
			Animation anim = mapAnimations.get(currentAnimationKey);
			TextureRegion frame = anim.getKeyFrame(stateTime);
			Vector2 position = getPosition().cpy().scl(unitScale);
			batch.draw(frame, position.x, position.y, frame.getRegionWidth() * unitScale, frame.getRegionHeight() * unitScale);
			if(anim.isAnimationFinished(stateTime))
				animationFinished(currentAnimationKey);
		} else {
			Log.logOnce(LOG_TAG, "currentAnimationKey", "Attempt to draw without the current animation key set");
		}
	}
}

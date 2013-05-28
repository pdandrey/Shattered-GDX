package com.ncgeek.games.shattered.utils;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.ncgeek.games.shattered.entities.Sprite;

public class TiledMapObjectRenderer extends OrthogonalTiledMapRenderer {

	public TiledMapObjectRenderer(TiledMap map) {
		super(map);
	}
	
	public TiledMapObjectRenderer(TiledMap map, float unitScale) {
		super(map, unitScale);
	}

	public void renderObject(MapObject object) {
		if(object instanceof Sprite) {
			Sprite s = (Sprite)object;
			//s.update();
			s.draw(getSpriteBatch(), unitScale);
		}
		super.renderObject(object);
	}
}

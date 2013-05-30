package com.ncgeek.games.shattered.utils;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.utils.Array;
import com.ncgeek.games.shattered.entities.EntitySprite;
import com.ncgeek.games.shattered.entities.Sprite;

public class TiledMapObjectRenderer extends OrthogonalTiledMapRenderer {

	public TiledMapObjectRenderer(TiledMap map) {
		super(map);
	}
	
	public TiledMapObjectRenderer(TiledMap map, float unitScale) {
		super(map, unitScale);
	}
	
	@Override
	public void render () {
		AnimatedTiledMapTile.updateAnimationBaseTime();
		spriteBatch.begin();
		for (MapLayer layer : map.getLayers()) {
			if (layer.isVisible()) {
				if (layer instanceof TiledMapTileLayer) {
					renderTileLayer((TiledMapTileLayer) layer);
				} else {
					Array<Sprite> arr = new Array<Sprite>();
					for (MapObject object : layer.getObjects()) {
						if(object instanceof Sprite)
							arr.add((Sprite)object);
						//renderObject(object);
					}
					arr.sort();
					for(Sprite s : arr)
						renderObject(s);
				}	
			}	
		}
		spriteBatch.end();
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

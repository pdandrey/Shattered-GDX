package com.ncgeek.games.shattered.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer10;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.esotericsoftware.tablelayout.Cell;
import com.ncgeek.games.shattered.characters.ShatteredCharacter;
import com.ncgeek.games.shattered.utils.Log;

public class CharacterListItem extends WidgetGroup {

	private static final String LOG_TAG = "CharacterListItem";
	
	private ShatteredCharacter character;
	private Image image;
	private TextureRegionDrawable drawable;
	private Label txtName;
	private Label txtSoul;
	private float stateTime = 0f;
	
	private ImmediateModeRenderer debugRenderer;
		
	public CharacterListItem(ShatteredCharacter character, Skin skin) {
		super();
		
		drawable = new TextureRegionDrawable();
		image = new Image(drawable);
		
		Table subtable = new Table();
		subtable.debugTable();
		
		txtName = new Label("", skin);
		txtSoul = new Label("", skin);
		
		setCharacter(character);
		
		setTouchable(Touchable.enabled);
		
		this.addActor(image);
		this.addActor(txtName);
		this.addActor(txtSoul);
	}
	
	public void setCharacter(ShatteredCharacter character) {
		this.character = character;
		drawable.setRegion(character.getAnimation().getKeyFrame(0));
		txtName.setText(character.getName());
		txtSoul.setText(character.getSoul());
	}
	
	public ShatteredCharacter getCharacter() { return character; }

	@Override
	public float getPrefWidth() {
		return 192;
	}

	@Override
	public float getPrefHeight() {
		return 64;
	}

	@Override
	public float getMinWidth() {
		return getPrefWidth();
	}

	@Override
	public float getMinHeight() {
		return getPrefHeight();
	}

	@Override
	public float getMaxWidth() {
		return getPrefWidth();
	}

	@Override
	public float getMaxHeight() {
		return getPrefHeight();
	}

	@Override
	public void layout() {
		image.setPosition(0, 0);
		image.setSize(64, 64);
		txtName.setPosition(64, getPrefHeight() - txtName.getPrefHeight() - 5);
		txtName.setSize(txtName.getPrefWidth(), txtName.getPrefHeight());
		txtSoul.setPosition(64, txtName.getY() - txtSoul.getPrefHeight() + 5);
		txtSoul.setSize(txtSoul.getPrefWidth(), txtSoul.getPrefHeight());
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		stateTime += Gdx.graphics.getDeltaTime();
		drawable.setRegion(character.getAnimation().getKeyFrame(stateTime));
		
		super.draw(batch, parentAlpha);
	}
	
	public void drawDebug (SpriteBatch batch) {
		if (debugRenderer == null) {
			if (Gdx.graphics.isGL20Available())
				debugRenderer = new ImmediateModeRenderer20(64, false, true, 0);
			else
				debugRenderer = new ImmediateModeRenderer10(64);
		}

		float x = getX(), y = getY();

		debugRenderer.begin(batch.getProjectionMatrix(), GL10.GL_LINES);
		
		float x1 = x;
		float y1 = y;
		float x2 = x1 + getPrefWidth();
		float y2 = y1 + getPrefHeight();
		drawDebugItem(x1, y1, x2, y2, 1,0,0);
		
		for(Actor a : getChildren()) {
			if (debugRenderer.getNumVertices() == 64) {
				debugRenderer.end();
				debugRenderer.begin(batch.getProjectionMatrix(), GL10.GL_LINES);
			}
			
			x1 = x + a.getX();
			y1 = y + a.getY();
			x2 = x1 + a.getWidth();
			y2 = y1 + a.getHeight();
			drawDebugItem(x1, y1, x2, y2, 0,1,0);
					
		}
		
		debugRenderer.end();
	}
	
	private void drawDebugItem(float x1, float y1, float x2, float y2, float r, float g, float b) {
		

		debugRenderer.color(r, g, b, 1);
		debugRenderer.vertex(x1, y1, 0);
		debugRenderer.color(r, g, b, 1);
		debugRenderer.vertex(x1, y2, 0);

		debugRenderer.color(r, g, b, 1);
		debugRenderer.vertex(x1, y2, 0);
		debugRenderer.color(r, g, b, 1);
		debugRenderer.vertex(x2, y2, 0);

		debugRenderer.color(r, g, b, 1);
		debugRenderer.vertex(x2, y2, 0);
		debugRenderer.color(r, g, b, 1);
		debugRenderer.vertex(x2, y1, 0);

		debugRenderer.color(r, g, b, 1);
		debugRenderer.vertex(x2, y1, 0);
		debugRenderer.color(r, g, b, 1);
		debugRenderer.vertex(x1, y1, 0);

		
	}
}

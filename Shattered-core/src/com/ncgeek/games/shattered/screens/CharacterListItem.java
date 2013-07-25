package com.ncgeek.games.shattered.screens;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.esotericsoftware.tablelayout.Cell;
import com.ncgeek.games.shattered.characters.ShatteredCharacter;
import com.ncgeek.games.shattered.utils.Log;

public class CharacterListItem extends Table {

	private ShatteredCharacter character;
	private Image image;
	private Label txtName;
	private Label txtSoul;
		
	public CharacterListItem(ShatteredCharacter character, Skin skin) {
		super();
		
		image = new Image();
		add(image);
		
		Table subtable = new Table();
		subtable.debugTable();
		
		txtName = new Label("", skin);
		txtSoul = new Label("", skin);
		
		subtable.add(txtName);
		subtable.row();
		subtable.add(txtSoul);
		
		setCharacter(character);
		
		Cell c = add(subtable).top().left();
		c.setWidgetWidth(128);
		c.setWidgetHeight(64);		
		
		setTouchable(Touchable.enabled);
	}
	
	public void setCharacter(ShatteredCharacter character) {
		this.character = character;
		image.setDrawable(new TextureRegionDrawable(character.getAnimation().getKeyFrame(0)));
		txtName.setText(character.getName());
		txtSoul.setText(character.getSoul());
	}
	
	public ShatteredCharacter getCharacter() { return character; }
	
	public void size() {
		Log.log("CharacterListItem", "%s size is %f x %f", 
				character.getName(), 
				this.getPrefWidth(),
				this.getPrefHeight());
	}
}

package com.ncgeek.games.shattered.screens;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.utils.SnapshotArray;

public class PartyLayout extends WidgetGroup {
	
	public PartyLayout() {
		super.setFillParent(false);
	}
	
	public void add(CharacterListItem cli) {
		super.addActor(cli);
	}
	
	public void addActor(Actor a) {
		if(a instanceof CharacterListItem)
			add((CharacterListItem)a);
		else
			throw new IllegalArgumentException("Actor must be a CharacterListItem");
	}

	@Override
	public void layout() {
		SnapshotArray<Actor> children = getChildren();
		
		if(children.size == 0)
			return;
		
		CharacterListItem cli = (CharacterListItem)children.get(0);
		cli.validate();
		
		final float width = getWidth();
		final int PER_ROW = (int)(width / cli.getPrefWidth());
		final int ROWS = (int)Math.ceil((double)children.size / (double)PER_ROW);
		int count = 0;
		
		setWidth(PER_ROW * cli.getPrefWidth());
		setHeight(Math.max(((ScrollPane)getParent()).getHeight(), (ROWS) * cli.getPrefHeight()));
		
		float x = 0;
		float y = getHeight() - cli.getPrefHeight();
		
		
		for(Actor a : children) {
			cli = (CharacterListItem)a;
			cli.validate();
			if(count >= PER_ROW) {
				count = 0;
				x = 0;
				y -= cli.getPrefHeight();
			}
			
			((CharacterListItem)a).layout();
			a.setPosition(x, y);
			x += cli.getPrefWidth();
			++count;
		}
	}

	@Override
	public float getPrefWidth() {
		return getWidth();
	}

	@Override
	public float getPrefHeight() {
		return getHeight();
	}
	
	@Override
	public void setFillParent(boolean fill) {
		throw new IllegalArgumentException("setFillParent cannot be called");
	}
}

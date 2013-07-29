package com.ncgeek.games.shattered.screens;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Layout;
import com.badlogic.gdx.utils.SnapshotArray;
import com.ncgeek.games.shattered.utils.Log;

public class PartyLayout extends WidgetGroup {

	private static final String LOG_TAG = "PartyLayout";

	private float prefWidth, prefHeight;
	private int cols = 1;
	private boolean sizeInvalid = true;
	
	public PartyLayout () {
		setTouchable(Touchable.childrenOnly);
	}

	public void invalidate () {
		super.invalidate();
		sizeInvalid = true;
	}

	private void computeSize () {
		sizeInvalid = false;
		prefWidth = 0;
		prefHeight = 0;
		float groupWidth = 0;
		
		// get the stage since getWidth() isn't reliable until layout();
		float w = getStage().getWidth();
		groupWidth = w - GameMenu.WIDTH_STATS;
		
		cols = Math.max(1, (int)(groupWidth / CharacterListItem.WIDTH));
		prefWidth = cols * CharacterListItem.WIDTH;
		
		SnapshotArray<Actor> children = getChildren();
		int rows = (int)Math.ceil((double)children.size / (double)cols);
		prefHeight = rows * CharacterListItem.HEIGHT;

	}

	public void layout () {
		float y = getHeight()-CharacterListItem.HEIGHT;
		
		SnapshotArray<Actor> children = getChildren();
		for (int i = 0, n = children.size, c = 0; i < n; i++, c++) {
			Actor child = children.get(i);
			float width, height;
			if (child instanceof Layout) {
				Layout layout = (Layout)child;
				width = layout.getPrefWidth();
				height = layout.getPrefHeight();
			} else {
				width = child.getWidth();
				height = child.getHeight();
			}
			
			if(c >= cols) {
				y -= height;
				c = 0;
			}
			
			float x = c * CharacterListItem.WIDTH;
			child.setBounds(x, y, width, height);
		}
	}

	public float getPrefWidth () {
		if (sizeInvalid) 
			computeSize();
		return prefWidth;
	}

	public float getPrefHeight () {
		if (sizeInvalid) 
			computeSize();
		return prefHeight;
	}
}

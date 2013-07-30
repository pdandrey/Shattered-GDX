package com.ncgeek.games.shattered.screens;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Layout;
import com.badlogic.gdx.utils.SnapshotArray;
import com.ncgeek.games.shattered.utils.Log;

public class PartyLayout extends WidgetGroup {

	private static final String LOG_TAG = "PartyLayout";

	private float prefWidth, prefHeight;
	private int cols = 1;
	private boolean sizeInvalid = true;
	
	private CharacterListItem selected = null;
	
	public PartyLayout () {
		setTouchable(Touchable.childrenOnly);
		
		this.addCaptureListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Actor a = hit(x, y, true);
				while(a != null && !(a instanceof CharacterListItem)) {
					a = a.getParent();
				}
				
				if(a == null) {
					super.clicked(event, x, y);
					return;
				}
				
				CharacterListItem cli = (CharacterListItem)a;
				event.cancel();
				
				selected.setSelected(false);
				selected = cli;
				cli.setSelected(true);
				fire(new CharacterSelectedEvent(cli));
			}
		});
	}
	
	public CharacterListItem getSelectedCharacterListItem() {
		return selected;
	}

	@Override
	public void addActor(Actor actor) {
		if(!(actor instanceof CharacterListItem))
			throw new IllegalArgumentException("Can only add CharacterListItems to a PartyLayout. Attempted to add a " + actor.getClass().getName());
		super.addActor(actor);
		
		if(selected == null) {
			selected = (CharacterListItem)actor;
			selected.setSelected(true);
			fire(new CharacterSelectedEvent(selected));
		}
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
	
	public static abstract class CharacterSelectedListener implements EventListener {

		@Override
		public boolean handle(Event event) {
			if(!(event instanceof CharacterSelectedEvent))
				return false;
			
			return characterSelected((CharacterSelectedEvent)event);
		}
		
		public abstract boolean characterSelected(CharacterSelectedEvent event);
	}
	
	public static class CharacterSelectedEvent extends Event {
		private final CharacterListItem cli;
		public CharacterSelectedEvent(CharacterListItem selected) {
			super();
			cli = selected;
		}
		public final CharacterListItem getSelected() { return cli; }
	}
}

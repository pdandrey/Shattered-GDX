package com.ncgeek.games.shattered.screens.ui;

import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputEvent.Type;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Pools;

public class FocusButton extends Button implements IFocusable {

	private boolean focused = false;
	private FocusGroup focusGroup;
	
	public FocusButton(Skin skin) {
		this(skin.get(ButtonStyle.class));
		setSkin(skin);
	}

	public FocusButton(Skin skin, String styleName) {
		this(skin.get(styleName, ButtonStyle.class));
		setSkin(skin);
	}

	public FocusButton(ButtonStyle style) {
		super();
		setStyle(style);
		setWidth(getPrefWidth());
		setHeight(getPrefHeight());
	}
	
	@Override
	public boolean isFocused() { return focused; }
	
	@Override
	public void setFocused(boolean focused) { 
		if(focusGroup != null && !focusGroup.canFocus(this, focused))
			return;
		this.focused = focused; 
	}
	
	@Override
	public void setFocusGroup(FocusGroup group) { focusGroup = group; }
	
	@Override
	public FocusGroup getFocusGroup() { return focusGroup; }

	@Override
	public boolean isOver() { return focused || super.isOver(); }
	
	public void click() {
		InputEvent e = Pools.obtain(InputEvent.class);
		e.setType(Type.touchUp);
		e.setButton(0);
		e.setPointer(-1);
		e.setListenerActor(this);
		e.setStageX(getX());
		e.setStageY(getY());
		fire(e);
		Pools.free(e);
	}

//	public static class FocusButtonStyle extends ButtonStyle {
//
//		public Drawable focusedDown;
//		public Drawable focusedUp;
//		
//		public FocusButtonStyle() {}
//
//		public FocusButtonStyle(FocusButtonStyle style) {
//			super(style); 
//			focusedDown = style.focusedDown;
//			focusedUp = style.focusedUp;
//		}
//
//		
//	}
}

package com.ncgeek.games.shattered.screens.ui;

import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputEvent.Type;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Pools;

public class FocusTextButton extends TextButton implements IFocusable {

	private boolean focused = false;
	private FocusGroup focusGroup;
	
	public FocusTextButton(String text, Skin skin) {
		this(text, skin.get(TextButtonStyle.class));
		setSkin(skin);
	}

	public FocusTextButton(String text, TextButtonStyle style) {
		super(text, style);
		setStyle(style);
		setWidth(getPrefWidth());
		setHeight(getPrefHeight());
	}

	public FocusTextButton(String text, Skin skin, String styleName) {
		this(text, skin.get(styleName, TextButtonStyle.class));
		setSkin(skin);
	}

	public boolean isFocused() { return focused; }
	public void setFocused(boolean focused) {
		if(focusGroup != null && !focusGroup.canFocus(this, focused))
			return;
		this.focused = focused; 
	}
	
	public void setFocusGroup(FocusGroup group) { focusGroup = group; }
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
}

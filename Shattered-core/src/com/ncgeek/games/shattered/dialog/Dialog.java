package com.ncgeek.games.shattered.dialog;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class Dialog extends ScrollPane {

	@SuppressWarnings("unused")
	private static final String LOG_TAG = "Dialog";
	
	private Label dialog;
	
	private DialogListener callback;
	private int converstationID;
	
	private Dialog(Skin skin, String scrollStyle) {
		super(null, skin, scrollStyle);
		dialog = new Label("", skin);
		this.setWidget(dialog);
	}
	
	@Override
	public float getPrefWidth() {
		return getStage().getWidth() - 10;
	}

	@Override
	public float getPrefHeight() {
		return 100;
	}

	public boolean isDialogActive() { return scroll.isVisible(); }
	
	public void setText(String text) {
		setText(text, null, -1);
	}
	
	public void setText(String text, DialogListener callback, int conversationID) {
		this.callback = callback;
		this.converstationID = conversationID;
		scroll.setScrollY(0);
		label.setText(text);
		label.getTextBounds();
		scroll.setVisible(true);
	}
	
	public void progress() {
		if(!scroll.isScrollY() || scroll.getScrollPercentY() == 1) {
			label.setText("");
			scroll.setVisible(false);
			
			if(callback != null) {
				callback.dialogFinished(converstationID);
			}
		} else {
			scroll.setScrollY(scroll.getScrollY() + scroll.getHeight() - 20);
		}
	}
}

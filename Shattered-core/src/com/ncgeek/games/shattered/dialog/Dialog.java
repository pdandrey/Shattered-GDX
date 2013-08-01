package com.ncgeek.games.shattered.dialog;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class Dialog {

	@SuppressWarnings("unused")
	private static final String LOG_TAG = "Dialog";
	private static final Dialog INSTANCE = new Dialog();
	
	public static void init(ScrollPane scroll) {
		INSTANCE.scroll = scroll;
		INSTANCE.label = (Label)scroll.findActor("lblDialog");
		scroll.setVisible(false);
		
		scroll.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				event.handle();
				INSTANCE.progress();
			}
			
		});
	}
	
	public static Dialog getInstance() {
		return INSTANCE;
	}
	
	private Label label;
	private ScrollPane scroll;
	
	private DialogListener callback;
	private int converstationID;
	
	private Dialog() {}
	
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

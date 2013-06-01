package com.ncgeek.games.shattered.utils;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class Dialog {

	@SuppressWarnings("unused")
	private static final String LOG_TAG = "Dialog";
	private static final Dialog INSTANCE = new Dialog();
	
	public static void init(Label label) {
		INSTANCE.label = label;
		label.setVisible(false);
		
		label.addListener(new InputListener() {

			@Override
			public boolean keyUp(InputEvent event, int keycode) {
				if(keycode == Keys.ENTER) {
					INSTANCE.hide();
				}
				return true;
			}

			@Override
			public boolean keyDown(InputEvent event, int keycode) {
				return true;
			}

			@Override
			public boolean keyTyped(InputEvent event, char character) {
				return true;
			}
			
		});
		
		label.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				event.handle();
				INSTANCE.hide();
			}
			
		});
	}
	
	public static void showDialog(String text) {
		INSTANCE.setText(text);
	}
	
	private Label label;
	
	private Dialog() {}
	
	public void setText(String text) {
		label.setText(text);
		label.setVisible(true);
		label.getStage().setKeyboardFocus(label);
	}
	
	public void hide() {
		label.setText("");
		label.setVisible(false);
		label.getStage().setKeyboardFocus(null);
	}
}

package com.ncgeek.games.shattered.utils;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class ShatteredController extends InputAdapter {
	
	private static final String LOG_TAG = "ShatteredController";
	
	private final Vector2 movement = new Vector2();
	
	private GameControllerListener actionListener;

	private EventListener menuButtonClicked = new ClickListener() {
		@Override
		public void clicked(InputEvent event, float x, float y) {
			fireMenu();
			event.handle();
		}
	}; 
	
	private EventListener touchpadChanged = new ChangeListener() {
		@Override
		public void changed(ChangeEvent event, Actor actor) {
			Touchpad pad = (Touchpad)actor;
			fireAbsoluteMove(pad.getKnobPercentX(), pad.getKnobPercentY());
		}
	};
	
	private GamepadControllerListener gamepad;
	
	private int[] keysMoveUp = { Keys.UP, Keys.W };
	private int[] keysMoveDown = { Keys.DOWN, Keys.S };
	private int[] keysMoveLeft = { Keys.LEFT, Keys.A };
	private int[] keysMoveRight = { Keys.RIGHT, Keys.D };
	private int[] keysMenu = { Keys.ESCAPE, Keys.UNKNOWN };
	private int[] keysAction = { Keys.ENTER, Keys.SPACE };
	private int[] keysCancel = { Keys.BACKSPACE, Keys.UNKNOWN };
	
	private int controllerMenu = 3;
	private int controllerAction = 0;
	private int controllerCancel = 1;
	
	public ShatteredController() {
		actionListener = null;
		try {
			gamepad = new GamepadControllerListener();
		} catch(NoClassDefFoundError ex) {}
	}
	
	public void setActionListener(GameControllerListener listener) { actionListener = listener; }
	public EventListener getMenuButtonClickedListener() { return menuButtonClicked; }
	public EventListener getTouchpadChangedListener() { return touchpadChanged; }
	public ControllerListener getGamepadListener() { return gamepad; }
	
	private void fireAction() {
		if(actionListener != null) {
			actionListener.action();
		}
	}
	
	private void fireMenu() {
		if(actionListener != null) {
			actionListener.menu();
		}
	}
	
	private void fireAbsoluteMove(float x, float y) {
		movement.set(x, y);
		if(actionListener != null)
			actionListener.move(movement.x, movement.y);
	}
	
	private void fireCompoundMove(float x, float y) {
		movement.add(x, y);
		if(actionListener != null)
			actionListener.move(movement.x, movement.y);
	}
	
	private void fireCancel() {
		if(actionListener != null)
			actionListener.cancel();
	}

	@Override
	public boolean touchUp (int x, int y, int pointer, int button) {
		fireAction();
		return true;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		if(keycode == keysMoveUp[0] || keycode == keysMoveUp[1])
			fireCompoundMove(0, 1);
		else if(keycode == keysMoveDown[0] || keycode == keysMoveDown[1])
			fireCompoundMove(0, -1);
		else if(keycode == keysMoveLeft[0] || keycode == keysMoveLeft[1])
			fireCompoundMove(-1, 0);
		else if(keycode == keysMoveRight[0] || keycode == keysMoveRight[1])
			fireCompoundMove(1, 0);
		else
			return false;
		
		Log.log(LOG_TAG, "Key pressed: %d", keycode);
		
		return true;		
	}

	@Override
	public boolean keyUp(int keycode) {
		if(keycode == keysMoveUp[0] || keycode == keysMoveUp[1])
			fireCompoundMove(0, -1);
		else if(keycode == keysMoveDown[0] || keycode == keysMoveDown[1])
			fireCompoundMove(0, 1);
		else if(keycode == keysMoveLeft[0] || keycode == keysMoveLeft[1])
			fireCompoundMove(1, 0);
		else if(keycode == keysMoveRight[0] || keycode == keysMoveRight[1])
			fireCompoundMove(-1, 0);
		else if(keycode == keysMenu[0] || keycode == keysMenu[1])
			fireMenu();
		else if(keycode == keysAction[0] || keycode == keysMenu[1])
			fireAction();
		else if(keycode == keysCancel[0] || keycode == keysCancel[1])
			fireCancel();
		else
			return false;
		
		Log.log(LOG_TAG, "Key released: %d", keycode);
		
		return true;
	}
	
	private class GamepadControllerListener implements ControllerListener {
		@Override
		public void connected (Controller controller) {
			
		}

		@Override
		public void disconnected (Controller controller) {
			
		}

		@Override
		public boolean buttonDown (Controller controller, int buttonIndex) {
			return false;
		}

		@Override
		public boolean buttonUp (Controller controller, int buttonIndex) {
			if(buttonIndex == controllerMenu)
				fireMenu();
			else if(buttonIndex == controllerAction)
				fireAction();
			else if(buttonIndex == controllerCancel)
				fireCancel();
			else
				return false;
			return true;
		}

		@Override
		public boolean axisMoved (Controller controller, int axisIndex, float value) {
			
			if(Math.abs(value) < 0.2)
				value = 0;
			
			switch(axisIndex) {
				case 0: // left stick, up/down
					value = -value;
					fireAbsoluteMove(movement.x, value);
					break;
					
				case 1: // left stick, left/right
					fireAbsoluteMove(value, movement.y);
					break;
					
				default:
					return false;
			}
			return true;
		}

		@Override
		public boolean povMoved (Controller controller, int povIndex, PovDirection value) {
			switch(value) {
				case center:
					fireAbsoluteMove(0, 0);
					break;
					
				case east:
					fireAbsoluteMove(1, 0);
					break;
					
				case north:
					fireAbsoluteMove(0, 1);
					break;
					
				case northEast:
					fireAbsoluteMove(1, 1);
					break;
					
				case northWest:
					fireAbsoluteMove(-1, 1);
					break;
					
				case south:
					fireAbsoluteMove(0, -1);
					break;
					
				case southEast:
					fireAbsoluteMove(1, -1);
					break;
					
				case southWest:
					fireAbsoluteMove(-1, -1);
					break;
					
				case west:
					fireAbsoluteMove(-1, 0);
					break;
					
				default:
					return false;
			}
			return true;
		}

		@Override
		public boolean xSliderMoved (Controller controller, int sliderIndex, boolean value) {
			return false;
		}

		@Override
		public boolean ySliderMoved (Controller controller, int sliderIndex, boolean value) {
			return false;
		}

		@Override
		public boolean accelerometerMoved (Controller controller, int accelerometerIndex, Vector3 value) {
			return false;
		}
	}
}

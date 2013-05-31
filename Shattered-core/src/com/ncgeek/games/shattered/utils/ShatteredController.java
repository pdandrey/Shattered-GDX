package com.ncgeek.games.shattered.utils;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class ShatteredController extends InputAdapter {
	private final OrthographicCamera camera;
	private final Vector3 curr = new Vector3();
	private final Vector3 last = new Vector3(-1, -1, -1);
	private final Vector3 delta = new Vector3();
	private final Vector2 movement = new Vector2();
	
	private ActionListener actionListener;

	public ShatteredController (OrthographicCamera camera) {
		this.camera = camera;
		actionListener = null;
	}
	
	public void setActionListener(ActionListener listener) { actionListener = listener; }
	
	public final Vector2 getMovement() { return movement.cpy().clamp(-1, 1); }
	public final boolean hasMovement() { return movement.x != 0 || movement.y != 0; }

	@Override
	public boolean touchDragged (int x, int y, int pointer) {
		camera.unproject(curr.set(x, y, 0));
		if (!(last.x == -1 && last.y == -1 && last.z == -1)) {
			camera.unproject(delta.set(last.x, last.y, 0));
			delta.sub(curr);
			camera.position.add(delta.x, delta.y, 0);
		}
		last.set(x, y, 0);
		return false;
	}

	@Override
	public boolean touchUp (int x, int y, int pointer, int button) {
		last.set(-1, -1, -1);
		
		if(actionListener != null) {
			actionListener.defaultActionPerformed();
			return true;
		}
		return false;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		switch(keycode) {
			case Keys.UP:
			case Keys.W:
				movement.y += 1;
				return true;
				
			case Keys.DOWN:
			case Keys.S:
				movement.y -= 1;
				return true;
				
			case Keys.LEFT:
			case Keys.A:
				movement.x -= 1;
				return true;
				
			case Keys.RIGHT:
			case Keys.D:
				movement.x += 1;
				return true;
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		switch(keycode) {
			case Keys.UP:
			case Keys.W:
				movement.y -= 1;
				return true;
				
			case Keys.DOWN:
			case Keys.S:
				movement.y += 1;
				return true;
				
			case Keys.LEFT:
			case Keys.A:
				movement.x += 1;
				return true;
				
			case Keys.RIGHT:
			case Keys.D:
				movement.x -= 1;
				return true;
				
			case Keys.ENTER:
			case Keys.SPACE:
				if(actionListener != null)
					actionListener.defaultActionPerformed();
				return true;
		}
		return false;
	}
}

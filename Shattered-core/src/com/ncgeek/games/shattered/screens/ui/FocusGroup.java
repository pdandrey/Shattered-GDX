package com.ncgeek.games.shattered.screens.ui;

import com.badlogic.gdx.utils.Array;

public class FocusGroup {

	private Array<IFocusable> widgets = new Array<IFocusable>();
	private Array<IFocusable> focusedWidgets = new Array<IFocusable>(1);
	private int maxFocus;
	private int minFocus;
	private boolean unfocusLast = true;
	private IFocusable lastFocused;
	
	public FocusGroup() {
		maxFocus = 1;
		minFocus = 1;
	}

	public FocusGroup(IFocusable...focusables) {
		this();
		add(focusables);
	}

	public void add(IFocusable focusable) {
		widgets.add(focusable);
		focusable.setFocusGroup(this);
		if(focusedWidgets.size < minFocus) {
			focusable.setFocused(true);
		}
	}
	
	public void add(IFocusable...focusables) {
		for(IFocusable f : focusables)
			add(f);
	}

	public void remove(IFocusable focusable) {
		focusedWidgets.removeValue(focusable, true);
		if(widgets.removeValue(focusable, true)) {
			focusable.setFocused(false);
			focusable.setFocusGroup(null);
		}
	}
	
	public void remove(IFocusable...focusables) {
		for(IFocusable f : focusables)
			remove(f);
	}

	protected boolean canFocus(IFocusable focusable, boolean newState) {
		if (focusable.isFocused() == newState) 
			return false;

		if (!newState) {
			// Keep button checked to enforce minCheckCount.
			if (focusedWidgets.size <= minFocus) 
				return false;
			focusedWidgets.removeValue(focusable, true);
		} else {
			// Keep button unchecked to enforce maxCheckCount.
			if (maxFocus != -1 && focusedWidgets.size >= maxFocus) {
				if (unfocusLast && lastFocused != null) {
					int old = minFocus;
					minFocus = 0;
					lastFocused.setFocused(false);
					minFocus = old;
				} else
					return false;
			}
			focusedWidgets.add(focusable);
			lastFocused = focusable;
		}

		return true;
	}

	public void unfocusAll() {
		int old = minFocus;
		minFocus = 0;
		for (int i = 0, n = widgets.size; i < n; i++) {
			IFocusable f = widgets.get(i);
			f.setFocused(false);
		}
		minFocus = old;
	}

	public IFocusable getFocused() {
		if(focusedWidgets.size > 0)
			return focusedWidgets.get(0);
		else
			return null;
	}

	public Array<IFocusable> getAllFocused() {
		return focusedWidgets;
	}
	
	public Array<IFocusable> getFocusables() { return widgets; }

	public void setMinFocusCount(int minFocusCount) {
		minFocus = minFocusCount;
	}

	public void setMaxFocusCount(int maxFocusCount) {
		maxFocus = maxFocusCount;
	}

	public void setUnfocusLast(boolean unfocusLast) {
		this.unfocusLast = unfocusLast;
	}

	
}

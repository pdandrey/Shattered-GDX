package com.ncgeek.games.shattered.screens.ui;

public interface IFocusable {
	public boolean isFocused();
	public void setFocused(boolean focused);
	
	public void setFocusGroup(FocusGroup group);
	public FocusGroup getFocusGroup();
}
